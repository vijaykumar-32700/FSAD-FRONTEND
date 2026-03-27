package com.klu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.klu.entity.Product;
import com.klu.repository.ProductRepo;

@Service
public class ProductService {

    private final ProductRepo repo;

    public ProductService(ProductRepo repo){
        this.repo = repo;
    }

    public Product save(Product p){
        return repo.save(p);
    }

    public List<Product> getByCategory(String category){
        return repo.findByCategory(category);
    }

    public List<Product> filterByPrice(double min,double max){
        return repo.findByPriceBetween(min, max);
    }

    public List<Product> getSorted(){
        return repo.sortProductsByPrice();
    }

    public List<Product> getExpensive(double price){
        return repo.findExpensiveProducts(price);
    }
}