package com.example.hivesampling.model;

import java.util.ArrayList;
import java.util.List;

public class PipelineState {

    public String taskId;
    public ParentTaskStatus parentStatus;
    public List<ExecutorResult> executors;

    public PipelineState() {
    }

    public PipelineState(String taskId, ParentTaskStatus parentStatus, List<ExecutorResult> executors) {
        this.taskId = taskId;
        this.parentStatus = parentStatus;
        this.executors = executors;
    }

    public static List<String> getFixedExecutorNames() {
        return List.of(
                "RetrieveMetaInfosExecutor",
                "PartitionSelectorExecutor",
                "SamplingExecutor",
                "FinalQueryBuilderExecutor",
                "QueryDispatcherExecutor",
                "ResultCollectorExecutor",
                "FinalizeTaskExecutor"
        );
    }

    public static PipelineState initialize(String taskId, ParentTaskStatus parentStatus) {
        List<ExecutorResult> executors = new ArrayList<>();
        for (String name : getFixedExecutorNames()) {
            executors.add(ExecutorResult.pending(name));
        }
        return new PipelineState(taskId, parentStatus, executors);
    }
}
