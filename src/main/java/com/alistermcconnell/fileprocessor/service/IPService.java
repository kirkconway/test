package com.alistermcconnell.fileprocessor.service;

import com.alistermcconnell.fileprocessor.domain.IpDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class IPService {

    private final String geoLocationUrl;
    private final RestTemplate restTemplate;
    private final static List<String> bannedCountrys = Arrays.asList("CN", "ES", "US");
    private final static List<String> bannedIsps = Arrays.asList("AWS", "GCP", "Azure");

    public IPService(@Value("${geolocation.url}") String geoLocationUrl, RestTemplate restTemplate) {
        this.geoLocationUrl = geoLocationUrl;
        this.restTemplate = restTemplate;
    }

    public IpDetails getIpDetails(String ip) {
        TypeReference<HashMap<String,Object>> typeRef
                = new TypeReference<HashMap<String,Object>>() {};

        JsonNode jsonNode = restTemplate.getForObject(geoLocationUrl+"/json/"+ip, JsonNode.class);

        return new IpDetails(jsonNode.get("countryCode").asText(),jsonNode.get("isp").asText());

    }


    public boolean ipAllowed(IpDetails ipDetails) {

        if(bannedCountrys.contains(ipDetails.country())){
            return false;
        }

        return !bannedIsps.contains(ipDetails.isp());

    }
}
