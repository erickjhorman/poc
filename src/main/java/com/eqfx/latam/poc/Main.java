package com.eqfx.latam.poc;

import com.eqfx.latam.poc.scenario.ProductAvgPrice;
import com.eqfx.latam.poc.scenario.SalesByQuarter;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;

public class Main {
    public static void main(String[] args) {
        PipelineOptionsFactory.register(SalesByQuarter.Options.class);
        PipelineOptionsFactory.register(SalesByQuarter.Options.class);

        ScenarioOptions options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(ScenarioOptions.class);

        Pipeline pipeline = Pipeline.create(options);

        switch (options.getScenario()){
            case ONE: {
                //TODO parse the csv model to the product model

                //TODO call the correct scenario e.j. ProductAvgPrice.apply(...)
                PCollection<ProductAvgPrice.Result> result = ProductAvgPrice
                        .apply( (ProductAvgPrice.Options) options, placeholder);
                //TODO save to AVRO
                result.apply("Save to AVRO",
                        AvroIO.write(ProductAvgPrice.Result.class)
                                .to(options.getTargetFile())
                                .withSuffix(".avro"));
                break;
            }
            case TWO: {
                PCollection<CSVRecordMap> csvRecordMap = pipeline.apply("Reading from csv",
                        CsvIO.read(options.getSourceFile())
                                .withDelimiter(';')
                                .withHeaders("ProductCategoryID", "ProductSubcategoryID", "SellEndDate","UnitPrice","OrderQty")
                                .build()
                );
                PCollection<SaleOrder> csvMapped = csvRecordMap.apply("Parse to Sale", CsvParsers.saleOrders());
                PCollection<SalesByQuarter.Result> result = SalesByQuarter.apply(options.as(SalesByQuarter.Options.class), csvMapped);
                result.apply("Save to avro", AvroIO.write(SalesByQuarter.Result.class)
                        .to(options.getTargetFile())
                        .withSuffix(".avro"));
                break;
            }
        }
        pipeline.run();
    }
}
