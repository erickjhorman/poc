package com.eqfx.latam.poc.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ResultProduct implements Serializable {
    private final Integer productID;
    private final String productName;
    private final BigDecimal avgUnitPrice;
}

