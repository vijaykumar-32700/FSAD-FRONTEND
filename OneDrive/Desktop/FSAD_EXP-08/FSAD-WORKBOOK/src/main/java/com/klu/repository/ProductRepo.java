package com.klu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.klu.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

    // Derived Queries
    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(double min,double max);

    // JPQL Queries

    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    List<Product> sortProductsByPrice();

    @Query("SELECT p FROM Product p WHERE p.price > ?1")
    List<Product> findExpensiveProducts(double price);

    @Query("SELECT p FROM Product p WHERE p.category = ?1")
    List<Product> findProductsByCategory(String category);
}