package com.eqfx.latam.poc.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A Product class which might be mapped from the CSV. Only key parameters were taken into consideration.
 */
@Data
public class Product implements Serializable {

    private String name;
    private int productCategoryId;
    private int productSubCategoryId;
    private double lineTotal;
    private int orderQty;
    private LocalDateTime sellEndDate;

}
