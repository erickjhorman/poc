package com.eqfx.latam.poc.csv;

import com.eqfx.latam.poc.model.SaleOrder;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvParsers {

    private static final String NULL = "NULL";

    public static CsvParser<SaleOrder> saleOrders(){
        return CsvParser.of(SaleOrder.class).using(input -> {
            String category = input.get("ProductCategoryID");
            String subCategory = input.get("ProductSubcategoryID");
            String dateValue = input.get("SellEndDate");
            LocalDate date = NULL.equals(dateValue) ? null:LocalDateTime.parse(dateValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).toLocalDate();
            Money unitPrice = Money.of(CurrencyUnit.USD, Double.parseDouble(replaceDoubleValue(input.get("UnitPrice"))), RoundingMode.DOWN);
            Integer qty = Integer.valueOf(input.get("OrderQty"));
            return new SaleOrder(category,subCategory, date, unitPrice, qty);
        });
    }

    private static String replaceDoubleValue(String unitPrice) {
        return NULL.equals(unitPrice) ? "0" : unitPrice.replace(',','.');
    }
}
