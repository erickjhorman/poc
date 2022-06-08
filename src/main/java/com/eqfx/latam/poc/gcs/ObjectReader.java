package com.eqfx.latam.poc.gcs;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

public interface ObjectReader {
    static Reader read(String bucketName, String filename) {
        StorageOptions storageOptions = StorageOptions.getDefaultInstance();
        Storage service = storageOptions.getService();
        Blob blob = service.get(bucketName, filename);
        ReadChannel readChannel = blob.reader();
        return Channels.newReader(readChannel, StandardCharsets.UTF_8);
    }
}
