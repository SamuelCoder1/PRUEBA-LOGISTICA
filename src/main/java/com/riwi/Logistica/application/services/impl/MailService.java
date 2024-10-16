package com.riwi.Logistica.application.services.impl;

import com.riwi.Logistica.application.dtos.requests.EmailRequest;
import com.riwi.Logistica.application.dtos.requests.ReportRequest;
import com.riwi.Logistica.domain.entities.Carga;
import com.riwi.Logistica.domain.ports.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Service
public class MailService implements IMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendReport(Carga carga, ReportRequest request) {
        String subject = "Carga damage report: " + carga.getId();
        String body = "Damage has been reported to the cargo with ID: " + carga.getId() +
                "\nDetails: " + request.getDetalles();

        SimpleMailMessage message = new SimpleMailMessage();

        String[] to = {
                "daviocassio95@gmail.com",
                "samuelparkourito132@gmail.com",
                "osorioemanuel0520@gmail.com"
        };

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
        } catch (MailSendException e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmail(EmailRequest emailRequest) {
        // Método opcional para enviar correos genéricos
    }
}
