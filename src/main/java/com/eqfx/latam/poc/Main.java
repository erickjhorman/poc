package com.eqfx.latam.poc;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.transforms.Combine.CombineFn;
import org.apache.beam.sdk.values.PCollectionList;

import static org.apache.beam.sdk.values.TypeDescriptors.*;

public class Main {
    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();
        Pipeline pipeline = Pipeline.create(options);

        PCollection<Product> products = pipeline.apply("Products",
                Create.of(
                        new Product(776, "Mountain-100 Black", 2024.994),
                        new Product(771,"Mountain-100 Silver", 2039.994),
                        new Product(776, "Mountain-100 Black", 3374.99),
                        new Product(923, "Touring Tire Tube", 4.99),
                        new Product(923, "Touring Tire Tube", 15.99)
                ));

        products.apply(Log.ofElements());

        PCollection<KV<Integer, Double>> output = applyTransformGrouping(products);

//        output.apply(Log.ofElements());

        PCollection<KV<Integer, Double>> output2 = applyTransformAvg(output);

//        output2.apply(Log.ofElements());

        PCollectionList<KV<Integer, Double>> output3 = applyTransformFilter(output2);

        output3.get(0).apply(Log.ofElements());

        pipeline.run();
    }

    static PCollection<KV<Integer, Double>> applyTransformGrouping(PCollection<Product> input){
        return input.apply(MapElements.into(kvs(integers(),doubles()))
                .via(person -> KV.of(person.getProductId(), person.getUnitPrice())));
    }

    static PCollection<KV<Integer, Double>> applyTransformAvg(PCollection<KV<Integer, Double>> input){
        return input.apply(Mean.perKey());
    }

    static PCollectionList<KV<Integer, Double>> applyTransformFilter(PCollection<KV<Integer, Double>> input){
        return input.apply(Partition.of(2,
                (Partition.PartitionFn<KV<Integer,Double>>) (number, numPartitions) ->{
                    if(number.getValue() > 1000){
                        return 0;
                    } else{
                        return 1;
                    }
                }));
    }
}
