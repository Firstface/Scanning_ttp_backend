package com.example.hivesampling.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sampling", uniqueConstraints = @UniqueConstraint(name = "uk_sampling_task_run_partition", columnNames = {"task_run_id", "partition_name"}))
public class SamplingEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "task_run_id", nullable = false)
    public TaskRunEntity taskRun;
    @Column(name = "partition_name", nullable = false, length = 255)
    public String partitionName;
    @Column(name = "status", nullable = false, length = 50)
    public String status;
    @Column(name = "planned_rows", nullable = false)
    public long plannedRows;
    @Column(name = "sampled_rows", nullable = false)
    public long sampledRows;
    @OneToMany(mappedBy = "sampling", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ShardTaskEntity> shardTasks = new ArrayList<>();
}
