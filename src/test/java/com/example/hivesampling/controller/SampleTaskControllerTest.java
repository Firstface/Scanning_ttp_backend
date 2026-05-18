package com.example.hivesampling.controller;

import com.example.hivesampling.dto.CreateSampleTaskRequest;
import com.example.hivesampling.model.PipelineState;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.SampleTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SampleTaskControllerTest {

    private SampleTaskService sampleTaskService;
    private SampleTaskController controller;

    @BeforeEach
    void setUp() {
        sampleTaskService = mock(SampleTaskService.class);
        controller = new SampleTaskController(sampleTaskService);
    }

    @Test
    void create_ShouldDelegateToService() {
        CreateSampleTaskRequest request = new CreateSampleTaskRequest();
        TaskContext context = new TaskContext();
        when(sampleTaskService.createAndStart(request)).thenReturn(context);

        TaskContext result = controller.create(request);

        assertSame(context, result);
        verify(sampleTaskService).createAndStart(request);
    }

    @Test
    void list_ShouldReturnAllTasks() {
        TaskContext first = new TaskContext();
        TaskContext second = new TaskContext();
        when(sampleTaskService.getAllTasks()).thenReturn(List.of(first, second));

        List<TaskContext> result = controller.list();

        assertEquals(2, result.size());
    }

    @Test
    void getPipeline_ShouldReturnExistingPipelineState() {
        TaskContext context = new TaskContext();
        context.taskId = "task-1";
        context.initializePipelineState();
        when(sampleTaskService.getTask("task-1")).thenReturn(context);

        PipelineState result = controller.getPipeline("task-1");

        assertSame(context.pipelineState, result);
    }

    @Test
    void getPipeline_ShouldCreateDefaultPipelineWhenMissing() {
        TaskContext context = new TaskContext();
        context.taskId = "task-2";
        when(sampleTaskService.getTask("task-2")).thenReturn(context);

        PipelineState result = controller.getPipeline("task-2");

        assertEquals("task-2", result.taskId);
        assertEquals(7, result.executors.size());
    }

    @Test
    void getShards_ShouldTranslateMissingTaskToNotFound() {
        when(sampleTaskService.getShards("missing")).thenThrow(new NoSuchElementException("missing"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.getShards("missing"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void getLogs_ShouldTranslateMissingTaskToNotFound() {
        when(sampleTaskService.getLogs("missing")).thenThrow(new NoSuchElementException("missing"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.getLogs("missing"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void getTask_ShouldTranslateMissingTaskToNotFound() {
        when(sampleTaskService.getTask("missing")).thenThrow(new NoSuchElementException("missing"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.getTask("missing"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
