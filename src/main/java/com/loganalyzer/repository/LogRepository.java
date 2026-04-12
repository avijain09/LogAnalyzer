package com.loganalyzer.repository;

import com.loganalyzer.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByLevel(String Level);
    List<Log> findByService(String service);
}