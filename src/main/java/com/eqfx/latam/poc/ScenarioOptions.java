package com.eqfx.latam.poc;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface ScenarioOptions extends PipelineOptions {



    @Description("target avro file")
    @Validation.Required
    String getTargetFile();
    void setTargetFile(String value);

    @Description("subscriptionId")
    @Validation.Required
    String getSubscriptionId();
    void setSubscriptionId(String value);

    enum Scenario {
        ONE,TWO
    }
}
