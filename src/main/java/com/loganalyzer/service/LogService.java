package com.loganalyzer.service;

import com.loganalyzer.dto.LogRequestDTO;
import com.loganalyzer.entity.Log;
import com.loganalyzer.repository.LogRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    // ✅ 1. Ingest log
    public void ingestLog(LogRequestDTO request) {
        Log log = new Log();

        log.setLogLevel(request.getLogLevel());
        log.setMessage(request.getMessage());
        log.setServiceName(request.getServiceName());

        // If timestamp not provided → use current time
        log.setTimestamp(
                request.getTimestamp() != null
                        ? request.getTimestamp()
                        : LocalDateTime.now()
        );

        logRepository.save(log);
    }

    // ✅ 2. Get logs with filters + pagination
    public Page<Log> getLogs(String level, String service,
                             LocalDateTime startTime, LocalDateTime endTime,
                             Pageable pageable) {

        Specification<Log> spec = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (level != null && !level.isEmpty()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("level")),
                        level.toLowerCase()
                ));
            }

            if (service != null && !service.isEmpty()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("service")),
                        service.toLowerCase()
                ));
            }

            if (startTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("timestamp"), startTime
                ));
            }

            if (endTime != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("timestamp"), endTime
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return logRepository.findAll(spec, pageable);
    }

    // ✅ 3. Get log by ID
    public Log getLogById(Long id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found with id: " + id));
    }

    // ✅ 4. Delete log
    public void deleteLog(Long id) {
        if (!logRepository.existsById(id)) {
            throw new RuntimeException("Log not found with id: " + id);
        }
        logRepository.deleteById(id);
    }

    // ✅ 5. Count logs by level
    public Long countLogsByLevel(String level) {
        return logRepository.countByLevelIgnoreCase(level);
    }
}