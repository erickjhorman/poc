package com.eqfx.latam.poc;

import com.eqfx.latam.poc.csv.CSVRecordMap;
import com.eqfx.latam.poc.csv.CsvIO;
import com.eqfx.latam.poc.csv.CsvParsers;
import com.eqfx.latam.poc.model.SaleOrder;
import com.eqfx.latam.poc.scenario.SalesByQuarter;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;

import static com.eqfx.latam.poc.csv.util.CsvConstants.DELIMITER_SCENARIO_2;
import static com.eqfx.latam.poc.csv.util.CsvConstants.HEADERS_SCENARIO_2;

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
                //TODO save to AVRO
                break;
            }
            case TWO: {
                PCollection<CSVRecordMap> csvRecordMap = pipeline.apply("Reading from csv",
                        CsvIO.read(options.getSourceFile())
                                .withDelimiter(DELIMITER_SCENARIO_2)
                                .withHeaders(HEADERS_SCENARIO_2)
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
