package com.bienvan.store.service.emailService;

import com.bienvan.store.model.OrderItem;
import org.springframework.stereotype.Service;

import com.bienvan.store.model.Order;

@Service
public class GmailEmailService extends EmailService {

    @Override
    public String prepareContent(Order order) {
        StringBuilder content = new StringBuilder();
        content.append("Dear ").append(order.getUserId()).append(",<br><br>");
        content.append("Your order has been processed successfully.<br>");
        content.append("Here are the details of your order:<br>");
        // Thêm các chi tiết đơn hàng vào nội dung email
         for (OrderItem item : order.getOrderItems()) {
             content.append(item.getProduct().getName()).append(": ").append(item.getQuantity()).append("<br>");
         }
        return content.toString();
    }
}

