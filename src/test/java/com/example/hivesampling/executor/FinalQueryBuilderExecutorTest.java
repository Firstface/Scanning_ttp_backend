package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.SqlBuilderService;
import com.example.hivesampling.service.TaskLogService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinalQueryBuilderExecutorTest {

    @Test
    void execute_ShouldWrapEveryInnerSql() {
        TaskContext context = new TaskContext();
        context.taskId = "task-1";
        ShardTask first = new ShardTask();
        first.innerSql = "SELECT 1";
        ShardTask second = new ShardTask();
        second.innerSql = "SELECT 2";
        context.shards = List.of(first, second);
        FinalQueryBuilderExecutor executor = new FinalQueryBuilderExecutor(new SqlBuilderService(), new TaskLogService());

        ExecutorResult result = executor.execute(context);

        assertEquals(2, context.finalSqls.size());
        assertTrue(first.finalSql.contains("WITH table_rows AS"));
        assertTrue(second.finalSql.contains("INSERT INTO mock_result_table"));
        assertTrue(result.outputSummary.contains("finalSqls=2"));
    }
}
