package com.ecommerce.cart.CartMS.repository;

import com.ecommerce.cart.CartMS.model.Cart;

public interface CartRepositoryCustom {
  public Cart fetchCartByAccountId(String accountId);
}
