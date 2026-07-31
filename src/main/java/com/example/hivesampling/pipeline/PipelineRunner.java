package com.example.hivesampling.pipeline;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.repository.TaskStore;
import com.example.hivesampling.service.TaskLogStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class PipelineRunner {

    private final List<SampleTaskExecutor> executors;
    private final TaskLogStore taskLogService;
    private final TaskStore taskRepository;
    private final Random random = new Random();

    public PipelineRunner(List<SampleTaskExecutor> executors, TaskLogStore taskLogService, TaskStore taskRepository) {
        this.executors = executors;
        this.taskLogService = taskLogService;
        this.taskRepository = taskRepository;
    }

    public void run(TaskContext context) {
        context.initializePipelineState();
        context.changeStatus(ParentTaskStatus.PIPELINE_RUNNING);
        taskRepository.save(context);
        taskLogService.info(context.taskId, "PipelineRunner started");

        for (SampleTaskExecutor executor : executors) {
            String executorName = executor.name();
            
            context.updateExecutorRunning(executorName, "Executing...", "Processing...");
            taskRepository.save(context);
            taskLogService.info(context.taskId, executorName + " started");
            
            ExecutorResult result;
            try {
                result = executor.execute(context);
                context.updateExecutorSuccess(executorName, result.action, result.outputSummary);
                taskRepository.save(context);
                taskLogService.info(context.taskId, executorName + " finished");
            } catch (RuntimeException e) {
                context.updateExecutorFailure(executorName, "Executor threw an exception", e.getMessage());
                taskRepository.save(context);
                taskLogService.info(context.taskId, executorName + " failed: " + e.getMessage());
                context.changeStatus(ParentTaskStatus.FAILED);
                taskRepository.save(context);
                return;
            }
        }

        taskLogService.info(context.taskId, "PipelineRunner finished");
    }
}
