package com.alistermcconnell.fileprocessor.repository;

import com.alistermcconnell.fileprocessor.domain.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<RequestLog, Long> {

}
