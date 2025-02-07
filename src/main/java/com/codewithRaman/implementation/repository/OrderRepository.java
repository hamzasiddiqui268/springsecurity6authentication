package com.codeWithRaman.implementation.repository;

import com.codeWithRaman.implementation.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems")
    List<Order> findAllWithOrderItems();
}
