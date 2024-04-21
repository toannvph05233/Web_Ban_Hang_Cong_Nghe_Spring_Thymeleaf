package com.bienvan.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bienvan.store.model.ERole;
import com.bienvan.store.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(ERole name);
}
