package com.ecommerce.cart.CartMS.service;

import com.ecommerce.cart.CartMS.exception.ProductNotfoundException;
import com.ecommerce.cart.CartMS.model.Cart;
import com.ecommerce.cart.CartMS.model.CartItem;
import com.ecommerce.cart.CartMS.model.Product;
import com.ecommerce.cart.CartMS.repository.CartRepository;
import com.ecommerce.cart.CartMS.repository.CartRepositoryCustom;
import com.ecommerce.cart.CartMS.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

  CartRepository cartRepository;
  CartRepositoryCustom cartRepositoryCustom;
  ProductRepository productRepository;

  @Autowired
  public CartService(CartRepository cartRepository, CartRepositoryCustom cartRepositoryCustom, ProductRepository productRepository) {
    this.cartRepository = cartRepository;
    this.cartRepositoryCustom = cartRepositoryCustom;
    this.productRepository = productRepository;
  }

  public Cart addToCart(Product product) {
    String accountId = "acc01";
//        String accountId = addToCartRequest.getAccountId();
//        Product product = productRepository.findOne(addToCartRequest.getProductId());
//        if(product == null){
//            throw new ProductNotfoundException(addToCartRequest.getProductId());
//        }


    Cart cart = cartRepositoryCustom.fetchCartByAccountId(accountId);
    List<CartItem> cartItemList = cart.getCartItemList();
    Double amountToAdd = new Double(0);
    CartItem cartItemToUpdate = null;
    for (CartItem cartItem : cartItemList) {
      if (cartItem.getItem().getProductId().equals(product.getProductId())) {
        cartItemToUpdate = cartItem;
        int initialItemQuantity = cartItem.getItem().getQuantity();
        Double itemPrice = cartItem.getItem().getPrice();
        amountToAdd = itemPrice;
        cartItemToUpdate.getItem().setQuantity(initialItemQuantity + 1);
        cart.setAmountPayable(cart.getAmountPayable() + amountToAdd);
      }
    }
    if (cartItemToUpdate == null) {
      CartItem cartItem = new CartItem();
      cartItem.setItem(product);
      cartItem.getItem().setQuantity(1);
      cart.getCartItemList().add(cartItem);
      cart.setAmountPayable(cart.getAmountPayable() + cartItem.getItem().getQuantity() * cartItem.getItem().getPrice());
      Cart cartResponse = cartRepository.save(cart);
      return cartResponse;
    } else {
      Cart cartResponse = cartRepository.save(cart);
      return cartResponse;
    }

  }

  public Cart changeQuantity(Product product) {
    String accountId = "acc01";
    Cart cart = cartRepositoryCustom.fetchCartByAccountId(accountId);
    List<CartItem> cartItemList = cart.getCartItemList();
    Double amountToAdd = new Double(0);
    CartItem cartItemToUpdate = null;
    for (CartItem cartItem : cartItemList) {
      if (cartItem.getItem().getProductId().equals(product.getProductId())) {
        cartItemToUpdate = cartItem;
        int initialItemQuantity = cartItem.getItem().getQuantity();
        Double itemPrice = cartItem.getItem().getPrice();
        amountToAdd = (product.getQuantity() - initialItemQuantity) * itemPrice;
        cartItem.getItem().setQuantity(product.getQuantity());
      }
    }
    if (cartItemToUpdate == null) {
      throw new ProductNotfoundException(product.getProductId());
    }
    cart.setAmountPayable(cart.getAmountPayable() + amountToAdd);

    if (cartItemToUpdate != null && cartItemToUpdate.getItem().getQuantity() <= 0) {
      deleteItemFromCart(cart, cartItemToUpdate);
    }
    Cart updatedCart = cartRepository.save(cart);
    return updatedCart;
  }

  public Cart fetchAllCartItems() {
    String accountId = "acc01";
    Cart cart = cartRepositoryCustom.fetchCartByAccountId(accountId);
    return cart;
  }

  public Cart removeCartItem(String productId) {
    String accountId = "acc01";
    Cart cart = cartRepositoryCustom.fetchCartByAccountId(accountId);
    CartItem cartItemToRemove = null;
    for (CartItem cartItem : cart.getCartItemList()) {
      if (cartItem.getItem().getProductId().equals(productId)) {
        cartItemToRemove = cartItem;
      }
    }
    if (cartItemToRemove == null) {
      throw new ProductNotfoundException("");
    }
    cart.getCartItemList().remove(cartItemToRemove);
    cart.setAmountPayable(cart.getAmountPayable() - cartItemToRemove.getItem().getPrice() * cartItemToRemove.getItem().getQuantity());
    Cart removedItem = cartRepository.save(cart);
    return removedItem;
  }


  public void deleteItemFromCart(Cart cart, CartItem cartItem) {
    cart.getCartItemList().remove(cartItem);
  }
}
