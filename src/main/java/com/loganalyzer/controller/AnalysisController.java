package com.loganalyzer.controller;
import com.loganalyzer.entity.Log;
import com.loganalyzer.service.AnalysisService;
import com.loganalyzer.service.LogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;
    private final LogService logService;

    public AnalysisController(AnalysisService analysisService,
                              LogService logService) {
        this.analysisService = analysisService;
        this.logService = logService;
    }

    @GetMapping("/cluster/{hashKey}")
    public String analyzeCluster(@PathVariable String hashKey) {

        List<Log> logs = logService.getLogsByHashKey(hashKey);

        return analysisService.analyzeLogs(logs);
    }
}
