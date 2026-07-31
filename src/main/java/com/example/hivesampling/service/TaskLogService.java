package com.example.hivesampling.service;

import com.example.hivesampling.model.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile({"default", "dev", "demo"})
public class TaskLogService implements TaskLogStore {

    private static final Logger log = LoggerFactory.getLogger(TaskLogService.class);

    private final Map<String, List<LogEntry>> taskLogs = new ConcurrentHashMap<>();

    public void info(String taskId, String message) {
        LogEntry entry = LogEntry.info(message);
        taskLogs.computeIfAbsent(taskId, ignored -> new ArrayList<>()).add(entry);
        log.info("[taskId={}] {}", taskId, message);
    }

    public List<LogEntry> list(String taskId) {
        return taskLogs.getOrDefault(taskId, List.of());
    }
}
