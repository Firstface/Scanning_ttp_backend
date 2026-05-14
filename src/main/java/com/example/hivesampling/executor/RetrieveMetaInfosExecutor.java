package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.TableMetadata;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.SampleTaskExecutor;
import com.example.hivesampling.service.TaskLogService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(1)
@Component
public class RetrieveMetaInfosExecutor implements SampleTaskExecutor {

    private final TaskLogService taskLogService;

    public RetrieveMetaInfosExecutor(TaskLogService taskLogService) {
        this.taskLogService = taskLogService;
    }

    @Override
    public ExecutorResult execute(TaskContext context) {
        TableMetadata metadata = new TableMetadata();
        metadata.columns = List.of("user_id", "event_name", "event_time", "device_type", "dt");
        metadata.partitionColumns = List.of("dt");
        metadata.tableType = "MANAGED_TABLE";
        context.metadata = metadata;
        taskLogService.info(context.taskId, "RetrieveMetaInfosExecutor loaded 5 columns and 1 partition column");
        return ExecutorResult.success(
                name(),
                "Mock table metadata and schema",
                "columns=5, partitionColumns=1, tableType=" + metadata.tableType);
    }
}
