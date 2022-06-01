package com.eqfx.latam.poc;

import com.eqfx.latam.poc.scenario.ProductAvgPrice;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

public class Main {
    public static void main(String[] args) {
        PipelineOptionsFactory.register(ProductAvgPrice.Options.class);
        ProductAvgPrice.Options options = PipelineOptionsFactory
                .fromArgs(args)
                .withValidation()
                .as(ProductAvgPrice.Options.class);

        Pipeline pipeline = Pipeline.create(options);

        ProductAvgPrice.applyWithTextIO(pipeline, options);

        pipeline.run();
    }
}


