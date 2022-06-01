package com.eqfx.latam.poc.transforms;

import com.eqfx.latam.poc.model.ResultProduct;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import java.math.BigDecimal;

public class FilterProductsFn extends DoFn<KV<KV<Integer, String>, BigDecimal>, String> {

    private final Double threshold;

    public FilterProductsFn(Double threshold) {
        this.threshold = threshold;
    }

    @ProcessElement
    public void processElement(@Element KV<KV<Integer, String>, BigDecimal> product, OutputReceiver<String> out) {
        if (product.getValue().compareTo(BigDecimal.valueOf(threshold)) > 0) {
            out.output(new ResultProduct(product.getKey().getKey(), product.getKey().getValue(), product.getValue()).toString());
        }
    }
}
