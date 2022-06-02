package com.eqfx.latam.poc;

import com.eqfx.latam.poc.helper.GenerateMockData;
import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.model.QuarterProduct;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Rule;
import org.junit.Test;

public class MainTransformTest {

    @Rule
    public final transient TestPipeline testPipeline = TestPipeline.create();

    @Test
    /**
     * A transform is performed on 3 products and should return a Collection of 3 objects 2 of whom belong
     * to the same category and same year.
     */
    public void transformProductSuccessfullyOnTheSameYear() {

        PCollection<Product> products = testPipeline.apply("Create mock data", Create.of(
                GenerateMockData.createProductMockObject(1, 1, 2013, 2, 100.0, "Cat1.1"),
                GenerateMockData.createProductMockObject(1, 1, 2013, 1, 100.0, "Cat1.1"),
                GenerateMockData.createProductMockObject(1, 2, 2013, 3, 200.0, "Cat1.2")));

        PCollection<QuarterProduct> actualResult = Main.applyTransformOfScenario2(products);

        PAssert.that(actualResult)
                .containsInAnyOrder(GenerateMockData.createQuarterProductMockObject(1, 1, 2013, 1,300),
                        GenerateMockData.createQuarterProductMockObject(1, 2, 2013, 1, 600)
                );
        testPipeline.run().waitUntilFinish();

    }

    @Test
    /**
     * A transform is performed on 5 products on the same category and should return a Collection of 2 objects with
     * their correctly sum on their respectively year.
     */
    public void transformProductSuccessfullyOnDifferentYears() {
        PCollection<Product> products = testPipeline.apply("Create mock data", Create.of(
                GenerateMockData.createProductMockObject(1, 1, 2013, 2, 100.0, "Cat1.1"),
                GenerateMockData.createProductMockObject(1, 1, 2013, 1, 100.0, "Cat1.1"),
                GenerateMockData.createProductMockObject(1, 1, 2014, 3, 100.0, "Cat1.1"),
                GenerateMockData.createProductMockObject(1, 1, 2014, 3, 200.0, "Cat1.1"),
                GenerateMockData.createProductMockObject(1, 1, 2015, 3, 200.0, "Cat1.1")));

        PCollection<QuarterProduct> actualResult = Main.applyTransformOfScenario2(products);

        PAssert.that(actualResult)
                .containsInAnyOrder(GenerateMockData.createQuarterProductMockObject(1, 1, 1,2013, 300),
                        GenerateMockData.createQuarterProductMockObject(1, 1, 2014, 1,900)
                );
        testPipeline.run().waitUntilFinish();
    }


    @Test
    /**
     * A transform is performed on 6 products on different categories and subcategories, should return a Collection of 5
     * objects with their correctly sum on their respectively year and quarter.
     */
    public void transformProductSuccessfullyOnDifferentYearsAndCategories() {
        PCollection<Product> products = testPipeline.apply("Create mock data", Create.of(
                GenerateMockData.createProductMockObject(1, 1, 2013, 2, 2, 100.0, "Cat1.1"),
                GenerateMockData.createProductMockObject(1, 2, 2013, 1, 1, 100.0, "Cat1.1"),
                GenerateMockData.createProductMockObject(2, 1, 2014, 10,3, 100.0, "Cat2.1"),
                GenerateMockData.createProductMockObject(2, 1, 2014, 6,2, 200.0, "Cat2.1"),
                GenerateMockData.createProductMockObject(2, 2, 2013, 1, 200.0, "Cat2.2"),
                GenerateMockData.createProductMockObject(2, 2, 2014, 1, 200.0, "Cat2.2")));

        PCollection<QuarterProduct> actualResult = Main.applyTransformOfScenario2(products);

        PAssert.that(actualResult)
                .containsInAnyOrder(GenerateMockData.createQuarterProductMockObject(1, 1, 2013, 1,200),
                        GenerateMockData.createQuarterProductMockObject(1, 2, 2013, 1,100),
                        GenerateMockData.createQuarterProductMockObject(2, 1, 2014, 4,300),
                        GenerateMockData.createQuarterProductMockObject(2, 1, 2014, 2,400),
                        GenerateMockData.createQuarterProductMockObject(2, 2, 2013, 1,200),
                        GenerateMockData.createQuarterProductMockObject(2, 2, 2014, 1,200)
                );
        testPipeline.run().waitUntilFinish();
    }

    @Test
    /**
     * A transform is performed on 2 products on different categories and subcategories, should return an empty
     * Collection due to filtering on the years.
     */
    public void transformProductSuccessfullyWithNoOutputDueFilteringYear() {
        PCollection<Product> products = testPipeline.apply("Create mock data", Create.of(
                GenerateMockData.createProductMockObject(1, 1, 2010, 2, 100.0, "Cat1.1"),
                GenerateMockData.createProductMockObject(1, 2, 2015, 1, 100.0, "Cat1.1")));

        PCollection<QuarterProduct> actualResult = Main.applyTransformOfScenario2(products);

        PAssert.that(actualResult).empty();
        testPipeline.run().waitUntilFinish();
    }

}
