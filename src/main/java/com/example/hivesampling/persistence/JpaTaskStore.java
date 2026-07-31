package com.example.hivesampling.persistence;

import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.ShardTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.persistence.entity.*;
import com.example.hivesampling.persistence.repository.TaskRunJpaRepository;
import com.example.hivesampling.persistence.repository.ValidationTaskJpaRepository;
import com.example.hivesampling.repository.TaskStore;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Profile({"persistence", "staging", "production-like"})
public class JpaTaskStore implements TaskStore {
    private final ValidationTaskJpaRepository validationTasks;
    private final TaskRunJpaRepository taskRuns;

    public JpaTaskStore(ValidationTaskJpaRepository validationTasks, TaskRunJpaRepository taskRuns) {
        this.validationTasks = validationTasks;
        this.taskRuns = taskRuns;
    }

    @Override
    @Transactional
    public void save(TaskContext context) {
        TaskRunEntity run = taskRuns.findByRunId(context.taskId).orElseGet(TaskRunEntity::new);
        ValidationTaskEntity task = run.validationTask;
        if (task == null) {
            if (context.databaseName == null || context.tableName == null) {
                throw new IllegalArgumentException("databaseName and tableName are required for a new task run");
            }
            String validationTaskKey = context.databaseName + "." + context.tableName;
            task = validationTasks.findByExternalKey(validationTaskKey).orElseGet(ValidationTaskEntity::new);
            task.externalKey = validationTaskKey;
            task.databaseName = context.databaseName;
            task.tableName = context.tableName;
            task.status = context.status.name();
            task = validationTasks.save(task);
            run.validationTask = task;
        } else {
            task = validationTasks.findById(task.id)
                    .orElseThrow(() -> new IllegalStateException("validation task not found for run " + context.taskId));
            context.databaseName = task.databaseName;
            context.tableName = task.tableName;
        }
        run.runId = context.taskId;
        run.status = context.status.name();
        run.targetSampleRows = context.targetSampleRows;
        run.sampledRows = context.sampledRows;
        run.finalSql = String.join("\n", context.finalSqls);
        if (!task.taskRuns.contains(run)) {
            task.taskRuns.add(run);
        }
        replaceSamplings(run, context);
        taskRuns.save(run);
    }

    private void replaceSamplings(TaskRunEntity run, TaskContext context) {
        List<String> partitions = context.selectedPartitions == null || context.selectedPartitions.isEmpty()
                ? List.of("__unpartitioned__") : context.selectedPartitions;
        run.samplings.removeIf(existing -> !partitions.contains(existing.partitionName));
        for (String partition : partitions) {
            SamplingEntity sampling = run.samplings.stream()
                    .filter(existing -> existing.partitionName.equals(partition))
                    .findFirst()
                    .orElseGet(() -> {
                        SamplingEntity created = new SamplingEntity();
                        created.taskRun = run;
                        created.partitionName = partition;
                        run.samplings.add(created);
                        return created;
                    });
            sampling.status = context.status.name();
            sampling.plannedRows = context.targetSampleRows;
            sampling.sampledRows = context.sampledRows;
            replaceShards(sampling, context.shards, partition, partitions.size());
        }
    }

    private void replaceShards(SamplingEntity sampling, List<ShardTask> sourceShards, String partition, int partitionCount) {
        sampling.shardTasks.removeIf(existing -> sourceShards.stream().noneMatch(source -> source.shardId.equals(existing.shardId)));
        for (ShardTask sourceShard : sourceShards) {
            if (!belongsToPartition(sourceShard, partition, partitionCount)) continue;
            ShardTaskEntity shard = sampling.shardTasks.stream()
                    .filter(existing -> existing.shardId.equals(sourceShard.shardId))
                    .findFirst()
                    .orElseGet(() -> {
                        ShardTaskEntity created = new ShardTaskEntity();
                        created.shardId = sourceShard.shardId;
                        created.sampling = sampling;
                        sampling.shardTasks.add(created);
                        return created;
                    });
            shard.partitionGroup = String.join(",", sourceShard.partitionGroup);
            shard.status = sourceShard.status.name();
            shard.plannedRows = sourceShard.plannedRowsPerRun;
            shard.sampledRows = sourceShard.sampledRows;
            shard.attemptCount = sourceShard.attemptCount;
            shard.resultCollected = sourceShard.resultCollected;
            shard.finalSql = sourceShard.finalSql;
            shard.message = sourceShard.message;
        }
    }

    private boolean belongsToPartition(ShardTask shard, String partition, int partitionCount) {
        return partitionCount == 1 || shard.partitionGroup == null || shard.partitionGroup.isEmpty() || shard.partitionGroup.contains(partition);
    }

    @Override
    @Transactional
    public Optional<TaskContext> findById(String taskId) {
        return taskRuns.findByRunId(taskId).map(this::toContext);
    }

    @Override
    @Transactional
    public Collection<TaskContext> findAll() {
        return taskRuns.findAll().stream().map(this::toContext).toList();
    }

    @Override
    @Transactional
    public Collection<TaskContext> findActiveTasks() {
        return taskRuns.findByStatusIn(List.of(ParentTaskStatus.DISPATCHED.name(), ParentTaskStatus.RUNNING.name(), ParentTaskStatus.PIPELINE_RUNNING.name()))
                .stream().map(this::toContext).toList();
    }

    private TaskContext toContext(TaskRunEntity run) {
        TaskContext context = new TaskContext();
        context.taskId = run.runId;
        context.databaseName = run.validationTask.databaseName;
        context.tableName = run.validationTask.tableName;
        context.targetSampleRows = run.targetSampleRows;
        context.sampledRows = run.sampledRows;
        context.status = ParentTaskStatus.valueOf(run.status);
        context.createdAt = run.createdAt;
        context.updatedAt = run.updatedAt;
        context.finalSqls = run.finalSql == null || run.finalSql.isBlank() ? new ArrayList<>() : new ArrayList<>(List.of(run.finalSql.split("\\n")));
        for (SamplingEntity sampling : run.samplings) {
            if (!"__unpartitioned__".equals(sampling.partitionName)) context.selectedPartitions.add(sampling.partitionName);
            for (ShardTaskEntity persisted : sampling.shardTasks) {
                ShardTask shard = new ShardTask();
                shard.shardId = persisted.shardId;
                shard.partitionGroup = persisted.partitionGroup == null || persisted.partitionGroup.isBlank() ? new ArrayList<>() : new ArrayList<>(List.of(persisted.partitionGroup.split(",")));
                shard.status = ShardTaskStatus.valueOf(persisted.status);
                shard.plannedRowsPerRun = persisted.plannedRows;
                shard.sampledRows = persisted.sampledRows;
                shard.attemptCount = persisted.attemptCount;
                shard.resultCollected = persisted.resultCollected;
                shard.finalSql = persisted.finalSql;
                shard.message = persisted.message;
                context.shards.add(shard);
            }
        }
        return context;
    }
}
