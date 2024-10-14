package com.company.ecommerce.customer.handler;

import com.company.ecommerce.customer.exception.BusinessException;
import com.company.ecommerce.customer.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleException(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(FORBIDDEN).body(problemDetail);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, ex.getMessage());
        return ResponseEntity.status(CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(BusinessException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, ex.getMessage());
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> validationMap = new HashMap<>();
        List<ObjectError> validations = ex.getBindingResult().getAllErrors();

        validations.forEach(validation -> {
            String fieldName = ((FieldError) validation).getField();
            String validationMessage = validation.getDefaultMessage();
            validationMap.put(fieldName, validationMessage);
        });
        ProblemDetail problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
        problemDetail.setProperty("validations", validationMap);
        return ResponseEntity.status(BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, Object> violationMap = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        violations.forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String violationMessage = violation.getMessage();
            violationMap.put(fieldName, violationMessage);
        });
        ProblemDetail problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
        problemDetail.setProperty("validations", violationMap);
        return ResponseEntity.status(BAD_REQUEST).body(problemDetail);
    }

}
