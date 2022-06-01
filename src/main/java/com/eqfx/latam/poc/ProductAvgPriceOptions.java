package com.eqfx.latam.poc;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface ProductAvgPriceOptions extends PipelineOptions {
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
