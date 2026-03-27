package com.coop.emailGateway.controller;

import com.coop.emailGateway.model.EmailSend;
import com.coop.emailGateway.service.EmailMessaging;
import com.coop.emailGateway.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller("/mail/api")
public class EmailSenderController {
    @Autowired
private EmailMessaging emailMessaging;
@PostMapping
    public void sendEmail(EmailSend emailSend) throws JsonProcessingException {
    emailMessaging.sendNotification(emailSend);


}
}
