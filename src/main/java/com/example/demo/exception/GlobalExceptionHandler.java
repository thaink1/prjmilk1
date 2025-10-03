package com.example.demo.exception;

import com.example.demo.dto.BaseResponse;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<BaseResponse> handleException(RuntimeException ex){
        BaseResponse response = new BaseResponse();
        response.setCode(1001);
        response.setMessage(ex.getMessage());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<BaseResponse> handleException(MethodArgumentNotValidException ex){
        BaseResponse response = new BaseResponse();
        response.setCode(1002);
        response.setMessage(ex.getFieldError().getDefaultMessage());
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());
        return ResponseEntity.badRequest().body(response);
    }
}
