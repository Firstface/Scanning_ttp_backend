package com.example.hivesampling.executor;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.TaskLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RetrieveMetaInfosExecutorTest {

    @Mock
    private TaskLogService taskLogService;

    @InjectMocks
    private RetrieveMetaInfosExecutor executor;

    private TaskContext context;

    @BeforeEach
    void setUp() {
        context = new TaskContext();
        context.setDatabaseName("demo_db");
        context.setTableName("demo_table");
    }

    @Test
    void execute_ShouldSuccessfullyRetrieveMetaInfos() {
        ExecutorResult result = executor.execute(context);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(executor.name(), result.getExecutorName());
        assertNotNull(context.getMetadata());
        assertNotNull(context.getMetadata().getColumns());
        assertFalse(context.getMetadata().getColumns().isEmpty());
    }

    @Test
    void execute_ShouldReturnCorrectColumnCount() {
        executor.execute(context);

        assertEquals(5, context.getMetadata().getColumns().size());
    }

    @Test
    void name_ShouldReturnCorrectName() {
        assertEquals("RetrieveMetaInfosExecutor", executor.name());
    }
}
