package com.example.hivesampling.controller;

import com.example.hivesampling.dto.CreateSampleTaskRequest;
import com.example.hivesampling.model.LogEntry;
import com.example.hivesampling.model.PipelineState;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.service.SampleTaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/sample-tasks")
public class SampleTaskController {

    private final SampleTaskService sampleTaskService;

    public SampleTaskController(SampleTaskService sampleTaskService) {
        this.sampleTaskService = sampleTaskService;
    }

    @GetMapping
    public List<TaskContext> list() {
        return sampleTaskService.getAllTasks();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskContext create(@Valid @RequestBody CreateSampleTaskRequest request) {
        return sampleTaskService.createAndStart(request);
    }

    @GetMapping("/{taskId}")
    public TaskContext getTask(@PathVariable String taskId) {
        return findTask(taskId);
    }

    @GetMapping("/{taskId}/shards")
    public List<ShardTask> getShards(@PathVariable String taskId) {
        try {
            return sampleTaskService.getShards(taskId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{taskId}/pipeline")
    public PipelineState getPipeline(@PathVariable String taskId) {
        TaskContext task = findTask(taskId);
        if (task.pipelineState != null) {
            return task.pipelineState;
        }
        return PipelineState.initialize(task.taskId, task.status);
    }

    @GetMapping("/{taskId}/logs")
    public List<LogEntry> getLogs(@PathVariable String taskId) {
        try {
            return sampleTaskService.getLogs(taskId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    private TaskContext findTask(String taskId) {
        try {
            return sampleTaskService.getTask(taskId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
