package com.coop.emailGateway.controller;

import com.coop.emailGateway.model.EmailResponse;
import com.coop.emailGateway.model.EmailSend;
import com.coop.emailGateway.service.EmailMessaging;
import com.coop.emailGateway.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Controller("/mail/api")
public class EmailSenderController {
    @Autowired
private EmailMessaging emailMessaging;
@PostMapping("/send")
    public void sendEmail(@RequestBody EmailSend emailSend) throws JsonProcessingException {
    emailMessaging.sendNotification(emailSend);


}
    @PostMapping("/sendWithConfirmation")
    public ResponseEntity<EmailResponse> sendEmailAndSendResponse(@RequestBody EmailSend emailSend) throws JsonProcessingException {
        return emailMessaging.sendEmailAndSendResponse(emailSend);


    }
}
