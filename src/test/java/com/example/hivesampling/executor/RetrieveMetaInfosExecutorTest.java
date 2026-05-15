package com.example.hivesampling.executor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetrieveMetaInfosExecutorTest {

    @Test
    void name_ShouldReturnCorrectName() {
        RetrieveMetaInfosExecutor executor = new RetrieveMetaInfosExecutor(null);
        assertEquals("RetrieveMetaInfosExecutor", executor.name());
    }

    @Test
    void executor_ShouldBeNotNull() {
        RetrieveMetaInfosExecutor executor = new RetrieveMetaInfosExecutor(null);
        assertNotNull(executor);
    }
}
