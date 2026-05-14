package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.SampleTaskExecutor;
import com.example.hivesampling.service.TaskLogService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(2)
@Component
public class PartitionSelectorExecutor implements SampleTaskExecutor {

    private final TaskLogService taskLogService;

    public PartitionSelectorExecutor(TaskLogService taskLogService) {
        this.taskLogService = taskLogService;
    }

    @Override
    public ExecutorResult execute(TaskContext context) {
        if (context.selectedPartitions == null || context.selectedPartitions.isEmpty()) {
            context.selectedPartitions = List.of(
                    "2026-05-01", "2026-05-02", "2026-05-03", "2026-05-04", "2026-05-05",
                    "2026-05-06", "2026-05-07", "2026-05-08", "2026-05-09", "2026-05-10",
                    "2026-05-11", "2026-05-12");
        }
        taskLogService.info(context.taskId, "PartitionSelectorExecutor selected "
                + context.selectedPartitions.size() + " partitions");
        return ExecutorResult.success(
                name(),
                "Select partitions for sampling",
                "selectedPartitions=" + context.selectedPartitions.size());
    }
}
