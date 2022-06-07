package com.eqfx.latam.poc;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface Options extends PipelineOptions {

    @Description("subscription id")
    @Validation.Required
    String getSubscriptionId();
    void setSubscriptionId(String value);
}
