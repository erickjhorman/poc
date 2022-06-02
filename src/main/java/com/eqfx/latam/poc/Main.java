package com.eqfx.latam.poc;

import com.eqfx.latam.poc.scenario.SalesByQuarter;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

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
                //TODO same as one but with the second model
                break;
            }
        }
        pipeline.run();
    }
}
