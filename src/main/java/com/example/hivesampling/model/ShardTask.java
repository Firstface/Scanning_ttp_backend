package com.example.hivesampling.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ShardTask {

    public String shardId;
    public List<String> partitionGroup = new ArrayList<>();
    public int plannedRowsPerRun;
    public long sampledRows;
    public long offset;
    public long lastRunRows;
    public String innerSql;
    public String finalSql;
    public String sqlPreview;
    public ShardTaskStatus status = ShardTaskStatus.QUEUED;
    public int attemptCount;
    public boolean resultCollected;
    public Instant createdAt = Instant.now();
    public Instant updatedAt = Instant.now();

    public void changeStatus(ShardTaskStatus nextStatus) {
        this.status = nextStatus;
        this.updatedAt = Instant.now();
    }
}
