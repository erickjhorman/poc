package com.eqfx.latam.poc.transforms;

import org.apache.beam.sdk.transforms.SerializableFunction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CombineProductsFn implements SerializableFunction<Iterable<BigDecimal>, BigDecimal> {
    @Override
    public BigDecimal apply(Iterable<BigDecimal> input) {
        List<BigDecimal> list = StreamSupport.stream(input.spliterator(), false)
                .collect(Collectors.toList());

        return list.stream()
                .reduce((bigDecimal1, bigDecimal2) -> bigDecimal1.add(bigDecimal2))
                .get()
                .divide(BigDecimal.valueOf(list.size()), RoundingMode.DOWN);
    }
}
