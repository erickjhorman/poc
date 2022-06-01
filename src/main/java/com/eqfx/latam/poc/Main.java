package com.eqfx.latam.poc;

import com.eqfx.latam.poc.transforms.CombineProductsFn;
import com.eqfx.latam.poc.transforms.FilterProductsFn;
import com.eqfx.latam.poc.transforms.GroupProductsFn;
import com.eqfx.latam.poc.transforms.ParseProductsFn;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.ParDo;

public class Main {
    public static void main(String[] args) {
        PipelineOptionsFactory.register(ProductAvgPriceOptions.class);
        ProductAvgPriceOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(ProductAvgPriceOptions.class);

        Pipeline pipeline = Pipeline.create(options);

        pipeline.apply("read file", TextIO.read().from(options.getInput()))
                .apply("parse products", ParDo.of(new ParseProductsFn()))
//                .apply(Log.ofElements("Parsed: "))
                .apply("group products", ParDo.of(new GroupProductsFn()))
//                .apply(Log.ofElements("Grouped: "))
                .apply("combine products", Combine.perKey(new CombineProductsFn()))
//                .apply(Log.ofElements("Combined: "))
                .apply("filter products", ParDo.of(new FilterProductsFn(options.getThreshold())))
//                .apply(Log.ofElements("Filtered: "))
                .apply("write file", TextIO.write().to(options.getOutput()).withSuffix(".txt"));

        pipeline.run();
    }
}


