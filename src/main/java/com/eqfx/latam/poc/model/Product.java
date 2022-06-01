package com.eqfx.latam.poc.model;

import lombok.*;
import org.joda.money.Money;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Product implements Serializable {
    private final Integer productID;
    private final String productName;
    private final BigDecimal unitPrice;
}

