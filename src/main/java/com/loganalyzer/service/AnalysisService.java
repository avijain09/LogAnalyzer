package com.loganalyzer.service;
import com.loganalyzer.entity.Log;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    private final ChatClient chatClient;

    public AnalysisService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String analyzeLogs(List<Log> logs) {

        String logText = logs.stream()
                .map(log -> String.format(
                        "[%s] [%s] [%s] %s",
                        log.getTimestamp(),
                        log.getServiceName(),
                        log.getLogLevel(),
                        log.getMessage()
                ))
                .collect(Collectors.joining("\n"));

        String prompt = """
        You are a backend reliability engineer.

        Analyze the following logs and provide:
        1. Root cause
        2. Impact
        3. Suggested fix

        Logs:
        """ + logText;

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}