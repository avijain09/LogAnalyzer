package com.loganalyzer.repository;

import com.loganalyzer.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LogRepository extends JpaRepository<Log, Long>,
        JpaSpecificationExecutor<Log> {

    // Case-insensitive level filter
    long countByLogLevelIgnoreCase(String level);

        @Query("""
    SELECT l.message, COUNT(l)
    FROM Log l
    WHERE LOWER(l.logLevel) = 'error'
    GROUP BY l.message
    ORDER BY COUNT(l) DESC
    """)
        List<Object[]> findTopErrors(Pageable pageable);

        @Query("""
    SELECT l.hashKey, COUNT(l)
    FROM Log l
    GROUP BY l.hashKey
    ORDER BY COUNT(l) DESC
    """)
        List<Object[]> findLogClusters();

    long countByServiceName(String serviceName);

    long countByServiceNameAndLogLevelIgnoreCase(String serviceName, String logLevel);

    List<Log> findByHashKey(String hashKey);

}