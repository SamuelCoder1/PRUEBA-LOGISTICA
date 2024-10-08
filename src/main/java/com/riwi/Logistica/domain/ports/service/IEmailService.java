package com.riwi.Logistica.domain.ports.service;

import com.riwi.Logistica.application.dtos.requests.EmailRequest;

public interface IEmailService {
    void sendEmail(EmailRequest emailRequest);
}