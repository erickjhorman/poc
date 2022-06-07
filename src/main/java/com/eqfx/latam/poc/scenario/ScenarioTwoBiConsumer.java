package com.eqfx.latam.poc.scenario;

import com.eqfx.latam.poc.csv.CSVRecordMap;
import com.eqfx.latam.poc.csv.CsvIO;
import com.eqfx.latam.poc.csv.CsvParsers;
import com.eqfx.latam.poc.model.SaleOrder;
import com.eqfx.latam.poc.model.SalesByQuarterWrapper;
import lombok.NoArgsConstructor;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.values.PCollection;

import java.util.function.BiConsumer;

import static com.eqfx.latam.poc.csv.util.CsvConstants.DELIMITER_SCENARIO_2;
import static com.eqfx.latam.poc.csv.util.CsvConstants.HEADERS_SCENARIO_2;

@NoArgsConstructor
public class ScenarioTwoBiConsumer implements BiConsumer<Pipeline, SalesByQuarterWrapper> {

    @Override
    public void accept(Pipeline pipeline, SalesByQuarterWrapper salesByQuarterWrappers) {
        PCollection<CSVRecordMap> csvRecordMap = pipeline.apply("Reading from csv",
                CsvIO.read(salesByQuarterWrappers.getSourceFile())
                        .withDelimiter(DELIMITER_SCENARIO_2)
                        .withHeaders(HEADERS_SCENARIO_2)
                        .build()
        );
        PCollection<SaleOrder> csvMapped = csvRecordMap.apply("Parse to Sale", CsvParsers.saleOrders());
        PCollection<SalesByQuarter.Result> result = SalesByQuarter.apply(salesByQuarterWrappers.getYear(), csvMapped);
        result.apply("Save to avro", AvroIO.write(SalesByQuarter.Result.class)
                .to(salesByQuarterWrappers.getTargetFile())
                .withSuffix(".avro"));
    }
}
