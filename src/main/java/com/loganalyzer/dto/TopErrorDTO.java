package com.loganalyzer.dto;

public class TopErrorDTO {
    private String message;
    private long count;

    public TopErrorDTO(String message, long count) {
        this.message = message;
        this.count = count;
    }
}
