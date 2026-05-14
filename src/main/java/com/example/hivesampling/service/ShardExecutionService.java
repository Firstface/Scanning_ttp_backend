package com.example.hivesampling.service;

import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.ShardTaskStatus;
import com.example.hivesampling.model.TaskContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ShardExecutionService {

    private final ExecutorService workerPool;
    private final TaskLogService taskLogService;

    public ShardExecutionService(
            @Value("${sampling.worker-pool-size:4}") int workerPoolSize,
            TaskLogService taskLogService) {
        this.workerPool = Executors.newFixedThreadPool(workerPoolSize);
        this.taskLogService = taskLogService;
    }

    public void dispatchQueuedShards(TaskContext context) {
        context.changeStatus(ParentTaskStatus.RUNNING);
        context.shards.stream()
                .filter(shard -> shard.status == ShardTaskStatus.QUEUED)
                .forEach(shard -> workerPool.submit(() -> runShard(context, shard)));
    }

    private void runShard(TaskContext context, ShardTask shard) {
        synchronized (shard) {
            if (context.status != ParentTaskStatus.RUNNING || shard.status != ShardTaskStatus.QUEUED) {
                return;
            }
            shard.attemptCount++;
            shard.resultCollected = false;
            shard.changeStatus(ShardTaskStatus.RUNNING);
        }

        taskLogService.info(context.taskId, shard.shardId + " RUNNING attempt=" + shard.attemptCount);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(700, 1600));
            synchronized (shard) {
                if (context.status == ParentTaskStatus.SUCCESS || shard.status == ShardTaskStatus.CANCELLED) {
                    shard.changeStatus(ShardTaskStatus.CANCELLED);
                    return;
                }
                long producedRows = mockProducedRows(shard);
                shard.lastRunRows = producedRows;
                shard.sampledRows += producedRows;
                shard.offset += producedRows;
                shard.changeStatus(ShardTaskStatus.SUCCESS);
            }
            taskLogService.info(context.taskId, shard.shardId + " SUCCESS producedRows=" + shard.lastRunRows);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            shard.changeStatus(ShardTaskStatus.FAILED);
            taskLogService.info(context.taskId, shard.shardId + " FAILED because worker was interrupted");
        } catch (RuntimeException e) {
            shard.changeStatus(ShardTaskStatus.FAILED);
            taskLogService.info(context.taskId, shard.shardId + " FAILED message=" + e.getMessage());
        }
    }

    private long mockProducedRows(ShardTask shard) {
        int lowerBound = Math.max(1, shard.plannedRowsPerRun / 2);
        return ThreadLocalRandom.current().nextLong(lowerBound, shard.plannedRowsPerRun + 1L);
    }

    @PreDestroy
    public void shutdown() {
        workerPool.shutdownNow();
    }
}
