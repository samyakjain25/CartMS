package com.ecommerce.cart.CartMS;

import com.ecommerce.cart.CartMS.controller.CartController;
import com.ecommerce.cart.CartMS.exception.ProductNotfoundException;
import com.ecommerce.cart.CartMS.model.Cart;
import com.ecommerce.cart.CartMS.model.CartItem;
import com.ecommerce.cart.CartMS.model.CustomResponse;
import com.ecommerce.cart.CartMS.model.Product;
import com.ecommerce.cart.CartMS.repository.CartRepository;
import com.ecommerce.cart.CartMS.service.CartService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class TestCartController {

    @InjectMocks
    private CartController cartController;

    @Mock
    CartService cartService;

    @Mock
    CartRepository cartRepository;

    @Autowired
    private MockMvc mockMvc;

    Product product;
    Cart cart;
    @Before
    public void setUp() throws Exception {
        initMocks(this);
        cartController = new CartController(cartService);
        product = new Product("P05", "A9", 25000.0,"url",10,"Samsung");

        CartItem cartItem = new CartItem();
        cartItem.setItem(product);
        List<CartItem> list = new ArrayList<>();
        list.add(cartItem);
        cart = new Cart("C01",list,"acc01",250000.0);
    }

    @Test
    public void TestGetAllCartItem() {
        given(cartService.fetchAllCartItems()).willReturn(cart);
        ResponseEntity<CustomResponse> resp = cartController.getAllCartItems();
        CustomResponse<Cart> customResponse = resp.getBody();
        Cart expected = customResponse.getResponseData();
        Assert.assertEquals(expected,cart);


        given(cartService.fetchAllCartItems()).willReturn(null);
        ResponseEntity<CustomResponse> resp1 = cartController.getAllCartItems();
        CustomResponse<Cart> customResponse1 = resp1.getBody();
        Cart expected1 = customResponse1.getResponseData();
        Assert.assertEquals(expected1,null);
        Assert.assertEquals(404,customResponse1.getStatusCode());

    }

    @Test
    public void TestAddItemToCart() throws Exception {

        given(cartService.addToCart(product)).willReturn(cart);
        ResponseEntity<CustomResponse> resp = cartController.addItemToCart(product);
        CustomResponse<Cart> customResponse = resp.getBody();
        Cart expected = customResponse.getResponseData();
        Assert.assertEquals(expected,cart);

        given(cartService.addToCart(product)).willReturn(null);
        ResponseEntity<CustomResponse> respNull = cartController.addItemToCart(product);
        CustomResponse<Cart> customResponseNull = respNull.getBody();
        Cart expectedNull = customResponseNull.getResponseData();
        Assert.assertEquals(expectedNull,null);

        ResponseEntity<CustomResponse> respNull2 = cartController.addItemToCart(null);
        CustomResponse<Cart> customResponseNull2 = respNull2.getBody();
        Cart expectedNull2 = customResponseNull2.getResponseData();
        Assert.assertEquals(expectedNull2,null);
    }

    @Test
    public void TestBuy() {
        ResponseEntity<CustomResponse> resp = cartController.buy(product);
        CustomResponse<Cart> customResponse = resp.getBody();
        Cart expected = customResponse.getResponseData();
        Assert.assertEquals(200,customResponse.getStatusCode());

        ResponseEntity<CustomResponse> resp1 = cartController.buy(null);
        CustomResponse<Cart> customResponse1 = resp1.getBody();
        Cart expected1 = customResponse1.getResponseData();
        Assert.assertEquals(404,customResponse1.getStatusCode());

    }

    @Test
    public void TestRemoveCartItem() {
        given(cartService.removeCartItem(product.getProductId())).willReturn(cart);
        ResponseEntity<CustomResponse> resp = cartController.removeCartItem(product.getProductId());
        CustomResponse<Cart> customResponse = resp.getBody();
        Cart expected = customResponse.getResponseData();
        Assert.assertEquals(200,customResponse.getStatusCode());

        given(cartService.removeCartItem(product.getProductId())).willReturn(null);
        ResponseEntity<CustomResponse> resp1 = cartController.removeCartItem(product.getProductId());
        CustomResponse<Cart> customResponse1 = resp1.getBody();
        Cart expected1 = customResponse1.getResponseData();
        Assert.assertEquals(404,customResponse1.getStatusCode());

        given(cartService.removeCartItem(product.getProductId())).willThrow(new ProductNotfoundException());
        ResponseEntity<CustomResponse> resp2 = cartController.removeCartItem(product.getProductId());
        CustomResponse<Cart> customResponse2 = resp1.getBody();
        Cart expected2 = customResponse2.getResponseData();
        Assert.assertEquals(404,customResponse1.getStatusCode());


    }





    @Test
    public void testUpdateCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setItem(product);
        List<CartItem> list = new ArrayList<>();
        list.add(cartItem);
        Cart cart = new Cart();
        cart.setAccountId("acc01");
        cart.setAmountPayable(250000.0);
        cart.setCartItemList(list);
        cart.setId("C01");
        product=null;
        // given(cartService.changeQuantity(product)).willReturn(null);
        ResponseEntity<CustomResponse> resp1 = cartController.updateCartItem(product);
        CustomResponse<Cart> customResponse1 = resp1.getBody();
        Cart expected1 = customResponse1.getResponseData();
        Assert.assertEquals(expected1,null);
        Assert.assertEquals(404,customResponse1.getStatusCode());
    }

    @Test
    public void testUpdateCartItem1() {
        CartItem cartItem = new CartItem();
        cartItem.setItem(product);
        List<CartItem> list = new ArrayList<>();
        list.add(cartItem);
        Cart cart = new Cart();
        cart.setAccountId("acc01");
        cart.setAmountPayable(250000.0);
        cart.setCartItemList(list);
        cart.setId("C01");
        given(cartService.changeQuantity(product)).willReturn(null);
        ResponseEntity<CustomResponse> resp1 = cartController.updateCartItem(product);
        CustomResponse<Cart> customResponse1 = resp1.getBody();
        Cart expected1 = customResponse1.getResponseData();
        Assert.assertEquals(expected1,null);
        Assert.assertEquals(404,customResponse1.getStatusCode());
//        given(cartService.up(product)).willReturn(cart);
    }
