package com.eqfx.latam.poc.csv;

import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.model.SaleOrder;
import com.eqfx.latam.poc.scenario.ProductAvgPrice;
import org.apache.beam.repackaged.core.org.antlr.v4.runtime.misc.IntegerList;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvParsers {

    public static CsvParser<SaleOrder> saleOrders(){
        return CsvParser.of(SaleOrder.class).using(input -> {
            String category = input.get("ProductCategoryID");
            String subCategory = input.get("ProductSubcategoryID");
            LocalDate date = LocalDateTime.parse(input.get("SellEndDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")).toLocalDate();
            Money unitPrice = Money.of(CurrencyUnit.USD, Double.parseDouble(input.get("UnitPrice")));
            Integer qty = Integer.valueOf(input.get("OrderQty"));
            return new SaleOrder(category,subCategory, date, unitPrice, qty);
        });
    }

    public static CsvParser<Product> products(){
        return CsvParser.of(Product.class).using(input -> {
            Integer id = Integer.parseInt(input.get("ProductID"));
            String name = input.get("ProductName");
            Money unitPrice = Money.of(CurrencyUnit.USD, Double.parseDouble(
                    input.get("UnitPrice").replace(',','.')), RoundingMode.HALF_UP);
            return new Product(id, name, unitPrice);
        });
    }
}
