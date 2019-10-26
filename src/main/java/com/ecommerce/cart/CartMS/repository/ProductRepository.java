package com.ecommerce.cart.CartMS.repository;

//package com.example.repository;

import com.ecommerce.cart.CartMS.model.Product;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

}

