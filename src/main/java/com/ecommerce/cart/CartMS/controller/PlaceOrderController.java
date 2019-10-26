package com.ecommerce.cart.CartMS.controller;

import com.ecommerce.cart.CartMS.model.Cart;
import com.ecommerce.cart.CartMS.model.CustomResponse;
import com.ecommerce.cart.CartMS.service.PlaceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/placeOrder")
public class PlaceOrderController {

    @Autowired
    PlaceOrderService placeOrderService;

    // before placing order, we validate the price of the product with the product catalogue
    // and check the quantity the user wants to buy with the inventory
    @RequestMapping(value="", method = RequestMethod.POST)
    public ResponseEntity<CustomResponse> validation(@RequestBody @Valid Cart cart) throws Exception {
        return placeOrderService.validatePriceQuantity(cart);
    }
}
