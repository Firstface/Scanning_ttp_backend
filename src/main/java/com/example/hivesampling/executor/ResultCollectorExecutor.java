package com.example.hivesampling.executor;

import com.example.hivesampling.adapter.QueryResultServiceAdapter;
import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.ShardTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.SampleTaskExecutor;
import com.example.hivesampling.service.TaskLogStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Order(6)
@Component
public class ResultCollectorExecutor implements SampleTaskExecutor {

    private final TaskLogStore taskLogService;
    private final QueryResultServiceAdapter queryResultServiceAdapter;
    private final Random random = new Random();

    @Autowired
    public ResultCollectorExecutor(TaskLogStore taskLogService, QueryResultServiceAdapter queryResultServiceAdapter) {
        this.taskLogService = taskLogService;
        this.queryResultServiceAdapter = queryResultServiceAdapter;
    }

    public ResultCollectorExecutor(TaskLogStore taskLogService) {
        this(taskLogService, new com.example.hivesampling.adapter.MockQueryResultServiceAdapter());
    }

    @Override
    public ExecutorResult execute(TaskContext context) {
        randomDelay();
        
        // Simulate collecting results from shards
        List<ShardTask> shards = context.shards;
        long totalSampled = 0;
        int completedShards = 0;
        
        for (ShardTask shard : shards) {
            if (shard.status == ShardTaskStatus.SUCCESS) {
                totalSampled += queryResultServiceAdapter.collectRows(shard);
                completedShards++;
            }
        }
        
        // Update context with collected rows
        context.sampledRows = totalSampled;
        
        String summary = String.format("shards=%d/%d, totalSampled=%d", 
                completedShards, shards.size(), totalSampled);
        
        taskLogService.info(context.taskId, "Result collection complete: " + summary);
        
        return ExecutorResult.success(
                name(),
                "Collect and aggregate shard results",
                summary);
    }

    private void randomDelay() {
        try {
            Thread.sleep(500 + random.nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
