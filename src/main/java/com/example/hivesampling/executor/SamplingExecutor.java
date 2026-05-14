package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.SampleTaskExecutor;
import com.example.hivesampling.service.SqlBuilderService;
import com.example.hivesampling.service.TaskLogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Order(3)
@Component
public class SamplingExecutor implements SampleTaskExecutor {

    private final int shardSize;
    private final int plannedRowsPerRun;
    private final SqlBuilderService sqlBuilderService;
    private final TaskLogService taskLogService;

    public SamplingExecutor(
            @Value("${sampling.shard-size:5}") int shardSize,
            @Value("${sampling.planned-rows-per-run:1000}") int plannedRowsPerRun,
            SqlBuilderService sqlBuilderService,
            TaskLogService taskLogService) {
        this.shardSize = shardSize;
        this.plannedRowsPerRun = plannedRowsPerRun;
        this.sqlBuilderService = sqlBuilderService;
        this.taskLogService = taskLogService;
    }

    @Override
    public ExecutorResult execute(TaskContext context) {
        List<ShardTask> shards = new ArrayList<>();
        int shardIndex = 1;
        for (int start = 0; start < context.selectedPartitions.size(); start += shardSize) {
            ShardTask shard = new ShardTask();
            shard.shardId = context.taskId + "-shard-" + shardIndex++;
            shard.partitionGroup = new ArrayList<>(context.selectedPartitions.subList(
                    start, Math.min(start + shardSize, context.selectedPartitions.size())));
            shard.plannedRowsPerRun = plannedRowsPerRun;
            shard.innerSql = sqlBuilderService.buildInnerSql(context, shard);
            shard.sqlPreview = shard.innerSql;
            shards.add(shard);
        }
        context.shards = shards;
        taskLogService.info(context.taskId, "SamplingExecutor created " + shards.size() + " shards");
        if (shards.isEmpty()) {
            return ExecutorResult.failure(name(), "Split partitions into shards", "no shards created");
        }
        return ExecutorResult.success(
                name(),
                "Split partitions into shards and build inner SQL",
                "shards=" + shards.size() + ", plannedRowsPerRun=" + plannedRowsPerRun);
    }
}
