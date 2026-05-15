package com.example.hivesampling.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogEntryTest {

    @Test
    void createInfoLog_ShouldWork() {
        LogEntry log = LogEntry.info("Test message");
        assertNotNull(log);
        assertNotNull(log.id);
        assertNotNull(log.timestamp);
        assertEquals("INFO", log.level);
        assertEquals("Test message", log.message);
    }

    @Test
    void logEntryShouldHaveIdField() {
        LogEntry log = new LogEntry();
        log.id = "test-id";
        assertEquals("test-id", log.id);
    }
}
