package com.loganalyzer.util;

public class LogUtil {

    // Replace numbers with X (basic normalization)
    public static String normalize(String message) {
        if (message == null) return "";
        return message.replaceAll("\\d+", "X");
    }

    // Generate hash key
    public static String generateHash(String normalizedMessage) {
        return String.valueOf(normalizedMessage.hashCode());
    }
}