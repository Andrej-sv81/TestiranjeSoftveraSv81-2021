package com.example.SpringfieldEDS.service;

import com.ftn.ts.model.EmailDetails;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("src/main/resources/templates/verification_email.html")
    private String templatePathVerification;
    @Value("src/main/resources/templates/acceptance_email.html")
    private String templatePathAcceptance;
    @Value("src/main/resources/templates/rejection_email.html")
    private String templatePathRejection;

    @Value("http://localhost:8080/api/users/registration/verification/")
    private String verificationLink;

    public boolean sendSimpleMail(EmailDetails details, String name, String address, String rejection, int type) {

        try {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

            helper.setSubject(details.getSubject());
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());

            String messageBody = switch (type) {
                case 1 -> createVerificationBody(details, name);
                case 2 -> createAcceptanceBody(name, address);
                case 3 -> createRejectionBody(name, address, rejection);
                default -> "";
            };

            helper.setText(messageBody, true);
            javaMailSender.send(mailMessage);
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private String createVerificationBody(EmailDetails details, String name) throws IOException {
        //Ovde ce trebati da se zameni details.getRecipient(); sa tokenom
        var messageBody = new String(Files.readAllBytes(Paths.get(templatePathVerification)));
        return messageBody
                .replace("{{name}}", name)
                .replace("{{link}}", verificationLink + details.getRecipient());
    }

    private String createAcceptanceBody(String name, String address) throws IOException {
        var messageBody = new String(Files.readAllBytes(Paths.get(templatePathAcceptance)));
        return messageBody
                .replace("{{name}}", name)
                .replace("{{address}}", address);    }

    private String createRejectionBody(String name, String address, String rejection) throws IOException {
        var messageBody = new String(Files.readAllBytes(Paths.get(templatePathRejection)));
        return messageBody
                .replace("{{name}}", name)
                .replace("{{address}}", address)
                .replace("{{rejection_text}}", rejection);
    }

}
