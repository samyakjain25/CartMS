package com.ecommerce.cart.CartMS.repository;

import com.ecommerce.cart.CartMS.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {

}
