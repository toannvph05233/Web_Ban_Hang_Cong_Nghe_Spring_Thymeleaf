package com.bienvan.store.service.oderService;

import com.bienvan.store.model.Order;
import com.bienvan.store.model.User;
import com.bienvan.store.repository.Factory_Method.OrderRepository_HQL;
import com.bienvan.store.repository.Factory_Method.OrderRepository_SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceHQL extends OrderService {
    @Autowired
    OrderRepository_HQL orderRepositoryHQL;

    public Order createOrder(Order order) {
        return orderRepositoryHQL.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepositoryHQL.findAll();
    }

    public List<Order> getOrdersByUserId(User entity) {
        return orderRepositoryHQL.findAllByUserId(entity);
    }

    public List<Order> getOrdersByUserId(Long id){
        return orderRepositoryHQL.findOrdersByUserId(id);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepositoryHQL.findById(id);
    }

    public void deleteOrder(Long id) {
        orderRepositoryHQL.deleteById(id);
    }
}

