package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.TaskLogService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinalizeTaskExecutorTest {

    @Test
    void execute_ShouldMarkTaskSuccessWhenTargetReached() {
        TaskContext context = new TaskContext();
        context.taskId = "task-success";
        context.sampledRows = 3000;
        context.targetSampleRows = 2500;
        TaskLogService logService = new TaskLogService();
        FinalizeTaskExecutor executor = new FinalizeTaskExecutor(logService);

        ExecutorResult result = executor.execute(context);

        assertEquals(ParentTaskStatus.SUCCESS, context.status);
        assertTrue(result.success);
        assertTrue(result.outputSummary.contains("sampledRows=3000"));
        assertTrue(logService.list("task-success").stream().anyMatch(entry -> entry.message.contains("SUCCESS")));
    }

    @Test
    void execute_ShouldMarkTaskFailedWhenTargetMissed() {
        TaskContext context = new TaskContext();
        context.taskId = "task-failed";
        context.sampledRows = 1000;
        context.targetSampleRows = 2500;
        TaskLogService logService = new TaskLogService();
        FinalizeTaskExecutor executor = new FinalizeTaskExecutor(logService);

        ExecutorResult result = executor.execute(context);

        assertEquals(ParentTaskStatus.FAILED, context.status);
        assertFalse(result.success);
        assertTrue(result.errorMessage.contains("sampledRows=1000"));
        assertTrue(logService.list("task-failed").stream().anyMatch(entry -> entry.message.contains("FAILED")));
    }
}
