package com.example.hivesampling.model;

import java.time.Instant;
import java.util.UUID;

public class LogEntry {

    public String id;
    public Instant timestamp;
    public String level;
    public String message;

    public LogEntry() {
    }

    public LogEntry(String id, Instant timestamp, String level, String message) {
        this.id = id;
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
    }

    public static LogEntry info(String message) {
        return new LogEntry(UUID.randomUUID().toString(), Instant.now(), "INFO", message);
    }
}
