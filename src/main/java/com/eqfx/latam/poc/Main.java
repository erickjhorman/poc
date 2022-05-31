package com.eqfx.latam.poc;

import com.eqfx.latam.poc.pubsub.GMessageSubscriber;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

public class Main {
    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();
        Pipeline pipeline = Pipeline.create(options);

        new GMessageSubscriber().consumeNotification(); /* run subscriber to pull new messages in pubsub */
        //TODO all

        pipeline.run();
    }
}
