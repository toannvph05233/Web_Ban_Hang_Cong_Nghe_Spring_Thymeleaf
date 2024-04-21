package com.bienvan.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bienvan.store.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long>{
    Optional<Brand> findByName(String name);
}
