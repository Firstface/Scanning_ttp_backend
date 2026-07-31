package com.example.hivesampling.persistence;

import com.example.hivesampling.model.LogEntry;
import com.example.hivesampling.persistence.entity.AuditEventEntity;
import com.example.hivesampling.persistence.entity.TaskLogEntity;
import com.example.hivesampling.persistence.entity.TaskRunEntity;
import com.example.hivesampling.persistence.repository.AuditEventJpaRepository;
import com.example.hivesampling.persistence.repository.TaskLogJpaRepository;
import com.example.hivesampling.persistence.repository.TaskRunJpaRepository;
import com.example.hivesampling.service.TaskLogStore;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Profile({"persistence", "staging", "production-like"})
public class JpaTaskLogStore implements TaskLogStore {
    private final TaskRunJpaRepository taskRuns;
    private final TaskLogJpaRepository logs;
    private final AuditEventJpaRepository audits;

    public JpaTaskLogStore(TaskRunJpaRepository taskRuns, TaskLogJpaRepository logs, AuditEventJpaRepository audits) {
        this.taskRuns = taskRuns;
        this.logs = logs;
        this.audits = audits;
    }

    @Override
    @Transactional
    public void info(String taskId, String message) {
        TaskRunEntity run = taskRuns.findByRunId(taskId)
                .orElseThrow(() -> new IllegalStateException("Task run must be persisted before logging: " + taskId));
        Instant now = Instant.now();
        TaskLogEntity log = new TaskLogEntity();
        log.logId = UUID.randomUUID().toString();
        log.taskRun = run;
        log.loggedAt = now;
        log.level = "INFO";
        log.message = message;
        logs.save(log);

        AuditEventEntity audit = new AuditEventEntity();
        audit.taskRun = run;
        audit.eventType = "TASK_LOGGED";
        audit.actor = "system";
        audit.occurredAt = now;
        audit.details = message;
        audits.save(audit);
    }

    @Override
    @Transactional
    public List<LogEntry> list(String taskId) {
        return logs.findByTaskRunRunIdOrderByLoggedAtAsc(taskId).stream()
                .map(log -> new LogEntry(log.logId, log.loggedAt, log.level, log.message))
                .toList();
    }
}
