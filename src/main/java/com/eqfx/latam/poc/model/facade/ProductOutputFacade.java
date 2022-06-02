package com.eqfx.latam.poc.model.facade;

import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.model.QuarterProduct;

public class ProductOutputFacade {

    public static QuarterProduct createQuarterProduct(Product product){
        QuarterProduct output = new QuarterProduct();
        output.setProductCategoryId(product.getProductCategoryId());
        output.setProductSubCategoryId(product.getProductSubCategoryId());
        output.setTotalSold(product.getLineTotal()*product.getOrderQty());
        output.setYear(product.getSellEndDate().getYear());
        return output;
    }
}
