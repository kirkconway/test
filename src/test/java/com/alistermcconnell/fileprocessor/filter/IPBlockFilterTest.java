package com.alistermcconnell.fileprocessor.filter;

import com.alistermcconnell.fileprocessor.domain.IpDetails;
import com.alistermcconnell.fileprocessor.service.IPService;
import com.alistermcconnell.fileprocessor.service.LoggingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

class IPBlockFilterTest {

    IPService ipService = Mockito.mock(IPService.class);
    LoggingService loggingService = Mockito.mock(LoggingService.class);
    IPBlockFilter ipBlockFilter = new IPBlockFilter(ipService, loggingService);


    @Test
    void testFilterWillReturnWithForbiddenIfIpIsNotAllowed() throws ServletException, IOException {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        IpDetails ipDetails = new IpDetails("GBR", "AWS");

        when(request.getHeader("X-FORWARDED-FOR")).thenReturn("127.0.0.1");
        when(ipService.getIpDetails("127.0.0.1")).thenReturn(ipDetails);
        when(ipService.ipAllowed(ipDetails)).thenReturn(false);

        ipBlockFilter.doFilter(request, response, chain);

        verify(request, times(2)).getHeader("X-FORWARDED-FOR");
        verify(request).getRequestURI();
        verify(ipService).getIpDetails("127.0.0.1");
        verify(ipService).ipAllowed(ipDetails);
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Ip address comes from forbidden country or ISP");
        verify(response).getStatus();

        verifyNoInteractions(chain);
        verifyNoMoreInteractions(response, request);
    }

    @Test
    void testFilterWillContainOnChainIfIpIsAllowed() throws ServletException, IOException {

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);
        IpDetails ipDetails = new IpDetails("GBR", "AWS");

        when(request.getHeader("X-FORWARDED-FOR")).thenReturn("127.0.0.1");
        when(ipService.getIpDetails("127.0.0.1")).thenReturn(ipDetails);
        when(ipService.ipAllowed(ipDetails)).thenReturn(true);
        when(response.getStatus()).thenReturn(HttpServletResponse.SC_OK);


        ipBlockFilter.doFilter(request, response, chain);

        verify(request, times(2)).getHeader("X-FORWARDED-FOR");
        verify(request).getRequestURI();
        verify(ipService).ipAllowed(ipDetails);
        verify(ipService).getIpDetails("127.0.0.1");
        verify(chain).doFilter(request, response);
        verify(response).getStatus();

        verifyNoMoreInteractions(chain, request, response);
    }


}