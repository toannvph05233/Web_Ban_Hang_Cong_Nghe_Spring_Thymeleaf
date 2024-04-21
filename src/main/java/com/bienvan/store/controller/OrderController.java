package com.bienvan.store.controller;

import com.bienvan.store.command_Command.CommandFactory;
import com.bienvan.store.command_Command.OrderCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bienvan.store.model.Order;
import com.bienvan.store.payload.response.MessageResponse;
import com.bienvan.store.service.EmailService;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private CommandFactory commandFactory;

    @Autowired
    private EmailService emailService;

    // Here is the code to send the
    @PutMapping
    public ResponseEntity<?> updateOrder(@RequestBody Order order) throws MessagingException {
        OrderCommand createOrderCommand = commandFactory.createOrderCommand(order);
        Order order1 = createOrderCommand.execute();

        if (order1 != null) {
            if (order.getStatus().equals("Đang giao")) {
                String to = order1.getUserId().getEmail();
                String subject = "Thông báo đơn hàng #" + order1.getId() + " của quý khách đã được tiếp nhận";
                System.out.println("dang giao");
                emailService.sendDynamicHtmlEmail(to, subject, order);
            }
            return ResponseEntity.ok(new MessageResponse(0, "Update order success",null));

        }
        return ResponseEntity.ok(new MessageResponse(1, "Update order failed",null));
    }

}
