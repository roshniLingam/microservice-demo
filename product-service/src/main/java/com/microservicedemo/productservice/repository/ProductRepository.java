package com.microservicedemo.productservice.repository;

import com.microservicedemo.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
