package com.ecommerce.produto.handler;

import com.ecommerce.produto.exceptions.BadRequestException;
import com.ecommerce.produto.exceptions.ConflictException;
import com.ecommerce.produto.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HttpErrorExceptionHandler {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> conflict(ConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> notFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());


    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> BadRequest(BadRequestException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }



}
