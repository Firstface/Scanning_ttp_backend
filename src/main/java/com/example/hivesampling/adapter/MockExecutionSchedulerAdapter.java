package com.example.hivesampling.adapter;

import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.ShardTaskStatus;
import com.example.hivesampling.model.TaskContext;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MockExecutionSchedulerAdapter implements ExecutionSchedulerAdapter {
    @Override
    public void execute(TaskContext context, ShardTask shard) {
        shard.lastRunRows = ThreadLocalRandom.current().nextLong(900, 1100);
        shard.sampledRows += shard.lastRunRows;
        shard.offset += shard.lastRunRows;
        shard.changeStatus(ShardTaskStatus.SUCCESS);
    }
}
