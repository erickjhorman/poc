package com.eqfx.latam.poc.csv;

import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.Channels;
import java.util.Objects;

public interface CsvIO {
    static Read.Builder read(String file){
        return new Read.Builder(file);
    }
    class Read extends PTransform<PBegin, PCollection<CSVRecordMap>> {
        private final String file;
        private final CSVFormat csvFormat;

        static public Builder withFile(String file) {
            return new Builder(file);
        }

        private Read(String file, String[] headers, char delimiter) {
            this.file = Objects.requireNonNull(file);
            this.csvFormat = CSVFormat.Builder.create()
                    .setHeader(Objects.requireNonNull(headers))
                    .setDelimiter(delimiter)
                    .setSkipHeaderRecord(true)
                    .build();
        }

        @Override
        public PCollection<CSVRecordMap> expand(PBegin input) {
            return input
                    .apply(FileIO.match().filepattern(file))
                    .apply(FileIO.readMatches())
                    .apply(ParDo.of(new DoFn<FileIO.ReadableFile, CSVRecordMap>() {
                        @ProcessElement
                        public void processElement(@Element FileIO.ReadableFile element,
                                                   OutputReceiver<CSVRecordMap> outputReceiver) throws IOException {
                            Reader reader = new InputStreamReader(Channels.newInputStream(element.open()));
                            csvFormat.parse(reader).stream()
                                    .map(CSVRecordMap::valueOf)
                                    .forEach(outputReceiver::output);
                        }
                    }));
        }

        public static class Builder {
            private final String file;
            private String[] headers;
            private char delimiter;

            private Builder(String file) { this.file = file; }


            public Builder withHeaders(String... headers) {
                this.headers = headers;
                return this;
            }

            public Builder withDelimiter(char delimiter) {
                this.delimiter = delimiter;
                return this;
            }

            public Read build() {
                return new Read(file, headers, delimiter);
            }
        }
    }
}
