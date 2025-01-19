package com.alistermcconnell.fileprocessor.filter;

import com.alistermcconnell.fileprocessor.domain.IpDetails;
import com.alistermcconnell.fileprocessor.domain.RequestLog;
import com.alistermcconnell.fileprocessor.service.IPService;
//import com.alistermcconnell.fileprocessor.service.LoggingService;
import com.alistermcconnell.fileprocessor.service.LoggingService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class IPBlockFilter implements Filter {

    private final IPService ipService;
    private final LoggingService loggingService;

    public IPBlockFilter(IPService ipService, LoggingService loggingService) {
        this.ipService = ipService;
        this.loggingService = loggingService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        
        IpDetails ipDetails = ipService.getIpDetails(request.getHeader("X-FORWARDED-FOR"));

        Long startTime = System.currentTimeMillis();

        if(ipService.ipAllowed(ipDetails)){
            filterChain.doFilter(servletRequest, servletResponse);
            loggingService.logRequest(getRequestLog(request, response, ipDetails, startTime));
        }
        else{
            ((HttpServletResponse) servletResponse).sendError(HttpStatus.FORBIDDEN.value(), "Ip address comes from forbidden country or ISP");
            loggingService.logRequest(getRequestLog(request, response, ipDetails, startTime));
        }

    }

    private static RequestLog getRequestLog(HttpServletRequest request, HttpServletResponse response, IpDetails ipDetails, Long startTime) {

        return new RequestLog(null,
                UUID.randomUUID().toString(), request.getRequestURI(),
                System.currentTimeMillis(),
                response.getStatus(),
                request.getHeader("X-FORWARDED-FOR"),
                ipDetails.country(), ipDetails.isp(),
                System.currentTimeMillis() - startTime);
    }

}
