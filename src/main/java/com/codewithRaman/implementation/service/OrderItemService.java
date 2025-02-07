package com.codeWithRaman.implementation.service;

import com.codeWithRaman.implementation.model.OrderItem;
import com.codeWithRaman.implementation.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    // Save a new order item
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    // Get order items by order ID
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        // You can write custom queries to get order items based on the order ID
        return orderItemRepository.findAll();  // Adjust this if needed
    }
}
