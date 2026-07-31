package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.SqlBuilderService;
import com.example.hivesampling.service.TaskLogService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SamplingExecutorTest {

    @Test
    void execute_ShouldCreateThreeShardTasksAndInnerSql() {
        TaskContext context = new TaskContext();
        context.taskId = "task-1";
        context.databaseName = "demo_db";
        context.tableName = "sample_table";
        context.selectedPartitions = List.of("2026-05-01", "2026-05-02", "2026-05-03");
        TaskLogService logService = new TaskLogService();
        SamplingExecutor executor = new SamplingExecutor(5, 1000, new SqlBuilderService(), logService);

        ExecutorResult result = executor.execute(context);

        assertEquals(3, context.shards.size());
        assertEquals("SamplingExecutor", result.executorName);
        assertTrue(result.outputSummary.contains("shards=3"));
        assertTrue(context.shards.stream().allMatch(shard -> shard.innerSql.contains("FROM demo_db.sample_table")));
        assertTrue(logService.list("task-1").stream().anyMatch(entry -> entry.message.contains("created 3 shards")));
    }

    @Test
    void execute_ShouldNotCreateUnassignedShardsWhenFewerThanThreePartitionsAreSelected() {
        TaskContext context = new TaskContext();
        context.taskId = "task-2";
        context.databaseName = "demo_db";
        context.tableName = "sample_table";
        context.selectedPartitions = List.of("2026-05-01", "2026-05-02");
        TaskLogService logService = new TaskLogService();
        SamplingExecutor executor = new SamplingExecutor(5, 1000, new SqlBuilderService(), logService);

        executor.execute(context);

        assertEquals(2, context.shards.size());
        assertEquals(List.of("2026-05-01"), context.shards.get(0).partitionGroup);
        assertEquals(List.of("2026-05-02"), context.shards.get(1).partitionGroup);
    }
}
