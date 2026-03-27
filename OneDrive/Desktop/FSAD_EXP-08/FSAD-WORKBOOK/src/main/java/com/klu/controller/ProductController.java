package com.klu.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.klu.entity.Product;
import com.klu.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service){
        this.service = service;
    }

    // Insert sample product
    @PostMapping
    public Product save(@RequestBody Product p){
        return service.save(p);
    }

    // /products/category/electronics
    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category){
        return service.getByCategory(category);
    }

    // /products/filter?min=100&max=500
    @GetMapping("/filter")
    public List<Product> filter(
            @RequestParam double min,
            @RequestParam double max){
        return service.filterByPrice(min, max);
    }

    // /products/sorted
    @GetMapping("/sorted")
    public List<Product> sorted(){
        return service.getSorted();
    }

    // /products/expensive/500
    @GetMapping("/expensive/{price}")
    public List<Product> expensive(@PathVariable double price){
        return service.getExpensive(price);
    }
}