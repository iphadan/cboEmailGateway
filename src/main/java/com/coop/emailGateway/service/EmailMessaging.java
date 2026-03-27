package com.coop.emailGateway.service;

import com.coop.emailGateway.model.EmailSend;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailMessaging {

@Value("${queue.mail.address}")
private  String mailAddress;


    @Async
    public void sendNotification(EmailSend emailSend) throws JsonProcessingException {
        try {



            String formattedDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

            // HTML body
            String htmlBody = "Hey";


            // Create the message (no attachment for now)
            MimeMessage message = EmailService.createMimeMessage(mailAddress, emailSend.getSubject(), htmlBody, null);

            if (message != null) {
                Transport.send(message);
                System.out.println("✅ Report email sent successfully!");
            } else {
                System.out.println("❌ Failed to create email.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}





