package com.bienvan.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bienvan.store.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>  {
    Optional<Category> findByName(String name);
}
