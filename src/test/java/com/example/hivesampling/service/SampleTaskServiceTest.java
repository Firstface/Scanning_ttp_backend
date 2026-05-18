package com.example.hivesampling.service;

import com.example.hivesampling.dto.CreateSampleTaskRequest;
import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.PipelineRunner;
import com.example.hivesampling.repository.InMemoryTaskRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SampleTaskServiceTest {

    @Test
    void createAndStart_ShouldInitializeTaskAndRunPipelineAsync() throws Exception {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        TaskLogService taskLogService = new TaskLogService();
        PipelineRunner pipelineRunner = mock(PipelineRunner.class);
        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            TaskContext context = invocation.getArgument(0);
            context.changeStatus(ParentTaskStatus.SUCCESS);
            latch.countDown();
            return null;
        }).when(pipelineRunner).run(any(TaskContext.class));
        SampleTaskService service = new SampleTaskService(repository, pipelineRunner, taskLogService);

        CreateSampleTaskRequest request = new CreateSampleTaskRequest();
        request.databaseName = "demo_db";
        request.tableName = "sample_table";
        request.targetSampleRows = 2500;
        request.selectedPartitions = List.of("2026-05-01");

        TaskContext created = service.createAndStart(request);

        assertNotNull(created.taskId);
        assertEquals("demo_db", created.databaseName);
        assertEquals("sample_table", created.tableName);
        assertEquals(2500, created.targetSampleRows);
        assertNotNull(created.pipelineState);
        assertEquals(7, created.pipelineState.executors.size());
        assertTrue(latch.await(2, TimeUnit.SECONDS));
        verify(pipelineRunner).run(created);
        assertFalse(service.getLogs(created.taskId).isEmpty());
        assertEquals(1, service.getAllTasks().size());
    }

    @Test
    void getTask_ShouldThrowWhenTaskIsMissing() {
        SampleTaskService service = new SampleTaskService(new InMemoryTaskRepository(), mock(PipelineRunner.class), new TaskLogService());

        assertThrows(NoSuchElementException.class, () -> service.getTask("missing"));
    }
}
