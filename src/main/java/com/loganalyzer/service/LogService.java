package com.loganalyzer.service;

import com.loganalyzer.dto.LogClusterDTO;
import com.loganalyzer.dto.LogRequestDTO;
import com.loganalyzer.dto.TopErrorDTO;
import com.loganalyzer.entity.Log;
import com.loganalyzer.repository.LogRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    // ✅ 1. Ingest log
    private String normalizeMessage(String message) {
        return message
                .replaceAll("\\d+", "")          // remove numbers
                .replaceAll("[a-fA-F0-9]{8,}", "") // remove ids / hashes
                .trim()
                .toLowerCase();
    }



    private String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

    private Log saveLog(Log log){
        String normalizedMessage = normalizeMessage(log.getMessage());

        String rawKey = log.getServiceName()
                + "|" + log.getLogLevel()
                + "|" + normalizedMessage;

        String hashKey = generateHash(rawKey);

        log.setHashKey(hashKey);

        return logRepository.save(log);
    }

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

        saveLog(log);
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
        return logRepository.countByLogLevelIgnoreCase(level);
    }

    public List<TopErrorDTO> getTopErrors(int limit) {

        Pageable pageable = PageRequest.of(0, limit);

        return logRepository.findTopErrors(pageable)
                .stream()
                .map(obj -> new TopErrorDTO(
                        (String) obj[0],
                        (Long) obj[1]
                ))
                .toList();
    }

    public double getErrorRate(String service) {

        long total = logRepository.countByServiceName(service);
        long errors = logRepository.countByServiceNameAndLogLevelIgnoreCase(service, "ERROR");

        if (total == 0) return 0;

        return (double) errors / total;
    }

    public List<LogClusterDTO> getLogClusters() {
        List<Object[]> results = logRepository.findLogClusters();

        return results.stream()
                .map(row -> new LogClusterDTO(
                        (String) row[0],
                        (Long) row[1]
                ))
                .toList();
    }

    public List<Log> getLogsByHashKey(String hashKey) {
        return logRepository.findByHashKey(hashKey);
    }

}