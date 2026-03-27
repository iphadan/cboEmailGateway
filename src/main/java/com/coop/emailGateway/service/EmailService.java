package com.coop.emailGateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

@Service
public class EmailService {


    private static String smtpAddress;

    private  static String smtpUsername;

    private  static String smtpPassword;

    private  static String staffMailAddresses;

    @Value("${staff.mail.addresses}")
    public void setStaffMailAddresses(String address) {
        EmailService.staffMailAddresses = address;
    }
    @Value("${smtp.address}")
    public void setSmtpAddress(String address) {
        EmailService.smtpAddress = address;
    }
    @Value("${smtp.username}")
    public void setSmtpUsername(String username) {
        EmailService.smtpUsername = username;
    }
    @Value("${smtp.password}")
    public void setSmtpPassword(String password) {
        EmailService.smtpPassword = password;
    }


    public static MimeMessage createMimeMessage(String sender,String email, String subject, String body, String attachmentPath) throws AddressException {
       String[] recipients = staffMailAddresses.split(",");
   //    String[] recipients = {"ephrem.daniel@coopbankoromiasc.com"};

        InternetAddress address = new InternetAddress();
        address.setAddress(email);


        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", smtpAddress);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.timeout", "15000");

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUsername, smtpPassword);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipient(Message.RecipientType.TO,address);
            message.setSubject(subject);

            // HTML body
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html; charset=utf-8");  // ✅ HTML here

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Optional attachment
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentPath);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(new File(attachmentPath).getName());
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);  // ✅ just set the multipart
            return message;

        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }


    static void sendEmail(MimeMessage message) {
        try {
            Transport.send(message);
            System.out.println("" +
                    "DC2 Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}