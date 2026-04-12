package com.loganalyzer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;

    private String logLevel;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime timestamp;

    private String hashKey;

    private Long clusterId; // will use later

    public void setId(Long id) {
        this.id = id;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }
}