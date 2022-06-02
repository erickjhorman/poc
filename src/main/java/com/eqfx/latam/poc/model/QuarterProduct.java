package com.eqfx.latam.poc.model;

import java.io.Serializable;
import java.util.Objects;

public class QuarterProduct implements Serializable, Cloneable{

    private int productCategoryId;
    private int productSubCategoryId;
    private double totalSold;
    private int year;

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

    public double getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(double totalSold) {
        this.totalSold = totalSold;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuarterProduct that = (QuarterProduct) o;
        return productCategoryId == that.productCategoryId && productSubCategoryId == that.productSubCategoryId &&
                Double.compare(that.totalSold, totalSold) == 0 && year == that.year;
    }

    @Override
    public String toString() {
        return "QuarterProduct{" +
                "productCategoryId=" + productCategoryId +
                ", productSubCategoryId=" + productSubCategoryId +
                ", totalSold=" + totalSold +
                ", year=" + year +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCategoryId, productSubCategoryId, totalSold, year);
    }

    @Override
    public QuarterProduct clone() {
        QuarterProduct cloned = new QuarterProduct();
        cloned.setYear(year);
        cloned.setTotalSold(totalSold);
        cloned.setProductSubCategoryId(productSubCategoryId);
        cloned.setProductCategoryId(productCategoryId);
        return cloned;
    }
}
