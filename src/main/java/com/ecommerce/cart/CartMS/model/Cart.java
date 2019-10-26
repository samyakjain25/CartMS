package com.ecommerce.cart.CartMS.model;

import com.google.common.collect.Lists;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Cart {

    @Id
    String id;
    List<CartItem> cartItemList = Lists.newArrayList();
    String accountId;
    Double amountPayable = new Double(0);

    public Cart(){

    }

    public Cart(String id, List<CartItem> cartItemList, String accountId, Double amountPayable) {
        this.id = id;
        this.cartItemList = cartItemList;
        this.accountId = accountId;
        this.amountPayable = amountPayable;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Double getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(Double amountPayable) {
        this.amountPayable = amountPayable;
    }


}
