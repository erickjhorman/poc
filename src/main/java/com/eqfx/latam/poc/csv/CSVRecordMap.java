package com.eqfx.latam.poc.csv;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CSVRecordMap implements Serializable {
    private final Map<String,Integer> headerMap;
    private final CSVRecord record;
    public static CSVRecordMap valueOf(CSVRecord record){
       return new CSVRecordMap(record.getParser().getHeaderMap(),record);
    }
    public String get(final String name){
        Optional<String> s = Optional.ofNullable(headerMap)
                .map(m -> m.get(name))
                .map(record::get);
        return s.orElseGet(()->record.get(name));
    }
    public String get(final int i){
        return record.get(i);
    }
}
