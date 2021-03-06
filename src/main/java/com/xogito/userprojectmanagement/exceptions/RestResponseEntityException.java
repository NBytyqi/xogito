package com.xogito.userprojectmanagement.exceptions;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value =
            ProjectNotFoundException.class)
    protected ResponseEntity<Object> handleConflict(ProjectNotFoundException ex) {
        String bodyOfException = String.format("Project with id: %d is not found", ex.getProjectId());
        ExceptionMessage exceptionMessage = new ExceptionMessage(HttpStatus.BAD_REQUEST, bodyOfException);
        return new ResponseEntity<>(exceptionMessage, exceptionMessage.getHttpStatus());
    }

    @ExceptionHandler(value =
            UserNotFoundException.class)
    protected ResponseEntity<Object> handleConflict(UserNotFoundException ex) {
        String bodyOfException = String.format("User with id: %d is not found", ex.getUserId());
        ExceptionMessage exceptionMessage = new ExceptionMessage(HttpStatus.BAD_REQUEST, bodyOfException);
        return new ResponseEntity<>(exceptionMessage, exceptionMessage.getHttpStatus());
    }

    @ExceptionHandler(value =
            ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConflict(ConstraintViolationException ex) {
        String bodyOfException = ex.getConstraintViolations().stream().map(constraintViolation -> constraintViolation.getMessage())
                .collect(Collectors.joining());
        ExceptionMessage exceptionMessage = new ExceptionMessage(HttpStatus.BAD_REQUEST, bodyOfException);
        return new ResponseEntity<>(exceptionMessage, exceptionMessage.getHttpStatus());
    }

    @ExceptionHandler(value =
            PSQLException.class)
    protected ResponseEntity<Object> handleConflict(PSQLException ex) {
        String bodyOfException = ex.getMessage();
        ExceptionMessage exceptionMessage = new ExceptionMessage(HttpStatus.BAD_REQUEST, bodyOfException);
        return new ResponseEntity<>(exceptionMessage, exceptionMessage.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        MethodArgumentsNotValidMessage message = new MethodArgumentsNotValidMessage(HttpStatus.BAD_REQUEST, errors);
        return new ResponseEntity<>(message, message.getHttpStatus());
    }
}