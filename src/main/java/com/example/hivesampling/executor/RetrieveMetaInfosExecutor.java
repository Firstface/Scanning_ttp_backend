package com.example.hivesampling.executor;

import com.example.hivesampling.adapter.MetadataServiceAdapter;
import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.TableMetadata;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.SampleTaskExecutor;
import com.example.hivesampling.service.TaskLogStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Order(1)
@Component
public class RetrieveMetaInfosExecutor implements SampleTaskExecutor {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final TaskLogStore taskLogService;
    private final MetadataServiceAdapter metadataServiceAdapter;

    @Autowired
    public RetrieveMetaInfosExecutor(TaskLogStore taskLogService, MetadataServiceAdapter metadataServiceAdapter) {
        this.taskLogService = taskLogService;
        this.metadataServiceAdapter = metadataServiceAdapter;
    }

    public RetrieveMetaInfosExecutor(TaskLogStore taskLogService) {
        this(taskLogService, new com.example.hivesampling.adapter.MockMetadataServiceAdapter());
    }

    @Override
    public ExecutorResult execute(TaskContext context) {
        randomDelay();
        TableMetadata metadata = metadataServiceAdapter.fetch(context.databaseName, context.tableName);
        context.metadata = metadata;
        taskLogService.info(context.taskId, "RetrieveMetaInfosExecutor loaded 5 columns and 1 partition column");
        return ExecutorResult.success(
                name(),
                "Mock table metadata and schema",
                "columns=5, partitionColumns=1, tableType=" + metadata.tableType);
    }

    private void randomDelay() {
        try {
            Thread.sleep(500L + SECURE_RANDOM.nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
