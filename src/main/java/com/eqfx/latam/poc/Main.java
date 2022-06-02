package com.eqfx.latam.poc;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

public class Main {
    public static void main(String[] args) {
        ScenarioOptions options = PipelineOptionsFactory.fromArgs(args)
                .withValidation()
                .as(ScenarioOptions.class);
        Pipeline pipeline = Pipeline.create(options);

        switch (options.getScenario()){
            case ONE: {
                //TODO execute scenario one
                break;
            }
            case TWO: {
                //TODO execute scenario two
                break;
            }
        }
        pipeline.run();
    }
}
