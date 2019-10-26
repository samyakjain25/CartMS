package com.ecommerce.cart.CartMS;

import com.ecommerce.cart.CartMS.model.Cart;
import com.ecommerce.cart.CartMS.model.Product;
import com.ecommerce.cart.CartMS.repository.CartRepositoryCustom;
import com.ecommerce.cart.CartMS.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class TestCartService {

    @InjectMocks
    CartService cartService;

    @Mock
    CartRepositoryCustom cartRepositoryCustom;

    Product product;
    Cart cart;

    @Before
    public void setUp(){
        initMocks(this);

    }

    @Test
    public void TestAddToCart(){

    }



}
