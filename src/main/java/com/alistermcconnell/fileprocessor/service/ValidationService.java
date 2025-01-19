package com.alistermcconnell.fileprocessor.service;

import com.alistermcconnell.fileprocessor.domain.ValidationError;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Float.parseFloat;

@Service
public class ValidationService {

    public List<ValidationError> validate(String input) {

        List<ValidationError> validationErrors = new ArrayList<ValidationError>();

        AtomicInteger index = new AtomicInteger(1);
        input.lines().forEach(line -> {
            verify(index.getAndIncrement(), line.split("\\|")).ifPresent(validationErrors::add);
        });


        return validationErrors;
    }

    private Optional<ValidationError> verify(int lineNumber, String[] line) {

        if (line.length != 7) {
            return Optional.of(new ValidationError(String.format("error in line %s : param size incorrect", lineNumber)));
        }

        if (line[1].length() != 6) {
            return Optional.of(new ValidationError(String.format("error in line %s : id not the correct size", lineNumber)));
        }

        try {
            parseFloat(line[6]);
        } catch (NumberFormatException e) {
            return Optional.of(new ValidationError(String.format("error in line %s : top speed is not a float", lineNumber)));
        }


        return Optional.empty();
    }
}
