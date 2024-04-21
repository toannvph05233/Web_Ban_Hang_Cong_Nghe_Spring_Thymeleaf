package com.bienvan.store.repository.Factory_Method;

import com.bienvan.store.model.Order;
import com.bienvan.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository_HQL extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(User userId);

    @Query(value = "SELECT o FROM Order o")
    List<Order> findOrdersByUserId(Long userId);

}
