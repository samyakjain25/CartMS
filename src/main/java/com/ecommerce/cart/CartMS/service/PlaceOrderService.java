package com.ecommerce.cart.CartMS.service;

import com.ecommerce.cart.CartMS.model.Cart;
import com.ecommerce.cart.CartMS.model.CartItem;
import com.ecommerce.cart.CartMS.model.CustomResponse;
import com.ecommerce.cart.CartMS.model.Product;
import com.ecommerce.cart.CartMS.repository.CartRepository;
import com.ecommerce.cart.CartMS.repository.CartRepositoryCustom;
import com.fasterxml.jackson.databind.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaceOrderService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartRepositoryCustom cartRepositoryCustom;

    public ResponseEntity<CustomResponse> validatePriceQuantity(Cart cart) throws Exception {
        String accountId = "acc01";
        final String uri = "http://10.10.212.75:8080/getProductsById";
        RestTemplate restTemplate = new RestTemplate();

        Cart cart1 = cartRepositoryCustom.fetchCartByAccountId(accountId);

        cart.setId(cart1.getId());
        List<String> productIds = new ArrayList<>();
        List<CartItem> cartItems = cart.getCartItemList();
        for (CartItem item: cartItems) {
            System.out.println(item.getItem().getProductId());
            productIds.add(item.getItem().getProductId());
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List> entity = new HttpEntity<>(productIds,headers);

        String productList = restTemplate.postForObject(uri,entity,String.class);

        JSONObject obj = new JSONObject(productList);
        JSONArray arr = obj.getJSONArray("responseData");

        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < arr.length(); i++) {
            Product prod = mapper.readValue(arr.getJSONObject(i).toString(), Product.class);
            System.out.println("Product = " + prod);
            System.out.println("ID = "+prod.getProductName());
            System.out.println("List ID  = "+productIds.get(i));
            if (!prod.getProductId().equals(productIds.get(i)))
                return ResponseEntity.status(200).body(new CustomResponse(403, "Product Id doesn't match", null));
            if(prod.getQuantity() < cartItems.get(i).getItem().getQuantity() && !prod.getPrice().equals(cartItems.get(i).getItem().getPrice()))
            {
                double amt = cart.getAmountPayable() - cartItems.get(i).getItem().getPrice()*cartItems.get(i).getItem().getQuantity();
                cartItems.get(i).getItem().setPrice(prod.getPrice());
                cartItems.get(i).getItem().setQuantity(prod.getQuantity());
                cart.setAmountPayable(amt + prod.getPrice()* prod.getQuantity());

                cartItems.get(i).getItem().setQuantity(prod.getQuantity());
                cartItems.get(i).getItem().setPrice(prod.getPrice());
                cartRepository.save(cart);
                return ResponseEntity.status(200).body(new CustomResponse(403, "Quantity and Price has been updated", null));
            }
            if (prod.getQuantity() < cartItems.get(i).getItem().getQuantity()) {
                double amt = cart.getAmountPayable() - cartItems.get(i).getItem().getPrice()*cartItems.get(i).getItem().getQuantity();
                cartItems.get(i).getItem().setQuantity(prod.getQuantity());
                cart.setAmountPayable(amt + prod.getPrice()* prod.getQuantity());

                cartItems.get(i).getItem().setQuantity(prod.getQuantity());
                cartRepository.save(cart);
                return ResponseEntity.status(200).body(new CustomResponse(403, "Quantity requested not available", null));
            }

            if (!prod.getPrice().equals(cartItems.get(i).getItem().getPrice())) {
                double amt = cart.getAmountPayable() - cartItems.get(i).getItem().getPrice()*cartItems.get(i).getItem().getQuantity();
                cartItems.get(i).getItem().setPrice(prod.getPrice());
//                System.out.println("Amount Payable Before = "+cart.getAmountPayable());
//                System.out.println("AMT = " + amt);
//                System.out.println("Price FROM GOURAV = "+prod.getPrice() +" Quantity = "+cartItems.get(i).getItem().getQuantity());
//                System.out.println("FROM === " + prod.getPrice()* prod.getQuantity());
                cart.setAmountPayable(amt + prod.getPrice()* cartItems.get(i).getItem().getQuantity());
//                System.out.println("Amount Payable After = "+cart.getAmountPayable());
                cartRepository.save(cart);
//                System.out.println("!!!!!!!!!!!!!!!!!!!!!!   PRICE UPDATED = " + prod.getPrice());
                return ResponseEntity.status(200).body(new CustomResponse(403, "Price has been changed", null));
            }
        }
        System.out.println("HERE");
        for (CartItem c:cart.getCartItemList()) {
            System.out.println("Name = "+ c.getItem().getProductName() + " Quantity = "+ c.getItem().getQuantity());
        }
        cart = cartRepository.save(cart);
//        System.out.println(cart.toString());
        return ResponseEntity.status(200).body(new CustomResponse(200,"Price and Quantity Validated successfully",cart));

    }
}
