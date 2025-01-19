package com.alistermcconnell.fileprocessor.service;

import com.alistermcconnell.fileprocessor.domain.ValidationError;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationServiceTest {

    ValidationService validationService = new ValidationService();


    @Test
    void testProcessFile() throws Exception {

        String input = """
                18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2
                3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D2|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5
                1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|8.5|string
                """;


        List<ValidationError> response = validationService.validate(input);


        assertEquals( 3, response.size());
        assertEquals("error in line 1 : param size incorrect", response.get(0).message());
        assertEquals( "error in line 2 : id not the correct size", response.get(1).message());
        assertEquals( "error in line 3 : top speed is not a float", response.get(2).message());

    }

}