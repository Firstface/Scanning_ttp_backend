package com.example.hivesampling.model;

import java.time.Instant;

public class ExecutorResult {

    public String executorName;
    public boolean success;
    public String action;
    public String outputSummary;
    public Instant executedAt = Instant.now();

    public ExecutorResult() {
    }

    public ExecutorResult(String executorName, boolean success, String action, String outputSummary) {
        this.executorName = executorName;
        this.success = success;
        this.action = action;
        this.outputSummary = outputSummary;
    }

    public static ExecutorResult success(String executorName, String action, String outputSummary) {
        return new ExecutorResult(executorName, true, action, outputSummary);
    }

    public static ExecutorResult failure(String executorName, String action, String outputSummary) {
        return new ExecutorResult(executorName, false, action, outputSummary);
    }
}
