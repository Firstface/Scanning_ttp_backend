package com.example.hivesampling.model;

import java.time.Instant;

public class ExecutorResult {

    public String executorName;
    public ExecutorStatus status;
    public Boolean success;
    public String action;
    public String outputSummary;
    public Instant executedAt;
    public Instant startedAt;
    public Instant finishedAt;
    public String errorMessage;

    public ExecutorResult() {
    }

    public ExecutorResult(String executorName, ExecutorStatus status, Boolean success, String action, String outputSummary, String errorMessage) {
        this.executorName = executorName;
        this.status = status;
        this.success = success;
        this.action = action;
        this.outputSummary = outputSummary;
        this.errorMessage = errorMessage;
    }

    public static ExecutorResult pending(String executorName) {
        ExecutorResult result = new ExecutorResult();
        result.executorName = executorName;
        result.status = ExecutorStatus.PENDING;
        result.success = null;
        result.action = "";
        result.outputSummary = "";
        result.errorMessage = "";
        return result;
    }

    public static ExecutorResult running(String executorName, String action, String outputSummary) {
        ExecutorResult result = new ExecutorResult();
        result.executorName = executorName;
        result.status = ExecutorStatus.RUNNING;
        result.success = null;
        result.action = action;
        result.outputSummary = outputSummary;
        result.startedAt = Instant.now();
        result.errorMessage = "";
        return result;
    }

    public static ExecutorResult success(String executorName, String action, String outputSummary) {
        ExecutorResult result = new ExecutorResult();
        result.executorName = executorName;
        result.status = ExecutorStatus.SUCCESS;
        result.success = true;
        result.action = action;
        result.outputSummary = outputSummary;
        result.executedAt = Instant.now();
        result.finishedAt = Instant.now();
        result.errorMessage = "";
        return result;
    }

    public static ExecutorResult failure(String executorName, String action, String errorMessage) {
        ExecutorResult result = new ExecutorResult();
        result.executorName = executorName;
        result.status = ExecutorStatus.FAILED;
        result.success = false;
        result.action = action;
        result.outputSummary = "";
        result.executedAt = Instant.now();
        result.finishedAt = Instant.now();
        result.errorMessage = errorMessage;
        return result;
    }

    public void markSuccess(String action, String outputSummary) {
        this.status = ExecutorStatus.SUCCESS;
        this.success = true;
        this.action = action;
        this.outputSummary = outputSummary;
        this.executedAt = Instant.now();
        this.finishedAt = Instant.now();
    }

    public void markFailure(String action, String errorMessage) {
        this.status = ExecutorStatus.FAILED;
        this.success = false;
        this.action = action;
        this.errorMessage = errorMessage;
        this.executedAt = Instant.now();
        this.finishedAt = Instant.now();
    }
}
