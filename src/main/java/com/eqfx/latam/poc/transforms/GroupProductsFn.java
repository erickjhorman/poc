package com.eqfx.latam.poc.transforms;


import com.eqfx.latam.poc.model.Product;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import java.math.BigDecimal;

public class GroupProductsFn extends DoFn<Product, KV<KV<Integer, String>, BigDecimal>> {
    @ProcessElement
    public void processElement(@Element Product product, OutputReceiver<KV<KV<Integer, String>, BigDecimal>> outputReceiver) {
        KV<Integer, String> key = KV.of(product.getProductID(), product.getProductName());
        BigDecimal value = product.getUnitPrice();

        outputReceiver.output(KV.of(key, value));
    }
}
