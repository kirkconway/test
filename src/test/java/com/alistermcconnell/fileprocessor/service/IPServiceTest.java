package com.alistermcconnell.fileprocessor.service;

import com.alistermcconnell.fileprocessor.domain.IpDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class IPServiceTest {

    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    IPService ipService = new IPService("url", restTemplate);


    @Test
    public void returnIpDetails() throws JsonProcessingException {

        String geoLocationsResponse = """
                {
                    "countryCode":"CN",
                    "isp":"SKY"
                }
                """;

        when(restTemplate.getForObject(any(String.class), eq(JsonNode.class))).thenReturn(new ObjectMapper().readTree(geoLocationsResponse));

        IpDetails result = ipService.getIpDetails("1.1.1.1");

        assertEquals(new IpDetails("CN", "SKY"), result);

    }

    @Test
    public void returnFalseIfCountryIsBanned() throws JsonProcessingException {

        assertFalse( ipService.ipAllowed(new IpDetails("CN","SKY")));

    }

    @Test
    public void returnFalseIfIspIsBanned() throws JsonProcessingException {


        assertFalse( ipService.ipAllowed(new IpDetails("GBR","AWS")));

    }

    @Test
    public void returnTrueIfCountryAndISPIsNotBanned() throws JsonProcessingException {

        assertTrue( ipService.ipAllowed(new IpDetails("GBR","SKY")));

    }

}