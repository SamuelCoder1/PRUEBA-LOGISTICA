package com.riwi.Logistica.controllers;

import com.riwi.Logistica.application.services.impl.AuditoryService;
import com.riwi.Logistica.domain.entities.AuditRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/audit")
public class AuditoryController {

    @Autowired
    private AuditoryService auditoryService;

    @GetMapping("/logs")
    public ResponseEntity<?> generatePdfAndGetHistory() {
        try {
            // Generar el PDF en un ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = auditoryService.generatePdf();
            byte[] pdfContent = byteArrayOutputStream.toByteArray();

            // Obtener el historial de auditoría
            List<AuditRecord> auditRecords = auditoryService.getAuditHistory();

            // Preparar la respuesta con el PDF
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=auditoria.pdf");
            headers.add("Content-Type", "application/pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent); // Devuelve el contenido del PDF
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generando el PDF: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }
}
