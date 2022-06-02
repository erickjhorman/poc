package com.eqfx.latam.poc.scenario;

import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.model.ProductResult;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class ProductAvgPriceTest {

    @Rule
    public final transient TestPipeline pipeline = TestPipeline.create();

    @Test
    public void apply() {
        ProductAvgPrice.Options options = TestPipeline.testingPipelineOptions().as(ProductAvgPrice.Options.class);

        PCollection<Product> products = pipeline.apply("Create products", Create.of(
                new Product(1, "Product 1", BigDecimal.valueOf(100.0)),
                new Product(1, "Product 1", BigDecimal.valueOf(300.0)),
                new Product(1, "Product 1", BigDecimal.valueOf(500.0)),
                new Product(1, "Product 1", BigDecimal.valueOf(700.0)),
                new Product(2, "Product 2", BigDecimal.valueOf(1000.5)),
                new Product(2, "Product 2", BigDecimal.valueOf(3000.5)),
                new Product(2, "Product 3", BigDecimal.valueOf(5000.5)),
                new Product(2, "Product 3", BigDecimal.valueOf(7000.5))));

        PCollection<ProductResult> results = ProductAvgPrice.apply(products, options);

        PAssert.that(results).containsInAnyOrder(List.of(
                new ProductResult(2, "Product 2", BigDecimal.valueOf(2000.5)),
                new ProductResult(2, "Product 3", BigDecimal.valueOf(6000.5))
        ));

        pipeline.run();
    }
}