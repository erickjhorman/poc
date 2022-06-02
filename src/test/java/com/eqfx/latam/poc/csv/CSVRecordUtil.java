package com.eqfx.latam.poc.csv;

import org.apache.beam.repackaged.core.org.apache.commons.lang3.StringUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;

public class CSVRecordUtil {
    private CSVRecordUtil() {
    }

    static CSVRecordMap mockRecord(String[] headers, String... data) throws IOException {
        String values = StringUtils.join(data, ",");
        try (CSVParser parser = CSVFormat.DEFAULT.builder().setHeader(headers).build().parse(new StringReader(values))) {
            CSVRecord record = parser.iterator().next();
            return CSVRecordMap.valueOf(record);
        }
    }
}