//    @Test
//    public void testUpdateCartItem2(){
//        CartItem cartItem = new CartItem();
//        cartItem.setItem(product);
//        List<CartItem> list = new ArrayList<>();
//        list.add(cartItem);
//        Cart cart = new Cart();
//        cart.setAccountId("acc01");
//        cart.setAmountPayable(250000.0);
//        cart.setCartItemList(list);
//        cart.setId("C01");
//        given(cartService.changeQuantity(product)).willReturn(null);
//        ResponseEntity<CustomResponse> resp1 = cartController.updateCartItem(product);
//        CustomResponse<Cart> customResponse1 = resp1.getBody();
//        Cart expected1 = customResponse1.getResponseData();
//        Assert.assertEquals(expected1,product);
//        Assert.assertEquals(200,customResponse1.getStatusCode());
////        given(cartService.up(product)).willReturn(cart);
//    }

    @Test
    public void testUpdateCartItem3() {
        CartItem cartItem = new CartItem();
        cartItem.setItem(product);
        List<CartItem> list = new ArrayList<>();
        list.add(cartItem);
        Cart cart = new Cart();
        cart.setAccountId("acc01");
        cart.setAmountPayable(250000.0);
        cart.setCartItemList(list);
        cart.setId("C01");
        given(cartService.changeQuantity(product)).willReturn(cart);
        ResponseEntity<CustomResponse> resp1 = cartController.updateCartItem(product);
        CustomResponse<Product> customResponse1 = resp1.getBody();
        Product expected1 = customResponse1.getResponseData();
        Assert.assertEquals(expected1,product);
        Assert.assertEquals(200,customResponse1.getStatusCode());
//        given(cartService.up(product)).willReturn(cart);
    }

    @Test
    public void testUpdateCartItem4() {
        CartItem cartItem = new CartItem();
        cartItem.setItem(product);
        List<CartItem> list = new ArrayList<>();
        list.add(cartItem);
        Cart cart = new Cart();
        cart.setAccountId("acc01");
        cart.setAmountPayable(250000.0);
        cart.setCartItemList(list);
        cart.setId("C01");
        given(cartService.changeQuantity(product)).willThrow(new ProductNotfoundException());
        ResponseEntity<CustomResponse> resp1 = cartController.updateCartItem(product);
        CustomResponse<Product> customResponse1 = resp1.getBody();
        Product expected1 = customResponse1.getResponseData();
        Assert.assertEquals(expected1, null);
        Assert.assertEquals(404, customResponse1.getStatusCode());
//        given(cartService.up(product)).willReturn(cart);
    }
}
