package com.example.hivesampling.service;

import com.example.hivesampling.model.LogEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskLogServiceTest {

    @Test
    void info_ShouldStoreLogEntryForTask() {
        TaskLogService taskLogService = new TaskLogService();

        taskLogService.info("task-1", "hello world");
        List<LogEntry> logs = taskLogService.list("task-1");

        assertEquals(1, logs.size());
        assertEquals("INFO", logs.get(0).level);
        assertEquals("hello world", logs.get(0).message);
    }

    @Test
    void list_ShouldReturnEmptyListForUnknownTask() {
        TaskLogService taskLogService = new TaskLogService();

        assertTrue(taskLogService.list("missing").isEmpty());
    }
}
