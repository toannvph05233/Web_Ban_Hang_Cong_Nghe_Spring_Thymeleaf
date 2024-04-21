package com.bienvan.store.command_Command;

import com.bienvan.store.model.Order;
import com.bienvan.store.service.oderService.OrderServiceSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandFactory {
    private OrderServiceSQL orderServiceSQL;

    @Autowired
    public CommandFactory(OrderServiceSQL orderServiceSQL) {
        this.orderServiceSQL = orderServiceSQL;
    }

    public OrderCommand createOrderCommand(Order order) {
        return new CreateOrderCommand(orderServiceSQL, order);
    }
}

