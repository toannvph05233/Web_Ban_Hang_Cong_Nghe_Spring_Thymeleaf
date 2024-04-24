package com.bienvan.store.service.emailService;

import com.bienvan.store.model.OrderItem;
import org.springframework.stereotype.Service;

import com.bienvan.store.model.Order;

@Service
public class OutlookEmailService extends EmailService {

    @Override
    public String prepareContent(Order order) {
        StringBuilder content = new StringBuilder();
        content.append("Hi ").append(order.getUserId()).append(",\n\n");
        content.append("Your order has been successfully processed.\n");
        content.append("Here are the details of your order:\n");
        // Thêm các chi tiết đơn hàng vào nội dung email
         for (OrderItem item : order.getOrderItems()) {
             content.append(item.getProduct().getName()).append(": ").append(item.getQuantity()).append("\n");
         }
        return content.toString();
    }
}
