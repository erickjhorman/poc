package com.eqfx.latam.poc.scenario;

import com.eqfx.latam.poc.model.Product;
import com.eqfx.latam.poc.model.ProductResult;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface ProductAvgPrice {

    static void applyWithTextIO(Pipeline pipeline, Options options) {
        PCollection<Product> products = pipeline
                .apply("read file", TextIO.read().from(options.getInput()))
                .apply("parse products", ParDo.of(new ParseProductsFn()));

        PCollection<ProductResult> results = apply(products, options);

        results.apply("parse results", ParDo.of(new ParseResultsFn()))
                .apply("write file", TextIO.write().to(options.getOutput())
                        .withSuffix(".txt"));
    }

    static PCollection<ProductResult> apply(PCollection<Product> products, Options options) {
        return products.apply("group unitPrice by productID and productName", ParDo.of(new GroupProductsFn()))
                .apply("combine by productID and productName, getting avgUnitPrice", Combine.perKey(new CombineProductsFn()))
                .apply("filter results by avgUnitPrice", ParDo.of(new FilterProductsFn(options.getThreshold())));
    }

    interface Options extends PipelineOptions {
        @Description("Input csv file")
        @Default.String("datasets/input/Case_1_Raw_Data.csv")
        String getInput();

        void setInput(String input);

        @Description("Output txt file")
        @Default.String("datasets/output/result")
        String getOutput();

        void setOutput(String output);

        @Description("Lower threshold to filter products by avg unit price")
        @Default.Double(1000.0)
        Double getThreshold();

        void setThreshold(Double threshold);
    }

    class CombineProductsFn implements SerializableFunction<Iterable<BigDecimal>, BigDecimal> {
        @Override
        public BigDecimal apply(Iterable<BigDecimal> input) {
            List<BigDecimal> list = StreamSupport.stream(input.spliterator(), false)
                    .collect(Collectors.toList());

            return list.stream()
                    .reduce(BigDecimal::add)
                    .get()
                    .divide(BigDecimal.valueOf(list.size()), RoundingMode.DOWN);
        }
    }

    class FilterProductsFn extends DoFn<KV<KV<Integer, String>, BigDecimal>, ProductResult> {

        private final Double threshold;

        public FilterProductsFn(Double threshold) {
            this.threshold = threshold;
        }

        @ProcessElement
        public void processElement(@Element KV<KV<Integer, String>, BigDecimal> kvProduct, OutputReceiver<ProductResult> out) {
            if (kvProduct.getValue().compareTo(BigDecimal.valueOf(threshold)) > 0) {
                ProductResult result = new ProductResult(
                        kvProduct.getKey().getKey(),
                        kvProduct.getKey().getValue(),
                        kvProduct.getValue());

                out.output(result);
            }
        }
    }

    class GroupProductsFn extends DoFn<Product, KV<KV<Integer, String>, BigDecimal>> {
        @ProcessElement
        public void processElement(@Element Product product, OutputReceiver<KV<KV<Integer, String>, BigDecimal>> outputReceiver) {
            KV<Integer, String> key = KV.of(product.getProductID(), product.getProductName());
            BigDecimal value = product.getUnitPrice();

            outputReceiver.output(KV.of(key, value));
        }
    }

    class ParseProductsFn extends DoFn<String, Product> {
        @ProcessElement
        public void processElement(@Element String record, OutputReceiver<Product> out) {
            String[] strArr = record.split(";");

            int productID = Integer.parseInt(strArr[0]);
            String productName = strArr[1];
            BigDecimal unitPrice = BigDecimal.valueOf(Double.parseDouble(strArr[31].replace(',', '.')));

            Product product = new Product(productID, productName, unitPrice);
            out.output(product);
        }
    }

    class ParseResultsFn extends DoFn<ProductResult, String> {
        @ProcessElement
        public void processElement(@Element ProductResult result, OutputReceiver<String> out) {
            out.output(result.toString());
        }
    }
}





