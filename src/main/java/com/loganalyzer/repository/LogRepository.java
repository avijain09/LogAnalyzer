package com.loganalyzer.repository;

import com.loganalyzer.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogRepository extends JpaRepository<Log, Long>,
        JpaSpecificationExecutor<Log> {

    // Optional: simple queries (not required if using Specifications)

    // Case-insensitive level filter
    long countByLevelIgnoreCase(String level);
}