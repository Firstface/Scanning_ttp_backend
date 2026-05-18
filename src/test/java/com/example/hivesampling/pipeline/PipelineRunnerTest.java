package com.example.hivesampling.pipeline;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ExecutorStatus;
import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.repository.InMemoryTaskRepository;
import com.example.hivesampling.service.TaskLogService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PipelineRunnerTest {

    @Test
    void run_ShouldExecutePipelineAndRecordSuccesses() {
        TaskContext context = new TaskContext();
        context.taskId = "task-success";
        TaskLogService taskLogService = new TaskLogService();
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        PipelineRunner runner = new PipelineRunner(
                List.of(
                        new SuccessfulExecutor("RetrieveMetaInfosExecutor"),
                        new SuccessfulExecutor("PartitionSelectorExecutor")),
                taskLogService,
                repository);

        runner.run(context);

        assertEquals(ParentTaskStatus.PIPELINE_RUNNING, context.status);
        assertEquals(2, context.pipelineResults.size());
        assertEquals(ExecutorStatus.SUCCESS, context.getCurrentExecutor("RetrieveMetaInfosExecutor").status);
        assertEquals(ExecutorStatus.SUCCESS, context.getCurrentExecutor("PartitionSelectorExecutor").status);
        assertTrue(taskLogService.list("task-success").stream().anyMatch(entry -> entry.message.contains("PipelineRunner finished")));
        assertTrue(repository.findById("task-success").isPresent());
    }

    @Test
    void run_ShouldStopPipelineWhenExecutorFails() {
        TaskContext context = new TaskContext();
        context.taskId = "task-fail";
        TaskLogService taskLogService = new TaskLogService();
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        PipelineRunner runner = new PipelineRunner(
                List.of(
                        new FailingExecutor("RetrieveMetaInfosExecutor"),
                        new SuccessfulExecutor("PartitionSelectorExecutor")),
                taskLogService,
                repository);

        runner.run(context);

        assertEquals(ParentTaskStatus.FAILED, context.status);
        assertEquals(1, context.pipelineResults.size());
        assertEquals(ExecutorStatus.FAILED, context.getCurrentExecutor("RetrieveMetaInfosExecutor").status);
        assertEquals(ExecutorStatus.PENDING, context.getCurrentExecutor("PartitionSelectorExecutor").status);
        assertTrue(taskLogService.list("task-fail").stream().anyMatch(entry -> entry.message.contains("failed: boom")));
    }

    private static final class SuccessfulExecutor implements SampleTaskExecutor {
        private final String executorName;

        private SuccessfulExecutor(String executorName) {
            this.executorName = executorName;
        }

        @Override
        public ExecutorResult execute(TaskContext context) {
            return ExecutorResult.success(executorName, "done", "ok");
        }

        @Override
        public String name() {
            return executorName;
        }
    }

    private static final class FailingExecutor implements SampleTaskExecutor {
        private final String executorName;

        private FailingExecutor(String executorName) {
            this.executorName = executorName;
        }

        @Override
        public ExecutorResult execute(TaskContext context) {
            throw new RuntimeException("boom");
        }

        @Override
        public String name() {
            return executorName;
        }
    }
}
