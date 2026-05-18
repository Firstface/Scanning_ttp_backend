package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.TaskLogService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetrieveMetaInfosExecutorTest {

    @Test
    void execute_ShouldFillMetadata() {
        TaskContext context = new TaskContext();
        context.taskId = "task-1";
        RetrieveMetaInfosExecutor executor = new RetrieveMetaInfosExecutor(new TaskLogService());

        ExecutorResult result = executor.execute(context);

        assertNotNull(context.metadata);
        assertEquals(5, context.metadata.columns.size());
        assertEquals("MANAGED_TABLE", context.metadata.tableType);
        assertEquals("RetrieveMetaInfosExecutor", result.executorName);
        assertTrue(result.outputSummary.contains("columns=5"));
    }
}
