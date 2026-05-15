package com.example.hivesampling.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LogEntryTest {

    @Test
    void testInfoLog() {
        LogEntry entry = LogEntry.info("test message");
        assertNotNull(entry.id);
        assertEquals("INFO", entry.level);
        assertEquals("test message", entry.message);
        assertNotNull(entry.timestamp);
    }

    @Test
    void testLogEntryEquality() {
        String id = UUID.randomUUID().toString();
        Instant ts = Instant.now();
        LogEntry entry1 = new LogEntry(id, ts, "INFO", "test");
        LogEntry entry2 = new LogEntry(id, ts, "INFO", "test");
        
        assertEquals(id, entry1.id);
        assertEquals(ts, entry1.timestamp);
        assertEquals(entry1.level, entry2.level);
    }

    @Test
    void testEmptyMessage() {
        LogEntry entry = new LogEntry("test-id", Instant.now(), "INFO", "");
        assertEquals("", entry.message);
    }

    @Test
    void testDifferentLevels() {
        LogEntry info = LogEntry.info("info");
        assertEquals("INFO", info.level);
        
        LogEntry custom = new LogEntry("id", Instant.now(), "WARN", "warning");
        assertEquals("WARN", custom.level);
    }

    @Test
    void testTimestampOrder() {
        LogEntry entry1 = LogEntry.info("first");
        try { Thread.sleep(1); } catch (Exception e) {}
        LogEntry entry2 = LogEntry.info("second");
        
        assertTrue(entry2.timestamp.isAfter(entry1.timestamp));
    }
}
