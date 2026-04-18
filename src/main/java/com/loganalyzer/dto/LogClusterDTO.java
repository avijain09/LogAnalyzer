package com.loganalyzer.dto;

public class LogClusterDTO {
    private String hashKey;
    private long count;

    public LogClusterDTO(String hashKey, long count) {
        this.hashKey = hashKey;
        this.count = count;
    }

    public String getHashKey() { return hashKey; }
    public long getCount() { return count; }
}
