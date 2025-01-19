package com.alistermcconnell.fileprocessor.service;

import com.alistermcconnell.fileprocessor.domain.UserResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class RequestConverterService {

    public List<UserResponse> convertRequest(String input) throws IOException {

        Scanner scanner = new Scanner(input);

        ArrayList<UserResponse> userResponses = new ArrayList<>();

        try {
            while (scanner.hasNextLine()) {
                String[] params = scanner.nextLine().split("\\|");

                userResponses.add(new UserResponse(params[2], params[4], params[6]));
            }
        } finally {
            scanner.close();
        }

        return userResponses;
    }

}
