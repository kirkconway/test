package com.alistermcconnell.fileprocessor.service;

import com.alistermcconnell.fileprocessor.domain.RequestLog;
import com.alistermcconnell.fileprocessor.repository.LogRepository;
//import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    private final LogRepository logRepository;

    public LoggingService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Transactional
    public void logRequest(RequestLog requestLog) {
        logRepository.save(requestLog);
    }

}