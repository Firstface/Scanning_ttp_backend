package com.example.hivesampling.repository;

import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.TaskContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTaskRepository {

    private final Map<String, TaskContext> tasks = new ConcurrentHashMap<>();

    public void save(TaskContext context) {
        tasks.put(context.taskId, context);
    }

    public Optional<TaskContext> findById(String taskId) {
        return Optional.ofNullable(tasks.get(taskId));
    }

    public Collection<TaskContext> findAll() {
        return tasks.values();
    }

    public Collection<TaskContext> findActiveTasks() {
        return tasks.values().stream()
                .filter(task -> task.status == ParentTaskStatus.DISPATCHED || task.status == ParentTaskStatus.RUNNING)
                .toList();
    }
}
