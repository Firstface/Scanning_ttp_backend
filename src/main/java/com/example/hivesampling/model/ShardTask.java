package com.example.hivesampling.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShardTask {

    public String shardId;
    public List<String> partitionGroup = new ArrayList<>();
    public String partitionName;
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
    public String message;
    public Instant createdAt = Instant.now();
    public Instant updatedAt = Instant.now();

    public void changeStatus(ShardTaskStatus nextStatus) {
        this.status = nextStatus;
        this.updatedAt = Instant.now();
    }

    public String getPartitionName() {
        if (partitionName != null && !partitionName.isEmpty()) {
            return partitionName;
        }
        if (partitionGroup != null && !partitionGroup.isEmpty()) {
            return partitionGroup.stream()
                .map(p -> "dt=" + p)
                .collect(Collectors.joining(", "));
        }
        return "";
    }

    public void setPartitionName(String partitionName) {
        this.partitionName = partitionName;
    }
}
