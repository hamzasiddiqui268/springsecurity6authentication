package com.codeWithRaman.implementation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    private double price;

    private int quantity = 1; // Default quantity

    @ManyToOne
    @JoinColumn(name = "beverage_id")
    private Beverage beverage; // This is the reference to the Beverage entity

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order; // The order to which this item belongs

    // Getters and Setters

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getPrice() {
        return price * quantity; // Return price based on quantity
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Beverage getBeverage() {
        return beverage;
    }

    public void setBeverage(Beverage beverage) {
        this.beverage = beverage;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order; // Set the order for the order item
    }
}
