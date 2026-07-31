CREATE TABLE validation_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    external_key VARCHAR(255) NOT NULL,
    database_name VARCHAR(255) NOT NULL,
    table_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_validation_task_external_key UNIQUE (external_key)
);
CREATE INDEX idx_validation_task_status ON validation_task(status);

CREATE TABLE task_run (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    run_id VARCHAR(64) NOT NULL,
    validation_task_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    target_sample_rows BIGINT NOT NULL,
    sampled_rows BIGINT NOT NULL DEFAULT 0,
    final_sql LONGTEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_task_run_run_id UNIQUE (run_id),
    CONSTRAINT fk_task_run_validation_task FOREIGN KEY (validation_task_id) REFERENCES validation_task(id)
);
CREATE INDEX idx_task_run_validation_task ON task_run(validation_task_id);
CREATE INDEX idx_task_run_status ON task_run(status);
