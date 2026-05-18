package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.ShardTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.TaskLogService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueryDispatcherExecutorTest {

    @Test
    void execute_ShouldRunQueuedShardSynchronously() {
        ShardTask shard = new ShardTask();
        shard.shardId = "task-1-shard-1";
        shard.finalSql = "SELECT 1";

        TaskContext context = new TaskContext();
        context.taskId = "task-1";
        context.shards = List.of(shard);
        QueryDispatcherExecutor executor = new QueryDispatcherExecutor(new TaskLogService());

        ExecutorResult result = executor.execute(context);

        assertEquals(ShardTaskStatus.SUCCESS, shard.status);
        assertEquals(1, shard.attemptCount);
        assertFalse(shard.resultCollected);
        assertTrue(shard.lastRunRows >= 900 && shard.lastRunRows < 1100);
        assertEquals(shard.lastRunRows, shard.sampledRows);
        assertEquals(shard.lastRunRows, context.sampledRows);
        assertEquals(shard.lastRunRows, shard.offset);
        assertTrue(result.outputSummary.contains("queuedShards=1"));
    }
}
