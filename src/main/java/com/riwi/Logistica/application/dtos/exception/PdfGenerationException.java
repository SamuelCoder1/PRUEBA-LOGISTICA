package com.riwi.Logistica.application.dtos.exception;

public class PdfGenerationException extends RuntimeException {
    public PdfGenerationException(String message) {
        super(message);
    }
}