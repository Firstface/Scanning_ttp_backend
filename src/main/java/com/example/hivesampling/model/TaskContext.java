package com.example.hivesampling.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TaskContext {

    public String taskId;
    public String databaseName;
    public String tableName;
    public long targetSampleRows;
    public long sampledRows;
    public TableMetadata metadata;
    public List<String> selectedPartitions = new ArrayList<>();
    public List<ShardTask> shards = new ArrayList<>();
    public List<String> finalSqls = new ArrayList<>();
    public ParentTaskStatus status = ParentTaskStatus.CREATED;
    public List<ExecutorResult> pipelineResults = new ArrayList<>();
    public Instant createdAt = Instant.now();
    public Instant updatedAt = Instant.now();

    public synchronized void changeStatus(ParentTaskStatus nextStatus) {
        this.status = nextStatus;
        this.updatedAt = Instant.now();
    }

    public synchronized void addSampledRows(long rows) {
        this.sampledRows += rows;
        this.updatedAt = Instant.now();
    }
}
