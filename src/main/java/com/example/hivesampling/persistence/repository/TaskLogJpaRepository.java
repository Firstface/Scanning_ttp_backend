package com.example.hivesampling.persistence.repository;

import com.example.hivesampling.persistence.entity.TaskLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskLogJpaRepository extends JpaRepository<TaskLogEntity, Long> {
    List<TaskLogEntity> findByTaskRunRunIdOrderByLoggedAtAsc(String runId);
}
