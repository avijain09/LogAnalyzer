package com.loganalyzer.service;

import com.loganalyzer.dto.LogRequestDTO;
import com.loganalyzer.entity.Log;
import com.loganalyzer.repository.LogRepository;
import com.loganalyzer.util.LogUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public void ingestLog(LogRequestDTO request) {

        // 1. Normalize
        String normalized = LogUtil.normalize(request.getMessage());

        // 2. Generate hash
        String hash = LogUtil.generateHash(normalized);

        // 3. Build entity
        Log log = Log.builder()
                .serviceName(request.getServiceName())
                .logLevel(request.getLogLevel())
                .message(request.getMessage())
                .timestamp(System.currentTimeMillis())
                .hashKey(hash)
                .build();

        // 4. Save
        logRepository.save(log);
    }

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

    public Page<Log> getLogs(String level, String service,
                             LocalDateTime startTime, LocalDateTime endTime,
                             Pageable pageable) {

        Specification<Log> spec = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (level != null && !level.isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("level")),
                        level.toLowerCase()
                ));
            }

            if (service != null && !service.isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("service")),
                        service.toLowerCase()
                ));
            }

            if (startTime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("timestamp"), startTime
                ));
            }

            if (endTime != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("timestamp"), endTime
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return logRepository.findAll(spec, pageable);
    }

    public Log getLogById(Long id){

    }

    public void deleteLog(Long id){

    }

    public Long countLogsByLevel(String level){

    }
}