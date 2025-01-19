package com.alistermcconnell.fileprocessor.domain;

//import jakarta.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;


@Entity
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;
    private String requestUri;
    private Long timestamp;
    private int responseCode;
    private String ipAddress;
    private String countryCode;
    private String ipProvider;
    private long timeLapsed;

    public RequestLog() {}

    public RequestLog(Long id, String uuid, String requestUri, Long timestamp, int responseCode, String ipAddress, String countryCode, String ipProvider, long timeLapsed) {
        this.id = id;
        this.uuid = uuid;
        this.requestUri = requestUri;
        this.timestamp = timestamp;
        this.responseCode = responseCode;
        this.ipAddress = ipAddress;
        this.countryCode = countryCode;
        this.ipProvider = ipProvider;
        this.timeLapsed = timeLapsed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIpProvider() {
        return ipProvider;
    }

    public void setIpProvider(String ipProvider) {
        this.ipProvider = ipProvider;
    }

    public long getTimeLapsed() {
        return timeLapsed;
    }

    public void setTimeLapsed(long timeLapsed) {
        this.timeLapsed = timeLapsed;
    }
}
