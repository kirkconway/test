package com.alistermcconnell.fileprocessor.controller;

import com.alistermcconnell.fileprocessor.domain.UserResponse;
import com.alistermcconnell.fileprocessor.domain.ValidationError;
import com.alistermcconnell.fileprocessor.service.FileService;
import com.alistermcconnell.fileprocessor.service.RequestConverterService;
import com.alistermcconnell.fileprocessor.service.ValidationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class UploadController {

    private final RequestConverterService requestConverterService;
    private final ValidationService validationService;
    private final FileService fileService;

    private final static String FILE_NAME = "OutcomeFile.json";

    public UploadController(RequestConverterService requestConverterService, ValidationService validationService, FileService fileService) {
        this.requestConverterService = requestConverterService;
        this.validationService = validationService;
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam(name = "skipValidation", required = false) boolean skipValidation) {

        String input;
        try {
            input = new String(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        if(!skipValidation) {
            System.out.println("validating");
            List<ValidationError> validationErrors = validationService.validate(input);

            if (!validationErrors.isEmpty()) {
                return new ResponseEntity<>(validationErrors, headers, HttpStatus.BAD_REQUEST);
            }
        }

        List<UserResponse> response;

        try {
            response = requestConverterService.convertRequest(input);
            fileService.createOutcomeFile(FILE_NAME, response);
        } catch (IOException e) {
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+FILE_NAME);
        return new ResponseEntity<>( response, headers, HttpStatus.OK);
    }
}
