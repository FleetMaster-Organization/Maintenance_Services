package com.services.maintenance.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        String errorMessage = "Error de comunicación con servicio externo";
        if (ex.contentUTF8() != null && !ex.contentUTF8().isEmpty()) {
            try {
                JsonNode jsonNode = objectMapper.readTree(ex.contentUTF8());
                if (jsonNode.has("message")) {
                    errorMessage = jsonNode.get("message").asText();
                } else {
                    errorMessage = ex.contentUTF8();
                }
            } catch (Exception e) {
                errorMessage = ex.getMessage();
            }
        } else {
            errorMessage = ex.getMessage();
        }

        int status = ex.status() > 0 ? ex.status() : HttpStatus.INTERNAL_SERVER_ERROR.value();
        HttpStatus httpStatus = HttpStatus.resolve(status);
        String errorName = httpStatus != null ? httpStatus.getReasonPhrase() : "Microservice Error";

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status,
                errorName,
                errorMessage
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleException(BusinessRuleException ex) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}