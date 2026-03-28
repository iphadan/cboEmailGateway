package com.coop.emailGateway.service;

import com.coop.emailGateway.model.EmailResponse;
import com.coop.emailGateway.model.EmailSend;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

@Service
@Slf4j
public class EmailMessaging {

@Value("${queue.mail.address}")
private  String mailAddress;
    @Async
    public ResponseEntity<EmailResponse> sendEmailAndSendResponse(EmailSend emailSend) throws JsonProcessingException {
        try {


            String formattedDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            if(emailSend.getEmail().isEmpty() ){
                System.out.println("❌ Failed to create email.");
                return new ResponseEntity<>(new EmailResponse(true, "Mail Address is not provided. Sending Mail Failed."), HttpStatus.EXPECTATION_FAILED);
            }
            if(emailSend.getBody().isEmpty()){
                System.out.println("❌ Failed to create email.");
                return new ResponseEntity<>(new EmailResponse(true, "Mail Body is not provided. Sending Mail Failed."), HttpStatus.EXPECTATION_FAILED);
            }
            if(emailSend.getName().isEmpty()){
                emailSend.setName("Our Valued Customer");
            }
            if(emailSend.getSubject().isEmpty()){
                emailSend.setSubject("Notification from Coop");
            }
            if(emailSend.getHeader().isEmpty()){
                emailSend.setHeader("Notification from Coop");

            }
            // HTML body
            String htmlBody =
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "  <meta charset='UTF-8'>" +
                            "</head>" +
                            "<body style='font-family: Arial, sans-serif; margin:0; padding:0; background-color:#f4f6f8;'>" +

                            "  <div style='max-width:600px; margin:20px auto; background-color:#ffffff; border-radius:8px; box-shadow:0 2px 4px rgba(0,0,0,0.1); padding:20px;'>" +

                            "    <!-- Header with Logo -->" +
                            "    <div style='text-align:center; margin-bottom:20px;'>" +
                            "      <img src='https://coopbankoromia.com.et/wp-content/uploads/2020/11/Coopbank-Logo-Ethiopia.svg' alt='Coop Bank Logo' style='height:60px;' />" +
                            "      <h2 style='color:#2FADDA; margin:10px 0; font-weight:bold;'>" +
                            emailSend.getHeader() +
                            "      </h2>" +
                            "    </div>" +

                            "    <!-- Greeting -->" +
                            "    <p>Dear <strong>" + emailSend.getName() + "</strong>,</p>" +

                            "    <!-- Body -->" +
                            "    <p style='color:#333333; font-size:14px; line-height:1.6;'>" +
                            emailSend.getBody().replace("\n", "<br/>") +
                            "    </p>" +

                            "    <!-- Closing -->" +
                            "    <p style='margin-top:20px;'>Best regards,<br/>" +
                            "    <strong>Cooperative Bank of Oromia S.C.</strong></p>" +

                            "  </div>" +

                            "  <!-- Footer (from second template) -->" +
                            "  <div style='max-width:600px; margin:0 auto; text-align:center; padding:12px 16px; color:#6a7b82; font-size:12px;background-color:white;'>" +

                            "    <div style='margin-top:10px;'>" +
                            "      <strong>Need help?</strong><br/>" +
                            "      Call: <a href='tel:609' style='color:#1CD5FF; font-weight:bold; text-decoration:none;'>609</a> | " +
                            "      Visit: <a href='https://www.coopbankoromia.com' style='color:#1CD5FF; font-weight:bold; text-decoration:none;'>www.coopbankoromia.com</a>" +
                            "    </div>" +

                            "    <div style='margin-top:10px;'>© " + java.time.Year.now() + " Cooperative Bank of Oromia. All rights reserved.</div>" +
                            "    <div style='margin-top:5px;'>Designed by Application Team</div>" +

                            "    <!-- Social Links -->" +
                            "    <div style='margin-top:12px;'>" +
                            "      <a href='https://www.facebook.com/share/1BiexZbHMx/' style='display:inline-block;width:28px;height:28px;border-radius:4px;background:#ffffff;border:1px solid #e1eef1;line-height:28px;text-align:center;color:#007c91;font-weight:700;text-decoration:none;margin-right:6px;'>f</a>" +
                            "      <a href='https://www.linkedin.com/in/ephrem-daniel-31966b227/' style='display:inline-block;width:28px;height:28px;border-radius:4px;background:#ffffff;border:1px solid #e1eef1;line-height:28px;text-align:center;color:#007c91;font-weight:700;text-decoration:none;'>in</a>" +
                            "    </div>" +

                            "  </div>" +

                            "</body>" +
                            "</html>";



            // Create the message (no attachment for now)
            MimeMessage message = EmailService.createMimeMessage(mailAddress, emailSend.getEmail(), emailSend.getSubject(), htmlBody, null);

            if (message != null) {
                Transport.send(message);
                System.out.println("✅ Report email sent successfully!");

            } else {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("logs/logs"+
                        LocalDate.now() +".txt", true))) {
                    writer.write("#");
                    writer.write("Sending Mail to " + emailSend.getEmail() + "Failed"  + LocalDate.now());
                    writer.newLine();
                    writer.write(emailSend.getEmail());
                    writer.newLine();
                    writer.write(emailSend.getBody());
                    writer.newLine();
                    writer.write(emailSend.getSubject());
                    writer.newLine();
                    writer.write(emailSend.getHeader());
                    writer.newLine();
                    writer.write(emailSend.getName());
                    writer.write("#");
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("❌ Failed to create email.");
                return new ResponseEntity<>(new EmailResponse(true, "Sending Mail to  " + emailSend.getEmail() + " Failed."), HttpStatus.EXPECTATION_FAILED);
            }


        } catch (Exception e) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("logs/logs"+
                    LocalDate.now() +".txt", true))) {
                writer.write("#");
                writer.write("Sending Mail to " + emailSend.getEmail() + "Failed"  + LocalDate.now());
                writer.newLine();
                writer.write(emailSend.getEmail());
                writer.newLine();
                writer.write(emailSend.getBody());
                writer.newLine();
                writer.write(emailSend.getSubject());
                writer.newLine();
                writer.write(emailSend.getHeader());
                writer.newLine();
                writer.write(emailSend.getName());
                writer.write("#");
                writer.newLine();
            } catch (IOException io) {
                throw new RuntimeException(io);
            }
            e.printStackTrace();
            return new ResponseEntity<>(new EmailResponse(true, "Sending Mail to " + emailSend.getEmail() + " Failed"), HttpStatus.EXPECTATION_FAILED);


        }
        return new ResponseEntity<>(new EmailResponse(true, "Mail Successfully Sent to " + emailSend.getEmail()), HttpStatus.ACCEPTED);

    }
    @Async
    public void sendNotification(EmailSend emailSend) throws JsonProcessingException {
        try {



            String formattedDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            if(emailSend.getEmail().isEmpty()){
                System.out.println("❌ Failed to create email.");
                throw new Exception("Mail Address is not provided");
            }
            if(emailSend.getBody().isEmpty()){
                System.out.println("❌ Failed to create email.");
                throw new Exception("Mail Body is not provided");
            }
            if(emailSend.getName().isEmpty()){
                emailSend.setName("Our Valued Customer");
            }
            if(emailSend.getSubject().isEmpty()){
                emailSend.setSubject("Notification from Coop");
            }
            if(emailSend.getHeader().isEmpty()){
                emailSend.setHeader("Notification from Coop");

            }
            // HTML body
            String htmlBody =
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "  <meta charset='UTF-8'>" +
                            "</head>" +
                            "<body style='font-family: Arial, sans-serif; margin:0; padding:0; background-color:#f4f6f8;'>" +

                            "  <div style='max-width:600px; margin:20px auto; background-color:#ffffff; border-radius:8px; box-shadow:0 2px 4px rgba(0,0,0,0.1); padding:20px;'>" +

                            "    <!-- Header with Logo -->" +
                            "    <div style='text-align:center; margin-bottom:20px;'>" +
                            "      <img src='https://coopbankoromia.com.et/wp-content/uploads/2020/11/Coopbank-Logo-Ethiopia.svg' alt='Coop Bank Logo' style='height:60px;' />" +
                            "      <h2 style='color:#2FADDA; margin:10px 0; font-weight:bold;'>" +
                            emailSend.getHeader() +
                            "      </h2>" +
                            "    </div>" +

                            "    <!-- Greeting -->" +
                            "    <p>Dear <strong>" + emailSend.getName() + "</strong>,</p>" +

                            "    <!-- Body -->" +
                            "    <p style='color:#333333; font-size:14px; line-height:1.6;'>" +
                            emailSend.getBody().replace("\n", "<br/>") +
                            "    </p>" +

                            "    <!-- Closing -->" +
                            "    <p style='margin-top:20px;'>Best regards,<br/>" +
                            "    <strong>Cooperative Bank of Oromia S.C.</strong></p>" +

                            "  </div>" +

                            "  <!-- Footer (from second template) -->" +
                            "  <div style='max-width:600px; margin:0 auto; text-align:center; padding:12px 16px; color:#6a7b82; font-size:12px;background-color:white;'>" +

                            "    <div style='margin-top:10px;'>" +
                            "      <strong>Need help?</strong><br/>" +
                            "      Call: <a href='tel:609' style='color:#1CD5FF; font-weight:bold; text-decoration:none;'>609</a> | " +
                            "      Visit: <a href='https://www.coopbankoromia.com' style='color:#1CD5FF; font-weight:bold; text-decoration:none;'>www.coopbankoromia.com</a>" +
                            "    </div>" +

                            "    <div style='margin-top:10px;'>© " + java.time.Year.now() + " Cooperative Bank of Oromia. All rights reserved.</div>" +
                            "    <div style='margin-top:5px;'>Designed by Application Team</div>" +

                            "    <!-- Social Links -->" +
                            "    <div style='margin-top:12px;'>" +
                            "      <a href='https://www.facebook.com/share/1BiexZbHMx/' style='display:inline-block;width:28px;height:28px;border-radius:4px;background:#ffffff;border:1px solid #e1eef1;line-height:28px;text-align:center;color:#007c91;font-weight:700;text-decoration:none;margin-right:6px;'>f</a>" +
                            "      <a href='https://www.linkedin.com/in/ephrem-daniel-31966b227/' style='display:inline-block;width:28px;height:28px;border-radius:4px;background:#ffffff;border:1px solid #e1eef1;line-height:28px;text-align:center;color:#007c91;font-weight:700;text-decoration:none;'>in</a>" +
                            "    </div>" +

                            "  </div>" +

                            "</body>" +
                            "</html>";

            // Create the message (no attachment for now)
            MimeMessage message = EmailService.createMimeMessage(mailAddress, emailSend.getEmail(),emailSend.getSubject(), htmlBody, null);

            if (message != null) {
                Transport.send(message);
                System.out.println("✅ Report email sent successfully!");
            } else {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("logs/logs"+
                        LocalDate.now() +".txt", true))) {
                    writer.write("#");
                    writer.write("Sending Mail to " + emailSend.getEmail() + "Failed"  + LocalDate.now());
                    writer.newLine();
                    writer.write(emailSend.getEmail());
                    writer.newLine();
                    writer.write(emailSend.getBody());
                    writer.newLine();
                    writer.write(emailSend.getSubject());
                    writer.newLine();
                    writer.write(emailSend.getHeader());
                    writer.newLine();
                    writer.write(emailSend.getName());
                    writer.write("#");
                    writer.newLine();
                } catch (IOException io) {
                    throw new RuntimeException(io);
                }
                System.out.println("❌ Failed to create email.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}





