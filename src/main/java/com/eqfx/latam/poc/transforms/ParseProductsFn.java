package com.eqfx.latam.poc.transforms;

import com.eqfx.latam.poc.model.Product;
import org.apache.beam.sdk.transforms.DoFn;

import java.math.BigDecimal;

public class ParseProductsFn extends DoFn<String, Product> {
    @ProcessElement
    public void processElement(@Element String record, OutputReceiver<Product> out) {
        String[] strArr = record.split(";");

        int productID = Integer.parseInt(strArr[0]);
        String productName = strArr[1];
        BigDecimal unitPrice = BigDecimal.valueOf(Double.parseDouble(strArr[31].replace(',', '.')));

        Product product = new Product(productID, productName, unitPrice);
        out.output(product);
    }
}
