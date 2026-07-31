package com.example.hivesampling.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;

@Entity
@Table(name = "shard_task")
public class ShardTaskEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "shard_id", nullable = false, unique = true, length = 100)
    public String shardId;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "sampling_id", nullable = false)
    public SamplingEntity sampling;
    @Column(name = "partition_group", length = 1000)
    public String partitionGroup;
    @Column(name = "status", nullable = false, length = 50)
    public String status;
    @Column(name = "planned_rows", nullable = false)
    public int plannedRows;
    @Column(name = "sampled_rows", nullable = false)
    public long sampledRows;
    @Column(name = "attempt_count", nullable = false)
    public int attemptCount;
    @Column(name = "result_collected", nullable = false)
    public boolean resultCollected;
    @JdbcTypeCode(Types.LONGVARCHAR) @Column(name = "final_sql", columnDefinition = "LONGTEXT")
    public String finalSql;
    @Column(name = "message", length = 2000)
    public String message;
}
