package com.eqfx.latam.poc.model;

import java.io.Serializable;


public class Event implements Serializable {

    private String id;
    private String bucket;
    private String filename;

    private String eventType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", bucket='" + bucket + '\'' +
                ", filename='" + filename + '\'' +
                ", Event Type" + eventType + '\'' +
                '}';
    }
}
