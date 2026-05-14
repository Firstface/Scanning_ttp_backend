package com.example.hivesampling.service;

import com.example.hivesampling.dto.CreateSampleTaskRequest;
import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.pipeline.PipelineRunner;
import com.example.hivesampling.repository.InMemoryTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class SampleTaskService {

    private final InMemoryTaskRepository taskRepository;
    private final PipelineRunner pipelineRunner;
    private final TaskLogService taskLogService;

    public SampleTaskService(InMemoryTaskRepository taskRepository,
                             PipelineRunner pipelineRunner,
                             TaskLogService taskLogService) {
        this.taskRepository = taskRepository;
        this.pipelineRunner = pipelineRunner;
        this.taskLogService = taskLogService;
    }

    public TaskContext createAndStart(CreateSampleTaskRequest request) {
        TaskContext context = new TaskContext();
        context.taskId = UUID.randomUUID().toString();
        context.databaseName = request.databaseName;
        context.tableName = request.tableName;
        context.targetSampleRows = request.targetSampleRows;
        context.selectedPartitions = request.selectedPartitions;
        context.changeStatus(ParentTaskStatus.CREATED);
        taskRepository.save(context);

        taskLogService.info(context.taskId, "Parent task created");
        pipelineRunner.run(context);
        taskRepository.save(context);
        return context;
    }

    public TaskContext getTask(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + taskId));
    }

    public List<ShardTask> getShards(String taskId) {
        return getTask(taskId).shards;
    }

    public List<String> getLogs(String taskId) {
        getTask(taskId);
        return taskLogService.list(taskId);
    }

    public List<TaskContext> getAllTasks() {
        return taskRepository.findAll().stream().toList();
    }
}
