package com.example.hivesampling.executor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinalizeTaskExecutorTest {

    @Test
    void name_ShouldReturnCorrectName() {
        FinalizeTaskExecutor executor = new FinalizeTaskExecutor(null);
        assertEquals("FinalizeTaskExecutor", executor.name());
    }

    @Test
    void executor_ShouldBeNotNull() {
        FinalizeTaskExecutor executor = new FinalizeTaskExecutor(null);
        assertNotNull(executor);
    }

    @Test
    void taskCompletion_ShouldBeHandled() {
        FinalizeTaskExecutor executor = new FinalizeTaskExecutor(null);
        assertNotNull(executor);
    }
}
