package com.example.hivesampling.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class AppUserEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "username", nullable = false, unique = true, length = 100)
    public String username;
    @Column(name = "password_hash", nullable = false, length = 255)
    public String passwordHash;
    @Column(name = "enabled", nullable = false)
    public boolean enabled = true;
    @OneToMany(mappedBy = "user")
    public List<UserRoleEntity> userRoles = new ArrayList<>();
}
