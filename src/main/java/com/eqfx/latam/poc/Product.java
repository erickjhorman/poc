package com.eqfx.latam.poc;

import java.io.Serializable;

public class Product implements Serializable {
    private int productId;
    private String name;
    private String productNumber;
    private String makeFlag;
    private String finishedGoodsFlag;
    private String color;
    private int safetyStockLevel;
    private String reorderPoint;
    private String standardCost;
    private String listPrice;
    private String size;
    private String sizeUnitMeasureCode;
    private String weightUnitMeasureCode;
    private String weight;
    private String daysToManufacture;
    private String productLine;
    private String productClass;
    private String style;
    private String productSubcategoryId;
    private String productModelId;
    private String sellStartDate;
    private String sellEndDate;
    private String discontinuedDate;
    private String rowguid;
    private String modifiedDate;
    private String salesOrderId;
    private String salesOrderDetailId;
    private String carrierTrackingNumber;
    private int orderQty;
    private int productId2;
    private String specialOfferId;
    private double unitPrice;
    private double unitPriceDiscount;
    private String lineTotal;
    private String rowguid2;
    private String modifiedDate2;

    public Product(int productId, String name, double unitPrice) {
        this.productId = productId;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getMakeFlag() {
        return makeFlag;
    }

    public void setMakeFlag(String makeFlag) {
        this.makeFlag = makeFlag;
    }

    public String getFinishedGoodsFlag() {
        return finishedGoodsFlag;
    }

    public void setFinishedGoodsFlag(String finishedGoodsFlag) {
        this.finishedGoodsFlag = finishedGoodsFlag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSafetyStockLevel() {
        return safetyStockLevel;
    }

    public void setSafetyStockLevel(int safetyStockLevel) {
        this.safetyStockLevel = safetyStockLevel;
    }

    public String getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(String reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public String getStandardCost() {
        return standardCost;
    }

    public void setStandardCost(String standardCost) {
        this.standardCost = standardCost;
    }

    public String getListPrice() {
        return listPrice;
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSizeUnitMeasureCode() {
        return sizeUnitMeasureCode;
    }

    public void setSizeUnitMeasureCode(String sizeUnitMeasureCode) {
        this.sizeUnitMeasureCode = sizeUnitMeasureCode;
    }

    public String getWeightUnitMeasureCode() {
        return weightUnitMeasureCode;
    }

    public void setWeightUnitMeasureCode(String weightUnitMeasureCode) {
        this.weightUnitMeasureCode = weightUnitMeasureCode;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDaysToManufacture() {
        return daysToManufacture;
    }

    public void setDaysToManufacture(String daysToManufacture) {
        this.daysToManufacture = daysToManufacture;
    }

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getProductClass() {
        return productClass;
    }

    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getProductSubcategoryId() {
        return productSubcategoryId;
    }

    public void setProductSubcategoryId(String productSubcategoryId) {
        this.productSubcategoryId = productSubcategoryId;
    }

    public String getProductModelId() {
        return productModelId;
    }

    public void setProductModelId(String productModelId) {
        this.productModelId = productModelId;
    }

    public String getSellStartDate() {
        return sellStartDate;
    }

    public void setSellStartDate(String sellStartDate) {
        this.sellStartDate = sellStartDate;
    }

    public String getSellEndDate() {
        return sellEndDate;
    }

    public void setSellEndDate(String sellEndDate) {
        this.sellEndDate = sellEndDate;
    }

    public String getDiscontinuedDate() {
        return discontinuedDate;
    }

    public void setDiscontinuedDate(String discontinuedDate) {
        this.discontinuedDate = discontinuedDate;
    }

    public String getRowguid() {
        return rowguid;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(String salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public String getSalesOrderDetailId() {
        return salesOrderDetailId;
    }

    public void setSalesOrderDetailId(String salesOrderDetailId) {
        this.salesOrderDetailId = salesOrderDetailId;
    }

    public String getCarrierTrackingNumber() {
        return carrierTrackingNumber;
    }

    public void setCarrierTrackingNumber(String carrierTrackingNumber) {
        this.carrierTrackingNumber = carrierTrackingNumber;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public int getProductId2() {
        return productId2;
    }

    public void setProductId2(int productId2) {
        this.productId2 = productId2;
    }

    public String getSpecialOfferId() {
        return specialOfferId;
    }

    public void setSpecialOfferId(String specialOfferId) {
        this.specialOfferId = specialOfferId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getUnitPriceDiscount() {
        return unitPriceDiscount;
    }

    public void setUnitPriceDiscount(double unitPriceDiscount) {
        this.unitPriceDiscount = unitPriceDiscount;
    }

    public String getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(String lineTotal) {
        this.lineTotal = lineTotal;
    }

    public String getRowguid2() {
        return rowguid2;
    }

    public void setRowguid2(String rowguid2) {
        this.rowguid2 = rowguid2;
    }

    public String getModifiedDate2() {
        return modifiedDate2;
    }

    public void setModifiedDate2(String modifiedDate2) {
        this.modifiedDate2 = modifiedDate2;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
