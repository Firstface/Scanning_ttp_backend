package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.ShardTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.SampleTaskExecutor;
import com.example.hivesampling.service.ShardExecutionService;
import com.example.hivesampling.service.TaskLogService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(5)
@Component
public class QueryDispatcherExecutor implements SampleTaskExecutor {

    private final ShardExecutionService shardExecutionService;
    private final TaskLogService taskLogService;

    public QueryDispatcherExecutor(ShardExecutionService shardExecutionService, TaskLogService taskLogService) {
        this.shardExecutionService = shardExecutionService;
        this.taskLogService = taskLogService;
    }

    @Override
    public ExecutorResult execute(TaskContext context) {
        for (ShardTask shard : context.shards) {
            shard.changeStatus(ShardTaskStatus.QUEUED);
        }
        context.changeStatus(ParentTaskStatus.DISPATCHED);
        taskLogService.info(context.taskId, "QueryDispatcherExecutor dispatched "
                + context.shards.size() + " shard tasks as QUEUED");
        shardExecutionService.dispatchQueuedShards(context);
        return ExecutorResult.success(
                name(),
                "Convert final SQLs into async shard tasks and dispatch them",
                "queuedShards=" + context.shards.size() + ", parentStatus=" + context.status);
    }
}
