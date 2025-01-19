package com.alistermcconnell.fileprocessor.controller;

import com.alistermcconnell.fileprocessor.domain.ValidationError;
import com.alistermcconnell.fileprocessor.service.FileService;
import com.alistermcconnell.fileprocessor.service.RequestConverterService;
import com.alistermcconnell.fileprocessor.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UploadControllerTest {

    private final ValidationService validationService = Mockito.mock(ValidationService.class);
    private final FileService fileService = Mockito.mock(FileService.class);
    private final RequestConverterService requestConverterService = Mockito.mock(RequestConverterService.class);

    UploadController uploadController = new UploadController(requestConverterService, validationService, fileService);

    @Test
    public void testUploadWithValidation() throws IOException {

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn("".getBytes());

        when(validationService.validate(any(String.class))).thenReturn(Collections.emptyList());
        when(requestConverterService.convertRequest(any(String.class))).thenReturn(Collections.emptyList());

        uploadController.upload(multipartFile, false);

        verify(validationService).validate(any(String.class));
        verify(requestConverterService).convertRequest(any(String.class));
        verify(fileService).createOutcomeFile("OutcomeFile.json", Collections.emptyList());


        verifyNoMoreInteractions(validationService, requestConverterService, fileService);
    }

    @Test
    public void testUploadWithNoValidation() throws IOException {

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn("".getBytes());

        when(requestConverterService.convertRequest(any(String.class))).thenReturn(Collections.emptyList());

        uploadController.upload(multipartFile, true);

        verify(requestConverterService).convertRequest(any(String.class));
        verify(fileService).createOutcomeFile("OutcomeFile.json", Collections.emptyList());

        verifyNoInteractions(validationService);
        verifyNoMoreInteractions(requestConverterService, fileService);
    }

    @Test
    public void testUploadWithValidationErrors() throws IOException {

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn("".getBytes());

        when(validationService.validate(any(String.class))).thenReturn(Collections.singletonList(new ValidationError("validation error")));

        uploadController.upload(multipartFile, false);

        verify(validationService).validate(any(String.class));
        verifyNoInteractions(requestConverterService, fileService);
        verifyNoMoreInteractions(validationService);
    }


}