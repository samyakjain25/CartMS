package com.ecommerce.cart.CartMS.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @Id
    private String productId;
    private String productName;
    private Double price;
    private String imageUrl;
    private Integer quantity;
    private String brand;

    public Product(){
    }

    public Product(String productId, String productName, Double price, String imageUrl, Integer quantity, String brand) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
