package com.example.hivesampling.monitor;

import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.ShardTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.repository.TaskStore;
import com.example.hivesampling.service.ShardExecutionService;
import com.example.hivesampling.service.SqlBuilderService;
import com.example.hivesampling.service.TaskLogStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SampleTaskMonitorService {

    private final int maxAttemptsPerShard;
    private final TaskStore taskRepository;
    private final ShardExecutionService shardExecutionService;
    private final SqlBuilderService sqlBuilderService;
    private final TaskLogStore taskLogService;

    public SampleTaskMonitorService(
            @Value("${sampling.max-attempts-per-shard:3}") int maxAttemptsPerShard,
            TaskStore taskRepository,
            ShardExecutionService shardExecutionService,
            SqlBuilderService sqlBuilderService,
            TaskLogStore taskLogService) {
        this.maxAttemptsPerShard = maxAttemptsPerShard;
        this.taskRepository = taskRepository;
        this.shardExecutionService = shardExecutionService;
        this.sqlBuilderService = sqlBuilderService;
        this.taskLogService = taskLogService;
    }

    @Scheduled(fixedDelay = 1000)
    public void collectResults() {
        taskRepository.findActiveTasks().stream()
                .filter(context -> context.status != ParentTaskStatus.PIPELINE_RUNNING)
                .forEach(this::collectTask);
    }

    private void collectTask(TaskContext context) {
        try {
        for (ShardTask shard : context.shards) {
            if (shard.status == ShardTaskStatus.SUCCESS && !shard.resultCollected) {
                shard.resultCollected = true;
                context.addSampledRows(shard.lastRunRows);
                taskLogService.info(context.taskId, "ResultCollectorExecutor updated sampledRows="
                        + context.sampledRows + " from " + shard.shardId);
            }
        }

        if (context.sampledRows >= context.targetSampleRows) {
            context.changeStatus(ParentTaskStatus.SUCCESS);
            cancelNotStartedShards(context);
            taskLogService.info(context.taskId, "Parent task early stopped because targetSampleRows reached");
            taskLogService.info(context.taskId, "Parent task finalized status=SUCCESS sampledRows=" + context.sampledRows);
            return;
        }

        if (context.shards.stream().anyMatch(shard -> shard.status == ShardTaskStatus.QUEUED
                || shard.status == ShardTaskStatus.RUNNING)) {
            return;
        }

        boolean canContinue = context.shards.stream().anyMatch(shard -> shard.attemptCount < maxAttemptsPerShard);
        if (canContinue) {
            for (ShardTask shard : context.shards) {
                if (shard.attemptCount < maxAttemptsPerShard) {
                    shard.innerSql = sqlBuilderService.buildInnerSql(context, shard);
                    shard.finalSql = sqlBuilderService.buildFinalSql(shard.innerSql);
                    shard.sqlPreview = shard.innerSql;
                    shard.changeStatus(ShardTaskStatus.QUEUED);
                    shard.resultCollected = false;
                }
            }
            taskLogService.info(context.taskId, "ResultCollectorExecutor target not reached, dispatch next sampling round");
            shardExecutionService.dispatchQueuedShards(context);
            return;
        }

        context.changeStatus(ParentTaskStatus.FAILED);
        taskLogService.info(context.taskId, "Parent task finalized status=FAILED sampledRows="
                + context.sampledRows + ", targetSampleRows=" + context.targetSampleRows);
        } finally {
            taskRepository.save(context);
        }
    }

    private void cancelNotStartedShards(TaskContext context) {
        for (ShardTask shard : context.shards) {
            if (shard.status == ShardTaskStatus.QUEUED) {
                shard.changeStatus(ShardTaskStatus.CANCELLED);
            }
        }
    }
}
