package com.codeWithRaman.implementation.repository;

import com.codeWithRaman.implementation.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Custom queries can be added here
}
