package com.eqfx.latam.poc.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A Product class which might be mapped from the CSV. Only key parameters were taken into consideration.
 */
public class Product implements Serializable {

    private String name;
    private int productCategoryId;
    private int productSubCategoryId;
    private double lineTotal;
    private int orderQty;
    private LocalDateTime sellEndDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public int getProductSubCategoryId() {
        return productSubCategoryId;
    }

    public void setProductSubCategoryId(int productSubCategoryId) {
        this.productSubCategoryId = productSubCategoryId;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public LocalDateTime getSellEndDate() {
        return sellEndDate;
    }

    public void setSellEndDate(LocalDateTime sellEndDate) {
        this.sellEndDate = sellEndDate;
    }

}
