package com.bienvan.store.controller;

import java.util.*;

import com.bienvan.store.service.ICategoryService;
import com.bienvan.store.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bienvan.store.model.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    ICategoryService categoryServiceImpl;


    @GetMapping
    public ResponseEntity<?> getListCategory() {
        List<Category> categories = categoryServiceImpl.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        Category category = categoryServiceImpl.getCategoryById(id);

        if(category != null){
            res.put("code", 0);
            res.put("message", "Find category successfully");
            res.put("data", category);
        return ResponseEntity.ok(res);
        }
        else{
            res.put("code", 1);
            res.put("message", "Find category failed");
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        Map<String, Object> res = new HashMap<>();
        Optional<Category> optional = categoryServiceImpl.findByName(category.getName());
        if (!optional.isPresent()) {
            Category data = categoryServiceImpl.save(category);
            res.put("code", 0);
            res.put("message", "Add category successfully");
            res.put("data", data);
        } else {
            res.put("code", 1);
            res.put("message", "Add category failed");
        }
        return ResponseEntity.ok(res);
    }

    @PutMapping // update
    public ResponseEntity<Map<String, Object>> updateCategory(@RequestBody Category category) {
        Map<String, Object> res = new HashMap<>();

        Category existingCategory = categoryServiceImpl.getCategoryById(category.getId());
    
        if (existingCategory != null) {
            existingCategory.setName(category.getName());
    
            categoryServiceImpl.save(existingCategory);
            res.put("code", 0);
            res.put("message", "Update category successfully");
        } else {
            res.put("code", 1);
            res.put("message", "Update category failed");
        }
        return ResponseEntity.ok(res);
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        Category existingCategory = categoryServiceImpl.getCategoryById(id);

        if (existingCategory != null) {
            categoryServiceImpl.delete(existingCategory);
            res.put("code", 0);
            res.put("message", "Delete category successfully");
        } else {
            res.put("code", 1);
            res.put("message", "Delete category failed");     
        }
        return ResponseEntity.ok(res);
    }
}
