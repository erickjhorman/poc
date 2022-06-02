package com.eqfx.latam.poc;

import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.model.QuarterProduct;
import com.eqfx.latam.poc.transforms.TransformHashToQuarterProduct;
import com.eqfx.latam.poc.transforms.TransformToQuarterHashAndMoney;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.PCollection;

public class Main {
    private static final int FINAL_END_YEAR = 2014;
    private static final int FINAL_START_YEAR = 2013;

    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();
        Pipeline pipeline = Pipeline.create(options);
        PCollection<Product> products = pipeline.apply(Create.of(new Product()));
        applyTransformOfScenario2(products);
        pipeline.run().waitUntilFinish();
    }

    static PCollection<QuarterProduct> applyTransformOfScenario2(PCollection<Product> products) {
        return products.apply("Filter on Years", Filter.by(prod -> prod.getSellEndDate().getYear()<=FINAL_END_YEAR
                        && prod.getSellEndDate().getYear()>=FINAL_START_YEAR))
                .apply("Breakdown product data", ParDo.of(new TransformToQuarterHashAndMoney()))
                .apply("Combine money", Sum.doublesPerKey())
                .apply("Convert into quarter products", ParDo.of(new TransformHashToQuarterProduct()));
    }

}
