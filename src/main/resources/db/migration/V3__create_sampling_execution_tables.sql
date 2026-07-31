CREATE TABLE sampling (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_run_id BIGINT NOT NULL,
    partition_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    planned_rows BIGINT NOT NULL,
    sampled_rows BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_sampling_task_run_partition UNIQUE (task_run_id, partition_name),
    CONSTRAINT fk_sampling_task_run FOREIGN KEY (task_run_id) REFERENCES task_run(id)
);
CREATE INDEX idx_sampling_task_run ON sampling(task_run_id);

CREATE TABLE shard_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shard_id VARCHAR(100) NOT NULL,
    sampling_id BIGINT NOT NULL,
    partition_group VARCHAR(1000),
    status VARCHAR(50) NOT NULL,
    planned_rows INT NOT NULL,
    sampled_rows BIGINT NOT NULL DEFAULT 0,
    attempt_count INT NOT NULL DEFAULT 0,
    result_collected BOOLEAN NOT NULL DEFAULT FALSE,
    final_sql LONGTEXT,
    message VARCHAR(2000),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_shard_task_shard_id UNIQUE (shard_id),
    CONSTRAINT fk_shard_task_sampling FOREIGN KEY (sampling_id) REFERENCES sampling(id)
);
CREATE INDEX idx_shard_task_sampling ON shard_task(sampling_id);
CREATE INDEX idx_shard_task_status ON shard_task(status);
