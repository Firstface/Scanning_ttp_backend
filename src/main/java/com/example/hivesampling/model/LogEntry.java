package com.example.hivesampling.model;

import java.time.Instant;

public class LogEntry {

    public Instant timestamp;
    public String level;
    public String message;

    public LogEntry() {
    }

    public LogEntry(Instant timestamp, String level, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
    }

    public static LogEntry info(String message) {
        return new LogEntry(Instant.now(), "INFO", message);
    }
}
