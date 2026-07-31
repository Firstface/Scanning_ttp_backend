package com.example.hivesampling.executor;

import com.example.hivesampling.adapter.ExecutionSchedulerAdapter;
import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.ShardTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.SampleTaskExecutor;
import com.example.hivesampling.service.TaskLogStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Random;

@Order(5)
@Component
public class QueryDispatcherExecutor implements SampleTaskExecutor {

    private final TaskLogStore taskLogService;
    private final ExecutionSchedulerAdapter executionSchedulerAdapter;
    private final Random random = new Random();

    @Autowired
    public QueryDispatcherExecutor(TaskLogStore taskLogService, ExecutionSchedulerAdapter executionSchedulerAdapter) {
        this.taskLogService = taskLogService;
        this.executionSchedulerAdapter = executionSchedulerAdapter;
    }

    public QueryDispatcherExecutor(TaskLogStore taskLogService) {
        this(taskLogService, new com.example.hivesampling.adapter.MockExecutionSchedulerAdapter());
    }

    @Override
    public ExecutorResult execute(TaskContext context) {
        randomDelay();
        
        // Mark shards as queued
        for (ShardTask shard : context.shards) {
            shard.changeStatus(ShardTaskStatus.QUEUED);
        }
        
        taskLogService.info(context.taskId, "QueryDispatcherExecutor dispatched "
                + context.shards.size() + " shard tasks as QUEUED");
        
        // Run shards synchronously with simulated delays for demonstration
        for (ShardTask shard : context.shards) {
            runShardSynchronously(context, shard);
        }
        
        return ExecutorResult.success(
                name(),
                "Convert final SQLs into async shard tasks and dispatch them",
                "queuedShards=" + context.shards.size());
    }

    private void runShardSynchronously(TaskContext context, ShardTask shard) {
        shard.attemptCount++;
        shard.resultCollected = false;
        shard.changeStatus(ShardTaskStatus.RUNNING);
        taskLogService.info(context.taskId, shard.shardId + " RUNNING attempt=" + shard.attemptCount);
        try {
            Thread.sleep(random.nextInt(700, 1600));
            executionSchedulerAdapter.execute(context, shard);
            context.sampledRows += shard.lastRunRows;
            taskLogService.info(context.taskId, shard.shardId + " SUCCESS producedRows=" + shard.lastRunRows + " (total so far: " + context.sampledRows + ")");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            shard.changeStatus(ShardTaskStatus.FAILED);
            taskLogService.info(context.taskId, shard.shardId + " FAILED because worker was interrupted");
        }
    }


    private void randomDelay() {
        try {
            Thread.sleep(500 + random.nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
