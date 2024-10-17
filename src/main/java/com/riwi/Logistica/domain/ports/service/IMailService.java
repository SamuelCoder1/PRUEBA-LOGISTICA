package com.riwi.Logistica.domain.ports.service;

import com.riwi.Logistica.application.dtos.requests.EmailRequest;
import com.riwi.Logistica.application.dtos.requests.ReportRequest;
import com.riwi.Logistica.domain.entities.Carga;

import java.io.File;

public interface IMailService {


    //GENERICO
    void sendEmail(EmailRequest emailRequest);
  
    //COMO SE NECESITE
    void sendReport(Carga carga, ReportRequest request);

    //void sendEmailWithFile(EmailRequest emailRequest, File file);
}