package com.loganalyzer.controller;

import com.loganalyzer.dto.LogClusterDTO;
import com.loganalyzer.dto.TopErrorDTO;
import com.loganalyzer.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class AnalyticsController {
    private final LogService logService;
    @GetMapping("/top-errors")
    public List<TopErrorDTO> getTopErrors(
            @RequestParam(defaultValue = "5") int limit
    ) {
        return logService.getTopErrors(limit);
    }

    @GetMapping("/error-rate")
    public double getErrorRate(@RequestParam String service) {
        return logService.getErrorRate(service);
    }

    @GetMapping("/clusters")
    public List<LogClusterDTO> getClusters() {
        return logService.getLogClusters();
    }


}
