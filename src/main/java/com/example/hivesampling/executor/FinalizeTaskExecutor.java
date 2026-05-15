package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.SampleTaskExecutor;
import com.example.hivesampling.service.TaskLogService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Random;

@Order(7)
@Component
public class FinalizeTaskExecutor implements SampleTaskExecutor {

    private final TaskLogService taskLogService;
    private final Random random = new Random();

    public FinalizeTaskExecutor(TaskLogService taskLogService) {
        this.taskLogService = taskLogService;
    }

    @Override
    public ExecutorResult execute(TaskContext context) {
        randomDelay();
        
        // Make final decision on task status
        long sampled = context.sampledRows;
        long target = context.targetSampleRows;
        
        if (sampled >= target) {
            context.changeStatus(ParentTaskStatus.SUCCESS);
            String summary = String.format("sampledRows=%d >= targetSampleRows=%d", sampled, target);
            taskLogService.info(context.taskId, "Task finalized: SUCCESS - " + summary);
            
            return ExecutorResult.success(
                    name(),
                    "Finalize task status and complete",
                    summary);
        } else {
            // Failed to reach target
            context.changeStatus(ParentTaskStatus.FAILED);
            String errorMessage = String.format("sampledRows=%d < targetSampleRows=%d", sampled, target);
            taskLogService.info(context.taskId, "Task finalized: FAILED - " + errorMessage);
            
            return ExecutorResult.failure(
                    name(),
                    "Finalize task status - target not reached",
                    errorMessage);
        }
    }

    private void randomDelay() {
        try {
            Thread.sleep(500 + random.nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
