package com.example.hivesampling.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "validation_task")
public class ValidationTaskEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "external_key", nullable = false, unique = true, length = 255)
    public String externalKey;
    @Column(name = "database_name", nullable = false, length = 255)
    public String databaseName;
    @Column(name = "table_name", nullable = false, length = 255)
    public String tableName;
    @Column(name = "status", nullable = false, length = 50)
    public String status;
    @OneToMany(mappedBy = "validationTask")
    public List<TaskRunEntity> taskRuns = new ArrayList<>();
}
