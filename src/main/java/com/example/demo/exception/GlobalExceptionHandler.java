package com.example.demo.exception;

import com.example.demo.dto.BaseResponse;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice

public class GlobalExceptionHandler {

    // RuntimeException
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleRuntimeException(RuntimeException ex) {
        BaseResponse response = new BaseResponse();
        response.setCode(1001);
        response.setMessage(ex.getMessage() != null ? ex.getMessage() : "Runtime error occurred");
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    // Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        BaseResponse response = new BaseResponse();
        response.setCode(1002);
        response.setMessage(message);
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    // Lỗi 404
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse handleNotFound(NoHandlerFoundException ex) {
        BaseResponse response = new BaseResponse();
        response.setCode(1003);
        response.setMessage("Endpoint not found: " + ex.getRequestURL());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }

    // Lỗi hệ thống
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse handleGenericException(Exception ex) {
        BaseResponse response = new BaseResponse();
        response.setCode(1999);
        response.setMessage("Internal server error: " + ex.getMessage());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
