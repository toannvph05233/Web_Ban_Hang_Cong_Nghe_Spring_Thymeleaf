package com.bienvan.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bienvan.store.dto.ProductDto;
import com.bienvan.store.model.*;
import com.bienvan.store.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<ProductDto>();
        if (!products.isEmpty()) {
            for (Product p : products) {
                ProductDto tmp = new ProductDto(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getImage(),
                        p.getQuantity(), p.getColor().getId(), p.getBrand().getId(), p.getUser().getId(), p.getCategory().getId());
                productDtos.add(tmp);
            }
            return productDtos;
        }
        return null;
    }

    public List<Product> getProductsForHomePage() {
        return productRepository.findByIsDeletedFalse();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> fillAllByCategory(Category category){
        return productRepository.findByCategory(category);
    }

    public Page<Product> findProductsByCategory(Category category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }

    public Page<Product> findProductsByBrand(Brand brand, Pageable pageable) {
        return productRepository.findByBrand(brand, pageable);
    }

    public List<Product> fillAllByBrand(Brand brand){
        return productRepository.findByBrand(brand);
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<Product> search(double minPrice, double maxPrice, Brand brand, Color color, Category category) {
        return productRepository.findByPriceBetweenAndBrandAndColorAndCategory(minPrice, maxPrice, brand, color,
                category);
    }

    public List<Product> findByNameContaining(String keyword){
        return productRepository.findByNameContaining(keyword);
    }


}
