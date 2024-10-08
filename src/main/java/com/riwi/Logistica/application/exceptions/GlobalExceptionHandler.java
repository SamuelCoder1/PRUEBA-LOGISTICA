package com.riwi.Logistica.application.exceptions;


import com.riwi.Logistica.application.dtos.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenericNotFoundExceptions.class)
    public ResponseEntity<String> handleGenericNotFound(GenericNotFoundExceptions ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CapacityExceededException.class)
    public ResponseEntity<String> handleGenericCapacityExceeded(CapacityExceededException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<String> handlePdfGenerationException(PdfGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generando PDF: " + ex.getMessage());
    }

    @ExceptionHandler(AuditNotFoundException.class)
    public ResponseEntity<String> handleAuditNotFoundException(AuditNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auditoría no encontrada: " + ex.getMessage());
    }
}
