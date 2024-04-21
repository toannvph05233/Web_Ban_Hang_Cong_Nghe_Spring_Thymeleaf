package com.bienvan.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bienvan.store.model.Color;

public interface ColorRepository extends JpaRepository<Color, Long>{
    Optional<Color> findByName(String name);
}
