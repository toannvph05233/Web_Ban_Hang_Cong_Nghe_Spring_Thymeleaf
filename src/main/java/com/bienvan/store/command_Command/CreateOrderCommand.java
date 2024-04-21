package com.bienvan.store.command_Command;

import com.bienvan.store.model.Order;
import com.bienvan.store.service.oderService.OrderServiceSQL;

import java.util.Optional;

public class CreateOrderCommand implements OrderCommand {
    private OrderServiceSQL orderServiceSQL;
    private Order order;

    public CreateOrderCommand(OrderServiceSQL orderServiceSQL, Order order) {
        this.orderServiceSQL = orderServiceSQL;
        this.order = order;
    }

    @Override
    public Order execute() {
        Optional<Order> optional = orderServiceSQL.getOrderById(order.getId());

        if (optional.isPresent()) {
            Order existingOrder = optional.get();
            existingOrder.setStatus(order.getStatus());
            orderServiceSQL.createOrder(order);
            return order;
        }
        return null;
    }
}

