package com.codeWithRaman.implementation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.CascadeType;
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated ID for the order

    @Positive
    private double price; // The total price of the order, must be > 0

    // One order can have many order items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @NotNull
    //@Size(min = 1, message = "An order must have at least one item")
    private List<OrderItem> orderItems = new ArrayList<>(); // Initialize to avoid null

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
