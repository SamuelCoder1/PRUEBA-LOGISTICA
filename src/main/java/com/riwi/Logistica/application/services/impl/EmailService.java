package com.riwi.Logistica.application.services.impl;

import com.riwi.Logistica.application.dtos.requests.ReportRequest;
import com.riwi.Logistica.domain.entities.Carga;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendReport(Carga carga, ReportRequest request) {
        String subject = "Carga damage report: " + carga.getId();
        String body = "Damage has been reported to the cargo with ID: " + carga.getId() +
                "\nDetails: " + request.getDetalles();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("daviocassio95@gmail.com");
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
        } catch (MailSendException e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*private final TransactionalEmailsApi transactionalEmailsApi;

    @Autowired
    public EmailService(TransactionalEmailsApi transactionalEmailsApi) {
        this.transactionalEmailsApi = transactionalEmailsApi;
    }

    public void sendEmail(EmailRequest emailRequest) {
        // Crea el objeto SendSmtpEmail
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail()
                .sender(new SendSmtpEmailSender().email("tu-email@ejemplo.com")) // Cambia esto por tu dirección de envío
                .to(Collections.singletonList(new SendSmtpEmailTo().email(emailRequest.getTo())))
                .subject(emailRequest.getSubject())
                .htmlContent(emailRequest.getBody());

        try {
            // Envía el correo
            transactionalEmailsApi.sendTransacEmail(sendSmtpEmail);
        } catch (Exception e) {
            // Manejo de errores
            System.err.println("Error al enviar el correo: " + e.getMessage());
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }*/
}
