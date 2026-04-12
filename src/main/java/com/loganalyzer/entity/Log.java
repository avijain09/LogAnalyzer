package com.loganalyzer.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private Long timestamp;

    private String hashKey;

    private Long clusterId; // will use later
}