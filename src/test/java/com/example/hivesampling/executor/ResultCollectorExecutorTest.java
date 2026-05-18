package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.ShardTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.TaskLogService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultCollectorExecutorTest {

    @Test
    void execute_ShouldAggregateOnlySuccessfulShards() {
        ShardTask successOne = new ShardTask();
        successOne.status = ShardTaskStatus.SUCCESS;
        successOne.sampledRows = 1200;
        ShardTask successTwo = new ShardTask();
        successTwo.status = ShardTaskStatus.SUCCESS;
        successTwo.sampledRows = 800;
        ShardTask failed = new ShardTask();
        failed.status = ShardTaskStatus.FAILED;
        failed.sampledRows = 500;

        TaskContext context = new TaskContext();
        context.taskId = "task-1";
        context.shards = List.of(successOne, successTwo, failed);
        ResultCollectorExecutor executor = new ResultCollectorExecutor(new TaskLogService());

        ExecutorResult result = executor.execute(context);

        assertEquals(2000, context.sampledRows);
        assertTrue(result.outputSummary.contains("shards=2/3"));
        assertTrue(result.outputSummary.contains("totalSampled=2000"));
    }
}
