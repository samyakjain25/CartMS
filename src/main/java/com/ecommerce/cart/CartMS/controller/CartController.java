package com.ecommerce.cart.CartMS.controller;

import com.ecommerce.cart.CartMS.exception.ProductNotfoundException;
import com.ecommerce.cart.CartMS.model.Cart;
import com.ecommerce.cart.CartMS.model.CartItem;
import com.ecommerce.cart.CartMS.model.CustomResponse;
import com.ecommerce.cart.CartMS.model.Product;
import com.ecommerce.cart.CartMS.repository.CartRepository;
import com.ecommerce.cart.CartMS.service.CartService;
import com.ecommerce.cart.CartMS.service.PlaceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PlaceOrderService placeOrderService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // this end point will add item to cart and will give error when product is null or price is less
    // then  zero or quantity is less than zero
    @RequestMapping(value="/addToCart",method = RequestMethod.POST)
    public ResponseEntity<CustomResponse> addItemToCart(@RequestBody @Valid Product product) {
        if(product ==null || product.getPrice()<=0 || product.getQuantity()<=0)
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"INVALID PRODUCT",null));
        Cart cart = cartService.addToCart(product);
        if(cart==null)
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"PRODUCT NOT ADDED",null));
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200,"PRODUCT SUCCESSFULLY ADDED",cart));
    }

    // This end point is used for placing buy order
    @RequestMapping(value="/buy",method = RequestMethod.POST)
    public ResponseEntity<CustomResponse> buy(@RequestBody @Valid Product product) {
        List<CartItem> productList = new ArrayList<>();
        if(product ==null || product.getPrice()<=0 || product.getQuantity()<=0 || product.getProductName()==null || product.getPrice()==null || product.getQuantity()==null)
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"INVALID PRODUCT",null));

        product.setQuantity(1);
        CartItem cartItem =new CartItem();
        cartItem.setItem(product);
        productList.add(cartItem);
        Cart cart = new Cart();
        cart.setAmountPayable(product.getPrice());
        cart.setAccountId("");
        cart.setId("");
        cart.setCartItemList(productList);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200,"PRODUCT SUCCESSFULLY ADDED",cart));
    }

    // This end point is used to update the quantity of a product present in the cart
    @RequestMapping(value="/updateQuantity",method = RequestMethod.POST)
    public ResponseEntity<CustomResponse> updateCartItem(@RequestBody @Valid Product product) {
        if(product ==null || product.getPrice()<=0 || product.getQuantity()<=0 || product.getProductName()==null || product.getPrice()==null || product.getQuantity()==null)
        {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"INVALID PRODUCT",null));
        }
        try {
            Cart cart = cartService.changeQuantity(product);
            if(cart==null)
            {
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"PRODUCT QUANTITY NOT UPDATED",null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200,"QUANTITY SUCCESSFULLY ADDED",product));
        }
        catch (ProductNotfoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404, "PRODUCT NOT FOUND ID : " + product.getProductId(), null));
        }
    }

    // This end point is used to fetch all the items present in the cart
    @RequestMapping(value="",method = RequestMethod.GET)
    public ResponseEntity<CustomResponse> getAllCartItems() {
        Cart cart = cartService.fetchAllCartItems();
        if(cart==null)
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"CART LIST NOT FOUND",null));
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200,"CART LIST",cart));
    }

    // This end point is used to remove a item present in the cart
    @RequestMapping(value="/removeCartItem/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<CustomResponse> removeCartItem(@PathVariable @Valid String id) {
        try{
            Cart cart = cartService.removeCartItem(id);
            if(cart==null)
            {
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"PRODUCT NOT REMOVED!!",null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(200,"PRODUCT REMOVED SUCCESSFULLY",cart));
        }
        catch(ProductNotfoundException e){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(404,"PRODUCT NOT FOUND ID : "+ id,null));
        }

    }

    // This end point is used to empty the cart
    @RequestMapping(value="/emptyCart",method = RequestMethod.DELETE)
    public void removeCartItem() { cartRepository.deleteAll(); }

}