package com.ftn.ts.service;

import com.ftn.ts.model.EmailDetails;
import jakarta.mail.MessagingException;
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
    @Value("src/main/resources/templates/verification.html")
    private String templatePathVerification;
    @Value("http://localhost:8080/api/user/registration/verification/")
    private String verificationLink;

    public void sendSimpleMail(EmailDetails details, String name) throws MessagingException, IOException {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
            helper.setSubject(details.getSubject());
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            String messageBody = createVerificationBody(details, name);
            helper.setText(messageBody, true);
            javaMailSender.send(mailMessage);
    }
    private String createVerificationBody(EmailDetails details, String name) throws IOException {
        var messageBody = new String(Files.readAllBytes(Paths.get(templatePathVerification)));
        return messageBody
                .replace("{{name}}", name)
                .replace("{{link}}", verificationLink + details.getRecipient());
    }


}
