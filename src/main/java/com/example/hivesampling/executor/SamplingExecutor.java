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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Order(3)
@Component
public class SamplingExecutor implements SampleTaskExecutor {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

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
        randomDelay();
        List<ShardTask> shards = new ArrayList<>();
        int numShards = 3; // Create 3 shards for demonstration
        
        for (int i = 1; i <= numShards; i++) {
            ShardTask shard = new ShardTask();
            shard.shardId = context.taskId + "-shard-" + i;
            // Assign 1 partition per shard
            int startIdx = (i - 1) * 1;
            int endIdx = Math.min(startIdx + 1, context.selectedPartitions.size());
            shard.partitionGroup = new ArrayList<>(context.selectedPartitions.subList(startIdx, endIdx));
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

    private void randomDelay() {
        try {
            Thread.sleep(500L + SECURE_RANDOM.nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
