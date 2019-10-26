package com.ecommerce.cart.CartMS.repository;

import com.ecommerce.cart.CartMS.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartRepositoryCustomImpl implements CartRepositoryCustom{
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CartRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Cart fetchCartByAccountId(String accountId) {
        final Query query = new Query();

        query.addCriteria(Criteria.where("accountId").is(accountId));
        List<Cart> cartList = mongoTemplate.find(query, Cart.class);
        if(cartList.size() == 0){
            Cart cart = new Cart();
            cart.setAccountId(accountId);
            return cart;
        }
        else return cartList.get(0);
    }

//    public void  fetchProduct(String productId)
//    {
//        final Query query = new Query();
//        query.addCriteria(Criteria.where("accountId").is(accountId));
//        List<Cart> cartList = mongoTemplate.find(query, Cart.class);
//
//
//    }


}
