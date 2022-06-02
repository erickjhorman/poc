package com.eqfx.latam.poc.scenario;

import com.eqfx.latam.poc.csv.CSVRecordMap;
import com.eqfx.latam.poc.csv.CsvIO;
import com.eqfx.latam.poc.csv.CsvParsers;
import com.eqfx.latam.poc.model.SaleOrder;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.values.PCollection;

import java.util.function.Consumer;

import static com.eqfx.latam.poc.csv.util.CsvConstants.DELIMITER_SCENARIO_2;
import static com.eqfx.latam.poc.csv.util.CsvConstants.HEADERS_SCENARIO_2;

public class ScenarioTwoConsumer implements Consumer<Pipeline> {

    SalesByQuarter.Options options;

    public ScenarioTwoConsumer(SalesByQuarter.Options options){
        this.options = options;
    }

    @Override
    public void accept(Pipeline pipeline){
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
    }
}
