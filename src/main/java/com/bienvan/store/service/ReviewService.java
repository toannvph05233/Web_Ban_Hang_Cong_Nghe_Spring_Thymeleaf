package com.bienvan.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bienvan.store.model.Product;
import com.bienvan.store.model.Review;
import com.bienvan.store.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    public Review add(Review review){
        return reviewRepository.save(review);
    }

    public List<Review> findByProduct(Product product){
        return reviewRepository.findByProduct(product);
    }
}
