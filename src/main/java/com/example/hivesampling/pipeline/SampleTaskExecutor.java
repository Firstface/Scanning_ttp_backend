package com.example.hivesampling.pipeline;

import com.example.hivesampling.model.ExecutorResult;
import com.example.hivesampling.model.TaskContext;

public interface SampleTaskExecutor {

    ExecutorResult execute(TaskContext context);

    default String name() {
        return getClass().getSimpleName();
    }
}
