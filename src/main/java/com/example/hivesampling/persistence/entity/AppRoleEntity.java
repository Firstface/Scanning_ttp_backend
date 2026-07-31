package com.example.hivesampling.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_role")
public class AppRoleEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "role_name", nullable = false, unique = true, length = 100)
    public String roleName;
    @OneToMany(mappedBy = "role")
    public List<UserRoleEntity> userRoles = new ArrayList<>();
}
