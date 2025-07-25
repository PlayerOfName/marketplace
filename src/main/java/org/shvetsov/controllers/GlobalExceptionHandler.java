package org.shvetsov.controllers;

import org.shvetsov.ApiError;
import org.shvetsov.comment.NotFoundCommentException;
import org.shvetsov.product.ForbiddenException;
import org.shvetsov.product.NotFoundProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        return ResponseEntity.badRequest()
                .body(new ApiError("Validation failed", errors));
    }

    @ExceptionHandler(NotFoundProductException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleProductNotFound(NotFoundProductException ex) {
        return new ApiError(
                ex.getMessage(),
                Map.of("productId", "Продукт не найден")
        );
    }

    @ExceptionHandler(NotFoundCommentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCommentNotFound(NotFoundCommentException ex) {
        return new ApiError(
                ex.getMessage(),
                Map.of("comment", "Продукт не найден")
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbidden(ForbiddenException ex) {
        return new ApiError(
                ex.getMessage(),
                Map.of("access", "Недостаточно прав")
        );
    }
}
