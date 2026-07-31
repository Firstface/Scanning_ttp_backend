package com.example.hivesampling.persistence.repository;

import com.example.hivesampling.persistence.entity.AuditEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEventJpaRepository extends JpaRepository<AuditEventEntity, Long> { }
