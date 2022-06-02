package com.eqfx.latam.poc.transforms;

import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.model.helper.HashProduct;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

public class TransformToQuarterHashAndMoney extends DoFn<Product, KV<String, Double>> {

    @ProcessElement
    public void processElement(@Element Product product, OutputReceiver<KV<String, Double>> out) {
        out.output(KV.of(HashProduct.createHashOfProduct(product),
                product.getLineTotal()*product.getOrderQty()));
    }

}
