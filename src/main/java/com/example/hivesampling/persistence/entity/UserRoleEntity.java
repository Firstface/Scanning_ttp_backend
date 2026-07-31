package com.example.hivesampling.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_role", uniqueConstraints = @UniqueConstraint(name = "uk_user_role", columnNames = {"user_id", "role_id"}))
public class UserRoleEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    public AppUserEntity user;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "role_id", nullable = false)
    public AppRoleEntity role;
}
