package com.example.hivesampling.persistence;

import com.example.hivesampling.model.ParentTaskStatus;
import com.example.hivesampling.model.ShardTask;
import com.example.hivesampling.model.ShardTaskStatus;
import com.example.hivesampling.model.TaskContext;
import com.example.hivesampling.persistence.entity.TaskRunEntity;
import com.example.hivesampling.persistence.repository.AuditEventJpaRepository;
import com.example.hivesampling.persistence.repository.TaskRunJpaRepository;
import com.example.hivesampling.persistence.repository.ValidationTaskJpaRepository;
import com.example.hivesampling.service.TaskLogStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:persistence_it;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=validate",
        "app.auth.enabled=false",
        "app.rate-limit.enabled=false"
})
@ActiveProfiles("persistence")
class JpaTaskStoreIT {
    @Autowired JpaTaskStore taskStore;
    @Autowired TaskLogStore taskLogStore;
    @Autowired ValidationTaskJpaRepository validationTasks;
    @Autowired TaskRunJpaRepository taskRuns;
    @Autowired AuditEventJpaRepository auditEvents;
    @Autowired EntityManager entityManager;

    @Test
    @Transactional
    void persistsRequiredOneToManyRelationships() {
        TaskContext context = new TaskContext();
        context.taskId = "persistence-it-task";
        context.databaseName = "demo_db";
        context.tableName = "events";
        context.targetSampleRows = 1000;
        context.sampledRows = 900;
        context.selectedPartitions = List.of("2026-07-01", "2026-07-02");
        context.status = ParentTaskStatus.RUNNING;

        ShardTask shard = new ShardTask();
        shard.shardId = "persistence-it-shard";
        shard.partitionGroup = List.of("2026-07-01");
        shard.status = ShardTaskStatus.SUCCESS;
        shard.plannedRowsPerRun = 500;
        shard.sampledRows = 450;
        shard.resultCollected = true;
        context.shards.add(shard);

        taskStore.save(context);
        taskLogStore.info(context.taskId, "relationship verification log");

        entityManager.flush();
        entityManager.clear();
        TaskContext secondRun = new TaskContext();
        secondRun.taskId = "persistence-it-task-2";
        secondRun.databaseName = "demo_db";
        secondRun.tableName = "events";
        secondRun.targetSampleRows = 200;
        secondRun.status = ParentTaskStatus.CREATED;
        taskStore.save(secondRun);
        entityManager.flush();
        entityManager.clear();

        TaskContext monitorContext = taskStore.findById(context.taskId).orElseThrow();
        monitorContext.databaseName = null;
        monitorContext.tableName = null;
        taskStore.save(monitorContext);
        assertEquals("demo_db", monitorContext.databaseName);
        assertEquals("events", monitorContext.tableName);
        entityManager.flush();
        entityManager.clear();

        TaskRunEntity run = taskRuns.findByRunId(context.taskId).orElseThrow();
        assertEquals(1, validationTasks.count());
        assertEquals(2, taskRuns.count());
        Number validationTaskId = (Number) entityManager.createNativeQuery(
                "SELECT validation_task_id FROM task_run WHERE run_id = 'persistence-it-task'")
                .getSingleResult();
        assertEquals(2, taskRuns.countByValidationTaskId(validationTaskId.longValue()));
        assertEquals(2, run.samplings.size());
        assertEquals(1, run.samplings.stream().mapToInt(s -> s.shardTasks.size()).sum());
        Number validationTaskLinks = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM task_run tr JOIN validation_task vt ON tr.validation_task_id = vt.id WHERE vt.external_key = 'demo_db.events'")
                .getSingleResult();
        assertEquals(2L, validationTaskLinks.longValue());
        assertEquals(1, taskLogStore.list(context.taskId).size());
        assertEquals(1, auditEvents.count());
        assertTrue(taskStore.findById(context.taskId).isPresent());
    }
}
