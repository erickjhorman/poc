package com.eqfx.latam.poc;

import com.eqfx.latam.poc.csv.CSVRecordMap;
import com.eqfx.latam.poc.csv.CsvIO;
import com.eqfx.latam.poc.csv.CsvParsers;
import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.model.SalesByQuarterWrapper;
import com.eqfx.latam.poc.pubsub.GMessageReceiver;
import com.eqfx.latam.poc.scenario.ProductAvgPrice;
import com.eqfx.latam.poc.scenario.SalesByQuarter;
import com.eqfx.latam.poc.scenario.ScenarioTwoBiConsumer;
import com.eqfx.latam.poc.model.Event;
import com.eqfx.latam.poc.util.Log;
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
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.List;

public class Main {

    static Pipeline pipeline;
    static ScenarioOptions options;

    public static final String OBJECT_FINALIZE = "OBJECT_FINALIZE"; /* it can also be added in an Enum class */
    //static private Log log = LogFactory.getLog(Main.class);
    public static void main(String[] args) {
        PipelineOptionsFactory.register(SalesByQuarter.Options.class);
        PipelineOptionsFactory.register(ProductAvgPrice.Options.class);

         options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(ScenarioOptions.class);

         pipeline = Pipeline.create(options);
        Read<PubsubMessage> messageRead = PubsubIO.readMessagesWithAttributes().fromSubscription("projects/cedar-router-268801/subscriptions/poc_pubsub-sub");

        PCollection<PubsubMessage> pubSubMessage = pipeline.apply("Read from subscription", messageRead); //returning a PCollection from Subscription
        Coder<PubsubMessage> coder = pubSubMessage.getCoder(); //add coder to the PCollection
        pubSubMessage.setCoder(coder);

        pubSubMessage.apply("Parse to Event model", ParDo.of(new convertToEvent()));

        pipeline.run().waitUntilFinish();
    }
    private static class convertToEvent extends DoFn<PubsubMessage, Event> {
        @ProcessElement
        public void processing(@Element PubsubMessage elem, ProcessContext pc) {
            String eventType = elem.getAttribute("eventType");
            if(OBJECT_FINALIZE.equals(eventType)) {
                //write pipeline logic and use filename variable to read the file from Pub/sub...
                //log.info("here running pipeline");
            String filename = elem.getAttribute("objectId");
            String sourceFile = String.format("gs://%s/%s", elem.getAttribute("bucketId"), elem.getAttribute("objectId"));
            String scenario = filename.substring(0, 3); //it comes TWO OR ONE
            System.out.println("here in DoFn");
            System.out.println("sourceFile " + sourceFile);
            System.out.println("scenario " + scenario);

            switch (scenario) {
                case "ONE": {

                    List<String> list = List.of("Colombia", "Francia", "Estados unidos", "Bolivia");
                    PCollection<String> apply = pipeline.apply(Create.of(list));
                    apply.apply(ParDo.of(new DoFn<String, String>() {
                        @ProcessElement
                        public void processElement(ProcessContext c) {
                            if (c.element().startsWith("C")) {
                                c.output(c.element());
                                System.out.println("here in countries");
                            }
                        }
                    }));

                    PCollection<CSVRecordMap> csvRecordMap = pipeline.apply("Reading from CSV",
                            CsvIO.read("gs://cedar-router-beam-poc-kms-2/ONECase_1_Raw_Data_E.csv") //remove sourceFile and scenario
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
                    ).apply(Log.ofElements());
                    //csvRecordMap.apply(Log.ofElements());
                    PCollection<Product> csvMapped = csvRecordMap.apply("Parse to Product",
                            CsvParsers.products());

                    PCollection<ProductAvgPrice.Result> result = ProductAvgPrice
                            .apply(options.as(ProductAvgPrice.Options.class), csvMapped);

                    System.out.println("before save to AVRO");
                    System.out.println("target" + options.getTargetFile());

                    result.apply("Save to AVRO",
                            AvroIO.write(ProductAvgPrice.Result.class)
                                    .to(options.getTargetFile())
                                    .withoutSharding()
                                    .withSuffix(".avro"));
                    System.out.println("After save to AVRO");
                    break;
                }
                case "TWO":
                    System.out.println("target" + options.getTargetFile());
                    SalesByQuarterWrapper salesByQuarterWrappers = SalesByQuarterWrapper.builder().sourceFile("gs://cedar-router-beam-poc-kms-2/TWOCase_2_Raw_Data_E.csv")
                            .targetFile(options.getTargetFile())
                            .year(Arrays.asList(2013, 2014))
                            .build();
                    new ScenarioTwoBiConsumer().accept(pipeline, salesByQuarterWrappers);
                    break;
            }
            }
        }
    }

   /* private static class convertToEvent extends DoFn<PubsubMessage, Event> {
        @ProcessElement
        public void processing(@Element PubsubMessage elem, ProcessContext pc) {
            Event event = new Event();
            event.setId(elem.getAttribute("id"));
            event.setEventType(elem.getAttribute("eventType"));
            event.setBucket(elem.getAttribute("bucket"));
            event.setFilename(elem.getAttribute("objectId"));
            pc.output(event);
            }
        }*/

   /*  private static class convertToEvent extends DoFn<PubsubMessage, Event> {
        @ProcessElement
        public void processing(@Element PubsubMessage elem, ProcessContext pc) {
            Event event = new Event();
            event.setId(elem.getAttribute("id"));
            event.setEventType(elem.getAttribute("eventType"));
            event.setBucket(elem.getAttribute("bucket"));
            event.setFilename(elem.getAttribute("objectId"));



            }
        }*/

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
