package com.loganalyzer.controller;

import com.loganalyzer.dto.LogRequestDTO;
import com.loganalyzer.entity.Log;
import com.loganalyzer.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    // ✅ 1. Ingest logs
    @PostMapping("/ingest")
    public String ingestLog(@RequestBody LogRequestDTO request) {
        logService.ingestLog(request);
        return "Log ingested successfully";
    }

    // ✅ 2. Get logs (pagination + optional filters)
    @GetMapping
    public Page<Log> getLogs(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String service,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime,
            Pageable pageable
    ) {
        return logService.getLogs(level, service, startTime, endTime, pageable);
    }

    // ✅ 3. Get log by ID
    @GetMapping("/{id}")
    public Log getLogById(@PathVariable Long id) {
        return logService.getLogById(id);
    }

    // ✅ 4. Delete log
    @DeleteMapping("/{id}")
    public String deleteLog(@PathVariable Long id) {
        logService.deleteLog(id);
        return "Log deleted successfully";
    }

    // ✅ 5. Count logs by level
    @GetMapping("/count")
    public Long countLogsByLevel(@RequestParam String level) {
        return logService.countLogsByLevel(level);
    }
}