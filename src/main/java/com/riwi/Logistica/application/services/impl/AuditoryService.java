package com.riwi.Logistica.application.services.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.riwi.Logistica.domain.entities.AuditRecord;
import com.riwi.Logistica.infrastructure.persistence.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class AuditoryService {

    @Autowired
    private AuditRepository auditRepository;

    public List<AuditRecord> getAuditHistory() {
        return auditRepository.findAll();
    }

    public ByteArrayOutputStream generatePdf() throws DocumentException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();

        // Usar el ByteArrayOutputStream para escribir el PDF
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        List<AuditRecord> auditRecords = getAuditHistory();
        if (auditRecords.isEmpty()) {
            document.add(new Paragraph("No hay registros de auditoría disponibles."));
        } else {
            for (AuditRecord record : auditRecords) {
                document.add(new Paragraph("Entity: " + record.getEntityName()));
                document.add(new Paragraph("Action: " + record.getAction()));
                document.add(new Paragraph("User: " + record.getUser()));
                document.add(new Paragraph("Timestamp: " + record.getTimestamp()));
                document.add(new Paragraph("Details: " + record.getDetails()));
                document.add(new Paragraph("\n"));
            }
        }

        document.close(); // Cerrar el documento
        return byteArrayOutputStream; // Devolver el ByteArrayOutputStream
    }
}
