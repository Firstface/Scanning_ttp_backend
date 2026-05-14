package com.example.hivesampling.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskLogService {

    private static final Logger log = LoggerFactory.getLogger(TaskLogService.class);

    private final Map<String, List<String>> taskLogs = new ConcurrentHashMap<>();

    public void info(String taskId, String message) {
        String line = Instant.now() + " | " + message;
        taskLogs.computeIfAbsent(taskId, ignored -> new ArrayList<>()).add(line);
        log.info("[taskId={}] {}", taskId, message);
    }

    public List<String> list(String taskId) {
        return taskLogs.getOrDefault(taskId, List.of());
    }
}
