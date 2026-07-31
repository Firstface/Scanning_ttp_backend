package com.example.hivesampling.adapter;

import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.TaskContext;

public interface ExecutionSchedulerAdapter {
    void execute(TaskContext context, ShardTask shard);
}
