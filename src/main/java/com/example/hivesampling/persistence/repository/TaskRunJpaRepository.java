package com.example.hivesampling.persistence.repository;

import com.example.hivesampling.persistence.entity.TaskRunEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.Optional;

public interface TaskRunJpaRepository extends JpaRepository<TaskRunEntity, Long> {
    Optional<TaskRunEntity> findByRunId(String runId);
    Collection<TaskRunEntity> findByStatusIn(Collection<String> statuses);
    long countByValidationTaskId(Long validationTaskId);
}
