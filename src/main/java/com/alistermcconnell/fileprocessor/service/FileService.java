package com.alistermcconnell.fileprocessor.service;

import com.alistermcconnell.fileprocessor.domain.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    public void createOutcomeFile(String fileName, List<UserResponse> content) throws IOException {

        String json = new ObjectMapper().writeValueAsString(content);
        FileOutputStream outputStream = new FileOutputStream(fileName);
        byte[] strToBytes = json.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }
}
