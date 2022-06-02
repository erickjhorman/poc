package com.eqfx.latam.poc.transforms;

import com.eqfx.latam.poc.model.QuarterProduct;
import com.eqfx.latam.poc.model.helper.HashProduct;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class TransformHashToQuarterProduct extends DoFn<KV<String, Double>, QuarterProduct> {

    @ProcessElement
    public void processElement(@Element KV<String, Double> hash, OutputReceiver<QuarterProduct> out) {
        QuarterProduct quarterProduct = HashProduct.createQuarterProductBasedOnHash(hash.getKey());
        quarterProduct.setTotalSold(Money.of(CurrencyUnit.USD,hash.getValue()));
        out.output(quarterProduct);
    }
}
