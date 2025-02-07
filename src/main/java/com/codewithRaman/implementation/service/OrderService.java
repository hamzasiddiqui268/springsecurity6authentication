package com.codeWithRaman.implementation.service;

import com.codeWithRaman.implementation.model.Order;
import com.codeWithRaman.implementation.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Save a new order
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Get all orders
    public List<Order> getAllOrders() {
        //return orderRepository.findAll(); // This will now work correctly
        return orderRepository.findAllWithOrderItems();
    }
}
