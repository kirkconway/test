package com.alistermcconnell.fileprocessor.functional;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WireMockTest(httpPort = 8089)  // WireMock server running on port 8089
@SpringBootTest
@AutoConfigureMockMvc
class FileProcessingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testProcessFileWithValidIp() throws Exception {
        // Mock the external IP validation API
        stubFor(get(urlEqualTo("/json/192.168.1.1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody( """
                {
                    "countryCode":"GB",
                    "isp":"SKY"
                }
                """)));

        // Simulate a POST request to the file processing endpoint
        mockMvc.perform(multipart("/upload")
                        .file("file", "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n".getBytes())
                        .header("X-Forwarded-For", "192.168.1.1"))
                .andExpect(status().isOk())
                .andExpect(content().string("""
                        [{"name":"John Smith","transport":"Rides A Bike","topSpeed":"12.1"}]
                        """.trim()));
    }

    @Test
    void testProcessFileWithBlockedIp() throws Exception {
        // Mock the external IP validation API to return a blocked country (China)
        stubFor(get(urlEqualTo("/json/192.168.1.1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                {
                    "countryCode":"CN",
                    "isp":"SKY"
                }
                """)));

        // Simulate a POST request to the file processing endpoint
        mockMvc.perform(multipart("/api/processFile")
                        .file("file", "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n".getBytes())
                        .header("X-Forwarded-For", "192.168.1.1"))
                .andExpect(status().isForbidden())
                .andExpect(status().reason("Ip address comes from forbidden country or ISP"));
    }
}
