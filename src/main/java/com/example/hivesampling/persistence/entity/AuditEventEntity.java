package com.example.hivesampling.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;
import java.time.Instant;

@Entity
@Table(name = "audit_event")
public class AuditEventEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "task_run_id", nullable = false)
    public TaskRunEntity taskRun;
    @Column(name = "event_type", nullable = false, length = 100)
    public String eventType;
    @Column(name = "actor", nullable = false, length = 100)
    public String actor;
    @Column(name = "occurred_at", nullable = false)
    public Instant occurredAt;
    @JdbcTypeCode(Types.LONGVARCHAR) @Column(name = "details", columnDefinition = "LONGTEXT")
    public String details;
}
