package com.eqfx.latam.poc.helper;

import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.model.QuarterProduct;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.time.LocalDateTime;

public class GenerateMockData {

    /**
     * Creates a mocked object of {@link Product}.
     *
     * @param id       of the product category.
     * @param subId    of the product subcategory.
     * @param year     of the product, month and day will be the first day and the first month of the year. i.e. year/01/01 00:00:00
     * @param quantity of the product.
     * @param value    of the product (without adding quantity).
     * @param name     of the product.
     * @return a new {@link Product} object with mocked data.
     */
    public static Product createProductMockObject(int id, int subId, int year, int quantity, double value, String name) {
        return createProductMockObject(id, subId, year, 1, quantity, value, name);
    }

    /**
     * Creates a mocked object of {@link Product} on specific month.
     *
     * @param id       of the product category.
     * @param subId    of the product subcategory.
     * @param year     of the product, day will be the first day of month. i.e. year/MM/01 00:00:00
     * @param month    of the product, day will be the first day of month. i.e. year/MM/01 00:00:00
     * @param quantity of the product.
     * @param value    of the product (without adding quantity).
     * @param name     of the product.
     * @return a new {@link Product} object with mocked data.
     */
    public static Product createProductMockObject(int id, int subId, int year, int month, int quantity, double value, String name) {
        Product mockProduct = new Product();
        mockProduct.setOrderQty(quantity);
        mockProduct.setProductCategoryId(id);
        mockProduct.setProductSubCategoryId(subId);
        mockProduct.setLineTotal(value);
        mockProduct.setName(name);
        mockProduct.setSellEndDate(LocalDateTime.of(year, month, 1, 0, 0));
        return mockProduct;
    }


    /**
     * Creates a mocked object of {@link QuarterProduct}.
     *
     * @param id    of the product category.
     * @param subId of the product subcategory.
     * @param year  of the product.
     * @param value of the product. (Adding quantity if compared to {@link Product})
     * @return a new {@link QuarterProduct} object with mocked data.
     */
    public static QuarterProduct createQuarterProductMockObject(int id, int subId, int year, int quarter, double value) {
        QuarterProduct mockQuarterProduct = new QuarterProduct();
        mockQuarterProduct.setTotalSold(Money.of(CurrencyUnit.USD, value));
        mockQuarterProduct.setYear(year);
        mockQuarterProduct.setProductCategoryId(id);
        mockQuarterProduct.setProductSubCategoryId(subId);
        mockQuarterProduct.setQuarter(quarter);
        return mockQuarterProduct;
    }
}
