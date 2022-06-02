package com.eqfx.latam.poc.transforms;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

public class LogTransform<T> extends PTransform<PCollection<T>, PCollection<T>> {

    @Override
    public PCollection<T> expand(PCollection<T> input) {
        return input.apply(ParDo.of(new DoFn<T, T>() {
            @ProcessElement
            public void processElement(@Element T element, OutputReceiver<T> out) {
                System.out.println(element.toString());
                out.output(element);
            }
        }));
    }
}
