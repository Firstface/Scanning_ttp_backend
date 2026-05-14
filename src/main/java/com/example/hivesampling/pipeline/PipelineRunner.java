package com.example.hivesampling.pipeline;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.TaskLogService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PipelineRunner {

    private final List<SampleTaskExecutor> executors;
    private final TaskLogService taskLogService;

    public PipelineRunner(List<SampleTaskExecutor> executors, TaskLogService taskLogService) {
        this.executors = executors;
        this.taskLogService = taskLogService;
    }

    public void run(TaskContext context) {
        context.changeStatus(ParentTaskStatus.PIPELINE_RUNNING);
        context.pipelineResults.clear();
        taskLogService.info(context.taskId, "PipelineRunner started");

        for (SampleTaskExecutor executor : executors) {
            taskLogService.info(context.taskId, executor.name() + " started");
            ExecutorResult result;
            try {
                result = executor.execute(context);
            } catch (RuntimeException e) {
                result = ExecutorResult.failure(executor.name(), "Executor threw an exception", e.getMessage());
            }

            context.pipelineResults.add(result);
            taskLogService.info(context.taskId, executor.name() + (result.success ? " finished" : " failed"));
            if (!result.success) {
                context.changeStatus(ParentTaskStatus.FAILED);
                return;
            }
        }

        taskLogService.info(context.taskId, "PipelineRunner finished");
    }
}
