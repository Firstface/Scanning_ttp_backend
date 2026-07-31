package com.example.hivesampling.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_run")
public class TaskRunEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "run_id", nullable = false, unique = true, length = 64)
    public String runId;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) @JoinColumn(name = "validation_task_id", nullable = false)
    public ValidationTaskEntity validationTask;
    @Column(name = "status", nullable = false, length = 50)
    public String status;
    @Column(name = "target_sample_rows", nullable = false)
    public long targetSampleRows;
    @Column(name = "sampled_rows", nullable = false)
    public long sampledRows;
    @JdbcTypeCode(Types.LONGVARCHAR) @Column(name = "final_sql", columnDefinition = "LONGTEXT")
    public String finalSql;
    @OneToMany(mappedBy = "taskRun", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<SamplingEntity> samplings = new ArrayList<>();
    @OneToMany(mappedBy = "taskRun", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<TaskLogEntity> taskLogs = new ArrayList<>();
    @OneToMany(mappedBy = "taskRun", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AuditEventEntity> auditEvents = new ArrayList<>();
}
