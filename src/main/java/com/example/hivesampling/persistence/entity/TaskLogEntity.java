package com.example.hivesampling.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "task_log")
public class TaskLogEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "log_id", nullable = false, unique = true, length = 64)
    public String logId;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "task_run_id", nullable = false)
    public TaskRunEntity taskRun;
    @Column(name = "logged_at", nullable = false)
    public Instant loggedAt;
    @Column(name = "level", nullable = false, length = 20)
    public String level;
    @Column(name = "message", nullable = false, length = 4000)
    public String message;
}
