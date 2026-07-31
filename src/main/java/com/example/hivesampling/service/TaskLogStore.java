package com.example.hivesampling.service;

import com.example.hivesampling.model.LogEntry;

import java.util.List;

public interface TaskLogStore {
    void info(String taskId, String message);
    List<LogEntry> list(String taskId);
}
