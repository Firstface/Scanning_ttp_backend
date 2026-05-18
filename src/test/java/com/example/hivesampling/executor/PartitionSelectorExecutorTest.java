package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.TaskLogService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartitionSelectorExecutorTest {

    @Test
    void execute_ShouldPopulateDefaultPartitionsWhenMissing() {
        TaskContext context = new TaskContext();
        context.taskId = "task-1";
        context.selectedPartitions = new ArrayList<>();
        PartitionSelectorExecutor executor = new PartitionSelectorExecutor(new TaskLogService());

        ExecutorResult result = executor.execute(context);

        assertEquals(12, context.selectedPartitions.size());
        assertEquals("PartitionSelectorExecutor", result.executorName);
        assertTrue(result.outputSummary.contains("selectedPartitions=12"));
    }

    @Test
    void execute_ShouldKeepExistingPartitions() {
        TaskContext context = new TaskContext();
        context.taskId = "task-2";
        context.selectedPartitions = new ArrayList<>(List.of("2026-06-01"));
        PartitionSelectorExecutor executor = new PartitionSelectorExecutor(new TaskLogService());

        executor.execute(context);

        assertEquals(List.of("2026-06-01"), context.selectedPartitions);
    }
}
