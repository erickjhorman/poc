package com.eqfx.latam.poc.scenario;

import com.eqfx.latam.poc.csv.CSVRecordMap;
import com.eqfx.latam.poc.csv.CsvParsers;
import com.eqfx.latam.poc.model.SaleOrder;
import lombok.RequiredArgsConstructor;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;

@RequiredArgsConstructor
public class ScenarioTwoTransformer extends PTransform<PCollection<CSVRecordMap>, PDone> {
    private final SalesByQuarter.Options options;

    @Override
    public PDone expand(PCollection<CSVRecordMap> csvRecordMap) {
        PCollection<SaleOrder> csvMapped = csvRecordMap.apply("Parse to Sale", CsvParsers.saleOrders());
        PCollection<SalesByQuarter.Result> result = SalesByQuarter.apply(options.getYears(), csvMapped);
        return result.apply("Save to avro", AvroIO.write(SalesByQuarter.Result.class)
                .to(options.getTargetFile())
                .withSuffix(".avro"));
    }
}
