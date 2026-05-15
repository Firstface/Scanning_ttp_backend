package com.example.hivesampling.controller;

import com.example.hivesampling.dto.CreateSampleTaskRequest;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.SampleTaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SampleTaskController.class)
class SampleTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SampleTaskService sampleTaskService;

    private CreateSampleTaskRequest createRequest;
    private TaskContext taskContext;

    @BeforeEach
    void setUp() {
        createRequest = new CreateSampleTaskRequest();
        createRequest.setDatabaseName("demo_db");
        createRequest.setTableName("demo_table");
        createRequest.setTargetSampleRows(2500L);
        createRequest.setSelectedPartitions(List.of("2026-05-01", "2026-05-02"));

        taskContext = new TaskContext();
        taskContext.setTaskId("test-task-id-123");
        taskContext.setDatabaseName("demo_db");
        taskContext.setTableName("demo_table");
    }

    @Test
    void createTask_ShouldReturnCreatedStatus_WhenValidRequest() throws Exception {
        when(sampleTaskService.createAndStart(any(CreateSampleTaskRequest.class))).thenReturn(taskContext);

        mockMvc.perform(post("/api/sample-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").value("test-task-id-123"));

        verify(sampleTaskService, times(1)).createAndStart(any(CreateSampleTaskRequest.class));
    }

    @Test
    void getTask_ShouldReturnTask_WhenExists() throws Exception {
        when(sampleTaskService.getTask("test-task-id-123")).thenReturn(taskContext);

        mockMvc.perform(get("/api/sample-tasks/test-task-id-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value("test-task-id-123"));
    }

    @Test
    void listTasks_ShouldReturnTasks() throws Exception {
        when(sampleTaskService.getAllTasks()).thenReturn(List.of(taskContext));

        mockMvc.perform(get("/api/sample-tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].taskId").value("test-task-id-123"));
    }
}
