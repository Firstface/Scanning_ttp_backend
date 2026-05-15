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
    public PipelineState pipelineState;
    public Instant createdAt = Instant.now();
    public Instant updatedAt = Instant.now();

    public synchronized void changeStatus(ParentTaskStatus nextStatus) {
        this.status = nextStatus;
        this.updatedAt = Instant.now();
        if (this.pipelineState != null) {
            this.pipelineState.parentStatus = nextStatus;
        }
    }

    public synchronized void addSampledRows(long rows) {
        this.sampledRows += rows;
        this.updatedAt = Instant.now();
    }

    public synchronized void initializePipelineState() {
        this.pipelineState = PipelineState.initialize(this.taskId, this.status);
        this.pipelineResults.clear();
    }

    public synchronized ExecutorResult getCurrentExecutor(String executorName) {
        if (this.pipelineState == null) {
            return null;
        }
        for (ExecutorResult result : this.pipelineState.executors) {
            if (result.executorName.equals(executorName)) {
                return result;
            }
        }
        return null;
    }

    public synchronized void updateExecutorRunning(String executorName, String action, String outputSummary) {
        ExecutorResult result = getCurrentExecutor(executorName);
        if (result != null) {
            result.status = ExecutorStatus.RUNNING;
            result.success = null;
            result.action = action;
            result.outputSummary = outputSummary;
            result.startedAt = Instant.now();
            this.updatedAt = Instant.now();
        }
    }

    public synchronized void updateExecutorSuccess(String executorName, String action, String outputSummary) {
        ExecutorResult result = getCurrentExecutor(executorName);
        if (result != null) {
            result.status = ExecutorStatus.SUCCESS;
            result.success = true;
            result.action = action;
            result.outputSummary = outputSummary;
            result.executedAt = Instant.now();
            result.finishedAt = Instant.now();
            this.pipelineResults.add(result);
            this.updatedAt = Instant.now();
        }
    }

    public synchronized void updateExecutorFailure(String executorName, String action, String errorMessage) {
        ExecutorResult result = getCurrentExecutor(executorName);
        if (result != null) {
            result.status = ExecutorStatus.FAILED;
            result.success = false;
            result.action = action;
            result.errorMessage = errorMessage;
            result.executedAt = Instant.now();
            result.finishedAt = Instant.now();
            this.pipelineResults.add(result);
            this.updatedAt = Instant.now();
        }
    }
}
