package com.bienvan.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bienvan.store.model.Product;
import com.bienvan.store.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByProduct(Product product);
}
