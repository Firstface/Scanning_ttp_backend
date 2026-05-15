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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PartitionSelectorExecutorTest {

    @Mock
    private TaskLogService taskLogService;

    @InjectMocks
    private PartitionSelectorExecutor executor;

    private TaskContext context;

    @BeforeEach
    void setUp() {
        context = new TaskContext();
    }

    @Test
    void execute_ShouldUseProvidedPartitions_WhenGiven() {
        List<String> expectedPartitions = List.of("p1", "p2", "p3");
        context.setSelectedPartitions(expectedPartitions);

        ExecutorResult result = executor.execute(context);

        assertTrue(result.isSuccess());
        assertEquals(expectedPartitions, context.getSelectedPartitions());
    }

    @Test
    void execute_ShouldUseMockPartitions_WhenNoneProvided() {
        ExecutorResult result = executor.execute(context);

        assertTrue(result.isSuccess());
        assertNotNull(context.getSelectedPartitions());
        assertFalse(context.getSelectedPartitions().isEmpty());
    }

    @Test
    void name_ShouldReturnCorrectName() {
        assertEquals("PartitionSelectorExecutor", executor.name());
    }
}
