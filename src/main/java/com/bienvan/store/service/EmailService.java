package com.bienvan.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.bienvan.store.model.Order;
import com.bienvan.store.model.OrderItem;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    OrderItemService orderItemService;

    public void sendDynamicHtmlEmail(String to, String subject, Order order)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);

        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrder(order);

        Context context = new Context();
        context.setVariable("order", order);
        context.setVariable("orderItems", orderItems);
        String htmlContent = templateEngine.process("email-template", context);

        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendPasswordResetEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Lấy lại mật khẩu");
        message.setText("Xin chào,\n\nBạn đã yêu cầu lấy lại mật khẩu.\nVui lòng truy cập đường dẫn sau để đặt lại mật khẩu của bạn:\n" + resetLink);
        javaMailSender.send(message);
    }
}
