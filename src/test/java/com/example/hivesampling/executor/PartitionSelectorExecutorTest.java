package com.example.hivesampling.executor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartitionSelectorExecutorTest {

    @Test
    void name_ShouldReturnCorrectName() {
        PartitionSelectorExecutor executor = new PartitionSelectorExecutor(null);
        assertEquals("PartitionSelectorExecutor", executor.name());
    }

    @Test
    void executor_ShouldBeNotNull() {
        PartitionSelectorExecutor executor = new PartitionSelectorExecutor(null);
        assertNotNull(executor);
    }
}
