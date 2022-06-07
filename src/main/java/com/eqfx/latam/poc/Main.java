package com.eqfx.latam.poc;

import com.eqfx.latam.poc.csv.CSVRecordMap;
import com.eqfx.latam.poc.csv.CsvIO;
import com.eqfx.latam.poc.csv.CsvParsers;
import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.scenario.ProductAvgPrice;
import com.eqfx.latam.poc.scenario.SalesByQuarter;
import com.eqfx.latam.poc.scenario.ScenarioTwoConsumer;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;

public class Main {
    public static void main(String[] args) {
        PipelineOptionsFactory.register(SalesByQuarter.Options.class);
        PipelineOptionsFactory.register(ProductAvgPrice.Options.class);

        ScenarioOptions options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(ScenarioOptions.class);

        Pipeline pipeline = Pipeline.create(options);

        switch (options.getScenario()){
            case ONE: {
                PCollection<CSVRecordMap> csvRecordMap = pipeline.apply("Reading from CSV",
                        CsvIO.read(options.getSourceFile())
                                .withDelimiter(';')
                                .withHeaders(	"ProductID", "ProductName",	"ProductNumber",	"MakeFlag",	"FinishedGoodsFlag",	"Color",
                                        "SafetyStockLevel",	"ReorderPoint",	"StandardCost",	"ListPrice",	"Size",
                                        "SizeUnitMeasureCode",	"WeightUnitMeasureCode",	"Weight",	"DaysToManufacture",
                                        "ProductLine",	"Class",	"Style",	"ProductSubcategoryID",	"ProductModelID",
                                        "SellStartDate",	"SellEndDate",	"DiscontinuedDate",	"SalesOrderDetailID",
                                        "CarrierTrackingNumber",	"OrderQty",	"SpecialOfferID",	"UnitPrice",	"UnitPriceDiscount",
                                        "LineTotal"
                                )
                                .build()
                );

                PCollection<Product> csvMapped = csvRecordMap.apply("Parse to Product",
                        CsvParsers.products());

                PCollection<ProductAvgPrice.Result> result = ProductAvgPrice
                        .apply(options.as(ProductAvgPrice.Options.class), csvMapped);


                result.apply("Save to AVRO",
                        AvroIO.write(ProductAvgPrice.Result.class)
                                .to(options.getTargetFile())
                                .withoutSharding()
                                .withSuffix(".avro"));
                break;
            }
            case TWO:
                new ScenarioTwoConsumer(options.as(SalesByQuarter.Options.class)).accept(pipeline);
                break;
        }
        pipeline.run();
    }
}
