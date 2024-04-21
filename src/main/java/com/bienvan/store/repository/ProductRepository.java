package com.bienvan.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bienvan.store.model.Brand;
import com.bienvan.store.model.Category;
import com.bienvan.store.model.Color;
import com.bienvan.store.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    List<Product> findByCategory(Category category);

    List<Product> findByBrand(Brand brand);

    Page<Product> findByCategory(Category category, Pageable pageable);
    
    Page<Product> findByBrand(Brand brand, Pageable pageable);

    List<Product> findByIsDeletedFalse();
    
    List<Product> findByNameContaining(String keyword);

    @Query("SELECT p FROM Product p WHERE (:minPrice IS NULL OR p.price >= :minPrice) AND " +
       "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
       "(:brand IS NULL OR p.brand = :brand) AND " +
       "(:color IS NULL OR p.color = :color) AND " +
       "(:category IS NULL OR p.category = :category)")
    List<Product> findByPriceBetweenAndBrandAndColorAndCategory(@Param("minPrice") Double minPrice,
                                                            @Param("maxPrice") Double maxPrice,
                                                            @Param("brand") Brand brand,
                                                            @Param("color") Color color,
                                                            @Param("category") Category category);


}
