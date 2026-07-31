package com.example.hivesampling.persistence.repository;

import com.example.hivesampling.persistence.entity.ValidationTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ValidationTaskJpaRepository extends JpaRepository<ValidationTaskEntity, Long> {
    Optional<ValidationTaskEntity> findByExternalKey(String externalKey);
}
