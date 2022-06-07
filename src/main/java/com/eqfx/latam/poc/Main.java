package com.eqfx.latam.poc;

import com.eqfx.latam.poc.csv.CSVRecordMap;
import com.eqfx.latam.poc.csv.CsvIO;
import com.eqfx.latam.poc.csv.CsvParsers;
import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.scenario.ProductAvgPrice;
import com.eqfx.latam.poc.scenario.SalesByQuarter;
import com.eqfx.latam.poc.scenario.ScenarioTwoBiConsumer;
import com.eqfx.latam.poc.model.Event;
import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO.Read;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
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
        Read<PubsubMessage> messageRead = PubsubIO.readMessagesWithAttributes().fromSubscription("projects/cedar-router-268801/subscriptions/poc_pubsub-sub");

        PCollection<PubsubMessage> pubSubMessage = pipeline.apply("Read from subscription", messageRead); //returning a PCollection from Subscription
        Coder<PubsubMessage> coder = pubSubMessage.getCoder(); //add coder to the PCollection
        pubSubMessage.setCoder(coder);

        PCollection<Event> apply = pubSubMessage.apply("Parse to Event model", ParDo.of(new convertToEvent()));

        PCollection<TableRow> tableRowCollections = apply.apply(ParDo.of(new ConvertorStringBq()));
        tableRowCollections.apply(BigQueryIO.writeTableRows().to("cedar-router-268801.streaming.pubsubtest")  //name of the table in bigQuery
                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER) // avoid recreating the table
                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));   //append new data into an existing table

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
                new ScenarioTwoBiConsumer().accept(pipeline, options.as(SalesByQuarter.Options.class));
                break;
        }
        pipeline.run();
    }

    private static class convertToEvent extends DoFn<PubsubMessage, Event> {
        @ProcessElement
        public void processing(@Element PubsubMessage elem, ProcessContext pc) {
            Event event = new Event();
            event.setId(elem.getAttribute("id"));
            event.setEventType(elem.getAttribute("eventType"));
            event.setBucket(elem.getAttribute("bucket"));
            event.setFilename(elem.getAttribute("objectId"));
            pc.output(event);
            }
        }

        private static class ConvertorStringBq extends DoFn<Event, TableRow> {
        @ProcessElement
        public void processing(@Element Event elem, ProcessContext pc) {
            TableRow tableRow = new TableRow();
            tableRow.set("message", elem.getFilename());
            tableRow.set("messageid", elem.getId() + ":" + pc.timestamp().toString());
            tableRow.set("messageprocessingtime", pc.timestamp().toString());
            pc.output(tableRow);
        }
    }

    /* Example to read a string file from a PUB/SUB subscription and save it in BigQuery
    * */
    private static void  readStringFromTextExample(Pipeline pipeline, PipelineOptions options) {
        PubsubIO.Read<String> stringRead = PubsubIO.readStrings().fromSubscription("projects/cedar-router-268801/subscriptions/poc_pubsub-sub");
        PCollection<String> PCollectionMessage = pipeline.apply("read from subscription", stringRead);

        PCollection<TableRow> tableRowCollections = PCollectionMessage.apply(ParDo.of(new ConvertorMessageBq()));

        tableRowCollections.apply(BigQueryIO.writeTableRows().to("cedar-router-268801.streaming.pubsubtest")  //name of the table in bigQuery
                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER) // avoid recreating the table
                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND));   //append new data into an existing table
    }

    private static class ConvertorMessageBq extends DoFn<String, TableRow> {
        @ProcessElement
        public void processing(@Element String elem, ProcessContext pc) {
            TableRow tableRow = new TableRow();
            tableRow.set("messageid", elem + ":" + pc.timestamp().toString());
            tableRow.set("message", elem);
            tableRow.set("messageprocessingtime", pc.timestamp().toString());
            pc.output(tableRow);
        }
    }
}
