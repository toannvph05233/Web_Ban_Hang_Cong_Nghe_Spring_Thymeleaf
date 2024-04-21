package com.bienvan.store.service.impl;

import java.util.List;
import java.util.Optional;

import com.bienvan.store.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bienvan.store.model.Category;
import com.bienvan.store.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        if(id == null){
            return null;
        }
        return categoryRepository.findById(id).orElse(null);
    }

    public Category delete(Category category) {
        categoryRepository.delete(category);
        return category;
    }

    public Optional<Category> findByName(String name){
        return categoryRepository.findByName(name);
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).get();
    }
}
