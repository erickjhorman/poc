package com.eqfx.latam.poc;

import com.eqfx.latam.poc.csv.CSVRecordMap;
import com.eqfx.latam.poc.csv.CsvParsers;
import com.eqfx.latam.poc.model.Event;
import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.scenario.GcpStorageCsvReaderFn;
import com.eqfx.latam.poc.scenario.ProductAvgPrice;
import com.eqfx.latam.poc.scenario.SalesByQuarter;
import com.eqfx.latam.poc.scenario.ScenarioTwoTransformer;
import com.eqfx.latam.poc.util.Log;
import com.google.api.services.bigquery.model.TableRow;
import lombok.RequiredArgsConstructor;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.FlatMapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.commons.csv.CSVFormat;
import org.joda.time.Duration;

import java.io.Serializable;
import java.util.Objects;

import static com.eqfx.latam.poc.ScenarioOptions.Scenario;
import static com.eqfx.latam.poc.csv.util.CsvConstants.DELIMITER_SCENARIO_2;
import static com.eqfx.latam.poc.csv.util.CsvConstants.HEADERS_SCENARIO_2;

public class Main {
    public static final String OBJECT_FINALIZE = "OBJECT_FINALIZE";

    public static void main(String[] args) {
        PipelineOptionsFactory.register(SalesByQuarter.Options.class);
        PipelineOptionsFactory.register(ProductAvgPrice.Options.class);

        ScenarioOptions options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(ScenarioOptions.class);

        Pipeline pipeline = Pipeline.create(options);

        String topicId = "projects/cedar-router-268801/subscriptions/poc_pubsub-sub";

        PCollection<FileUploadedEvent> pCollection = pipeline.apply("Read from subscription", PubsubIO.readMessagesWithAttributes().fromSubscription(topicId))
                .apply("Filter event type", Filter.by(e -> {
                    String eventType = e.getAttribute("eventType");
                    return OBJECT_FINALIZE.equals(eventType);
                }))
                .apply("Map to event", ParDo.of(new FileUploadedTransformer()))
                .apply("Applying windowing", Window.into(FixedWindows.of(Duration.standardSeconds(60))));

        ExecuteScenario.apply(options, pCollection);

        pipeline.run();
    }

    @RequiredArgsConstructor
    public static class FileUploadedEvent implements Serializable {
        public final String filename;
        public final String bucket;
        public final Scenario scenario;
    }

    private static class FileUploadedTransformer extends DoFn<PubsubMessage, FileUploadedEvent> {
        @ProcessElement
        public void processElement(@Element PubsubMessage message, OutputReceiver<FileUploadedEvent> outputReceiver) {
            String bucket = message.getAttribute("bucketId");
            String filename = Objects.requireNonNull(message.getAttribute("objectId"));
            Scenario scenario = Scenario.valueOf(filename.substring(0, 3));
            outputReceiver.output(new FileUploadedEvent(filename, bucket, scenario));
        }
    }

    private interface ExecuteScenario {
        static void apply(ScenarioOptions options, PCollection<FileUploadedEvent> pCollection) {
            pCollection
                    .apply("Filter Scenario Two",Filter.by(e->e.scenario.equals(Scenario.TWO)))
                    .apply("Map To CSV RECORD",FlatMapElements.into(TypeDescriptor.of(CSVRecordMap.class))
                    .via(new GcpStorageCsvReaderFn(CSVFormat.Builder.create()
                            .setHeader(Objects.requireNonNull(HEADERS_SCENARIO_2))
                            .setDelimiter(DELIMITER_SCENARIO_2)
                            .setSkipHeaderRecord(true)
                            .setNullString("NULL")
                            .build())))
                    .apply(new ScenarioTwoTransformer(options.as(SalesByQuarter.Options.class)));


            PCollection<Product> products = pCollection
                    .apply("Filter Scenario One", Filter.by(e -> e.scenario.equals(Scenario.ONE)))
                    .apply("Map To CSV RECORD", FlatMapElements.into(TypeDescriptor.of(CSVRecordMap.class))
                            .via(new GcpStorageCsvReaderFn(CSVFormat.Builder.create()
                                    .setHeader("ProductID", "ProductName", "ProductNumber", "MakeFlag", "FinishedGoodsFlag", "Color",
                                            "SafetyStockLevel", "ReorderPoint", "StandardCost", "ListPrice", "Size",
                                            "SizeUnitMeasureCode", "WeightUnitMeasureCode", "Weight", "DaysToManufacture",
                                            "ProductLine", "Class", "Style", "ProductSubcategoryID", "ProductModelID",
                                            "SellStartDate", "SellEndDate", "DiscontinuedDate", "SalesOrderDetailID",
                                            "CarrierTrackingNumber", "OrderQty", "SpecialOfferID", "UnitPrice", "UnitPriceDiscount",
                                            "LineTotal")
                                    .setDelimiter(';')
                                    .setSkipHeaderRecord(true)
                                    .setNullString("NULL")
                                    .build())))

                    .apply("Parse to Product", CsvParsers.products());

            PCollection<ProductAvgPrice.Result> result = ProductAvgPrice
                            .apply(options.as(ProductAvgPrice.Options.class), products).apply(Log.ofElements());

            PCollection<TableRow> tableRowCollections = result.apply(ParDo.of(new ConvertorStringBq()));
            tableRowCollections.apply(BigQueryIO.writeTableRows().to("cedar-router-268801.streaming.pubsubtest")  //name of the table in bigQuery
                    .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER) // avoid recreating the table
                    .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));   //append new data into an existing table

           /* result.apply("Save to AVRO",
                            AvroIO.write(ProductAvgPrice.Result.class)
                                    .withWindowedWrites()
                                    .to(options.getTargetFile())
                                    .withoutSharding()
                                    .withSuffix(".avro"));*/
            result.apply(Log.ofElements());
        }
    }

    private static class ConvertorStringBq extends DoFn<ProductAvgPrice.Result, TableRow> {
        @ProcessElement
        public void processing(@Element ProductAvgPrice.Result elem, ProcessContext pc) {
            TableRow tableRow = new TableRow();
            tableRow.set("message", elem.getName());
            tableRow.set("messageid", elem.getId() + ":" + pc.timestamp().toString());
            tableRow.set("messageprocessingtime", pc.timestamp().toString());
            pc.output(tableRow);
        }
    }
}

