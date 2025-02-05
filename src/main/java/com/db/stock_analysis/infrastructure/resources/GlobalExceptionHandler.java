package com.db.stock_analysis.infrastructure.resources;

import com.db.stock_analysis.domain.exceptions.CustomErrorException;
import com.db.stock_analysis.infrastructure.resources.dtos.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<ApiError> handleCustomException(final CustomErrorException ex) {
        var apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(ex.getStatus());
        apiError.setTimeStamp(LocalDate.now());
        return ResponseEntity
                .status(HttpStatus.valueOf(ex.getStatus()))
                .body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(final Exception ex) {
        var apiError = new ApiError();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(500);
        apiError.setTimeStamp(LocalDate.now());
        return ResponseEntity
                .status(HttpStatus.valueOf(500))
                .body(apiError);
    }
}
