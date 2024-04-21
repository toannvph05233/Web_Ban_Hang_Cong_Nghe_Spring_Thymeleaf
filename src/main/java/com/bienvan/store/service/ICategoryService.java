package com.bienvan.store.service;

import com.bienvan.store.model.Category;

import java.util.Optional;

public interface ICategoryService extends ManagerService<Category> {
    Optional<Category> findByName(String name);
    Category getCategoryById(Long id);
}
