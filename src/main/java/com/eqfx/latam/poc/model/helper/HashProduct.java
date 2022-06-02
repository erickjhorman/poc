package com.eqfx.latam.poc.model.helper;

import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.model.QuarterProduct;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;

public class HashProduct {

    public static String createHashOfProduct(Product product){
        LocalDateTime sellDate = product.getSellEndDate();
        return createCategoryKey(product.getProductCategoryId(),
                product.getProductSubCategoryId(),
                sellDate.getYear(),
                sellDate.get(IsoFields.QUARTER_OF_YEAR));
    }

    public static QuarterProduct createQuarterProductBasedOnHash(String hash){
        String[] values = hash.split(",");
        QuarterProduct quarterProduct = new QuarterProduct();
        quarterProduct.setProductCategoryId(Integer.parseInt(values[0]));
        quarterProduct.setProductSubCategoryId(Integer.parseInt(values[1]));
        quarterProduct.setYear(Integer.parseInt(values[2]));
        return quarterProduct;
    }

    private static String createCategoryKey(int productCategoryId, int productSubCategoryId, int year, int quarter) {
        return String.format("%d,%d,%d,%d", productCategoryId, productSubCategoryId, year, quarter);
    }
}
