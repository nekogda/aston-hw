package org.example.user.rest.ctrl;

import lombok.extern.slf4j.Slf4j;
import org.example.user.app.exception.UserAppAccessDeniedException;
import org.example.user.app.exception.UserAppDataException;
import org.example.user.app.exception.UserAppException;
import org.example.user.app.exception.UserAppIllegalStateException;
import org.example.user.app.exception.UserAppNotFoundException;
import org.example.user.app.exception.UserAppValidationException;
import org.example.user.infra.Log;
import org.example.user.infra.LogLevel;
import org.example.user.rest.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {UserAppAccessDeniedException.class})
    @Log(LogLevel.ERROR)
    public ResponseEntity<ErrorResponse> handleAccess(UserAppAccessDeniedException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.from(List.of(ex.getMessage())));
    }

    @ExceptionHandler(value = {UserAppValidationException.class})
    @Log(LogLevel.ERROR)
    public ResponseEntity<ErrorResponse> handleValidation(UserAppValidationException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.from(List.of(ex.getMessage())));
    }

    @ExceptionHandler(value = {UserAppNotFoundException.class})
    @Log(LogLevel.ERROR)
    public ResponseEntity<ErrorResponse> handleNotFound(UserAppNotFoundException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.from(List.of(ex.getMessage())));
    }

    @ExceptionHandler(value = {UserAppIllegalStateException.class})
    @Log(LogLevel.ERROR)
    public ResponseEntity<ErrorResponse> handleIllegalState(UserAppIllegalStateException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from(List.of(ex.getMessage())));
    }

    @ExceptionHandler(value = {UserAppDataException.class})
    @Log(LogLevel.ERROR)
    public ResponseEntity<ErrorResponse> handleIllegalState(UserAppDataException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from(List.of("Something goes wrong!")));
    }

    @ExceptionHandler(value = {UserAppException.class})
    @Log(LogLevel.ERROR)
    public ResponseEntity<ErrorResponse> handleOtherDomain(UserAppException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .body(ErrorResponse.from(List.of(ex.getMessage())));
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @Log(LogLevel.ERROR)
    public ResponseEntity<ErrorResponse> handleOther(RuntimeException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from(List.of(ex.getMessage())));
    }
}