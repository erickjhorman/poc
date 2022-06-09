package com.eqfx.latam.poc.scenario;

import com.eqfx.latam.poc.csv.CSVRecordMap;
import com.eqfx.latam.poc.gcs.ObjectReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.eqfx.latam.poc.Main.FileUploadedEvent;

@Log
@RequiredArgsConstructor
public class GcpStorageCsvReaderFn implements SerializableFunction<FileUploadedEvent, Iterable<CSVRecordMap>> {
    private final CSVFormat csvFormat;


    @Override
    public Iterable<CSVRecordMap> apply(FileUploadedEvent input) {
        Reader reader = ObjectReader.read(input.bucket, input.filename);

        try{
            return csvFormat.parse(reader)
                    .stream()
                    .map(CSVRecordMap::valueOf)
                    .collect(Collectors.toList());
        }catch (IOException exception){
            log.log(Level.SEVERE,exception.getMessage());
            return List.of();
        }

    }
}
