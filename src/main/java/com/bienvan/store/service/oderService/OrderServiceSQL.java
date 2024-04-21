package com.bienvan.store.service.oderService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bienvan.store.model.Order;
import com.bienvan.store.model.User;
import com.bienvan.store.repository.Factory_Method.OrderRepository_SQL;

@Service
public class OrderServiceSQL extends OrderService {
    @Autowired
    OrderRepository_SQL orderRepositorySQL;

    public Order createOrder(Order order) {
        return orderRepositorySQL.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepositorySQL.findAll();
    }

    public List<Order> getOrdersByUserId(User entity) {
        return orderRepositorySQL.findAllByUserId(entity);
    }

    public List<Order> getOrdersByUserId(Long id){
        return orderRepositorySQL.findOrdersByUserId(id);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepositorySQL.findById(id);
    }

    public void deleteOrder(Long id) {
        orderRepositorySQL.deleteById(id);
    }
}

