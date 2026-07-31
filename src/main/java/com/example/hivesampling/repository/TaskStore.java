package com.example.hivesampling.repository;

import com.example.hivesampling.model.TaskContext;

import java.util.Collection;
import java.util.Optional;

/** Storage boundary used by the executor pipeline. Implementations are profile-selected. */
public interface TaskStore {
    void save(TaskContext context);
    Optional<TaskContext> findById(String taskId);
    Collection<TaskContext> findAll();
    Collection<TaskContext> findActiveTasks();
}
