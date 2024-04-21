package com.bienvan.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bienvan.store.payload.request.EmailRequest;
import com.bienvan.store.service.EmailService;

import javax.mail.MessagingException;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public String sendEmail() throws MessagingException {
        String to = "khanhungvy@gmail.com"; // Replace with the recipient's email address
        String subject = "Test Email";
        String text = "<h1>Hello from Spring Boot!</h1><p>This is an HTML-formatted email.</p>";

        emailService.sendEmail(to, subject, text);

        return "Email sent successfully!";
    }

    @PostMapping("/send-dynamic-html-email")
    public String sendDynamicHtmlEmail(@RequestBody EmailRequest emailRequest) {
        // String to = emailRequest.getTo();
        // String subject = emailRequest.getSubject();
        // String title = emailRequest.getTitle();
        // String message = emailRequest.getMessage();

        // try {
            // emailService.sendDynamicHtmlEmail(to, subject, title, message);
            return "Dynamic HTML Email sent successfully!";
        // } catch (MessagingException e) {
        //     return "Error sending Dynamic HTML Email: " + e.getMessage();
        // }
    }

}
