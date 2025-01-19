package com.alistermcconnell.fileprocessor.service;

import com.alistermcconnell.fileprocessor.domain.UserResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestConverterServiceTest {


    RequestConverterService requestConverterService = new RequestConverterService();


    @Test
    void testProcessFile() throws Exception {

        String input = """
                18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1
                3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5
                1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|8.5|15.3
                """;


        List<UserResponse> response = requestConverterService.convertRequest(input);

        assertEquals(response.get(0).name(), "John Smith");
        assertEquals(response.get(0).topSpeed(), "12.1");
        assertEquals(response.get(0).transport(), "Rides A Bike");

        assertEquals(response.get(1).name(), "Mike Smith");
        assertEquals(response.get(1).topSpeed(), "95.5");
        assertEquals(response.get(1).transport(), "Drives an SUV");

        assertEquals(response.get(2).name(), "Jenny Walters");
        assertEquals(response.get(2).topSpeed(), "15.3");
        assertEquals(response.get(2).transport(), "Rides A Scooter");
    }

}