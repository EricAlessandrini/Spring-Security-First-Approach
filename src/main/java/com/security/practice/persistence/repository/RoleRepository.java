package com.security.practice.persistence.repository;

import com.security.practice.domain.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query("SELECT r FROM RoleEntity r WHERE r.role = :roleName")
    RoleEntity findRoleEntityByRoleName(@Param("roleName") String roleName);
}
