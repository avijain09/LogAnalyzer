package com.loganalyzer.dto;

import lombok.Data;

@Data
public class LogRequestDTO {

    private String serviceName;
    private String logLevel;
    private String message;
}