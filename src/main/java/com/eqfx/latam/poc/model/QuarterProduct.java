package com.eqfx.latam.poc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@ToString
public class QuarterProduct implements Serializable, Cloneable{

    private int productCategoryId;
    private int productSubCategoryId;
    private Money totalSold;
    private int year;
    private int quarter;

    @Override
    public QuarterProduct clone() {
        QuarterProduct cloned = new QuarterProduct();
        cloned.setYear(year);
        cloned.setTotalSold(Money.of(CurrencyUnit.USD, totalSold.getAmount()));
        cloned.setProductSubCategoryId(productSubCategoryId);
        cloned.setProductCategoryId(productCategoryId);
        return cloned;
    }
}
