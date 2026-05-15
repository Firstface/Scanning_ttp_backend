package com.example.hivesampling.executor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SamplingExecutorTest {

    @Test
    void name_ShouldReturnCorrectName() {
        SamplingExecutor executor = new SamplingExecutor(1000, 3, null, null);
        assertEquals("SamplingExecutor", executor.name());
    }

    @Test
    void executor_ShouldBeNotNull() {
        SamplingExecutor executor = new SamplingExecutor(1000, 3, null, null);
        assertNotNull(executor);
    }

    @Test
    void executor_WithDifferentParameters_ShouldWork() {
        SamplingExecutor executor = new SamplingExecutor(500, 5, null, null);
        assertNotNull(executor);
    }
}
