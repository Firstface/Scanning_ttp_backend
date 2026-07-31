CREATE TABLE task_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    log_id VARCHAR(64) NOT NULL,
    task_run_id BIGINT NOT NULL,
    logged_at TIMESTAMP NOT NULL,
    level VARCHAR(20) NOT NULL,
    message VARCHAR(4000) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_task_log_log_id UNIQUE (log_id),
    CONSTRAINT fk_task_log_task_run FOREIGN KEY (task_run_id) REFERENCES task_run(id)
);
CREATE INDEX idx_task_log_task_run_logged_at ON task_log(task_run_id, logged_at);

CREATE TABLE audit_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_run_id BIGINT NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    actor VARCHAR(100) NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    details LONGTEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_audit_event_task_run FOREIGN KEY (task_run_id) REFERENCES task_run(id)
);
CREATE INDEX idx_audit_event_task_run_occurred_at ON audit_event(task_run_id, occurred_at);
