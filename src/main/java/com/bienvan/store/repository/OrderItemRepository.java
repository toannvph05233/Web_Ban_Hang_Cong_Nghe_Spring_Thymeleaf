package com.bienvan.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bienvan.store.model.Order;
import com.bienvan.store.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
        List<OrderItem> findAllByOrder(Order order);
}