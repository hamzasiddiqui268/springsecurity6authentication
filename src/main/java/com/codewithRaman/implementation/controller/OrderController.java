package com.codeWithRaman.implementation.controller;

import com.codeWithRaman.implementation.model.Order;
import com.codeWithRaman.implementation.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //@GetMapping("/orders")
    //public String showAllOrders(Model model) {
    // model.addAttribute("orders", orderService.getAllOrders());
    //return "orders"; // Thymeleaf template for viewing orders
    // }

    @PostMapping("/user/checkout")
    public String checkout(Model model, Order order) {
        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user (or you can fetch the full User object if needed)
        String username = authentication.getName();

        // Add the username to the model
        model.addAttribute("username", username);
        Order savedOrder = orderService.saveOrder(order);
        System.out.println("Order saved with ID: " + savedOrder.getId());
        return "redirect:/orders"; // Redirect to the orders page after checkout
    }


    /*@GetMapping("/orders")
    public String viewOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        // Accessing the orderItems will trigger lazy loading
        for (Order order : orders) {
            // Force Hibernate to load the orderItems by accessing them
            order.getOrderItems().forEach(item -> {});  // Access the list to trigger loading
        }
        model.addAttribute("orders", orders);
        return "orders"; // Render orders.html template
    }*/
    @GetMapping("/user/orders")
    public String viewOrders(Model model) {
        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user (or you can fetch the full User object if needed)
        String username = authentication.getName();

        // Add the username to the model
        model.addAttribute("username", username);

        List<Order> orders = orderService.getAllOrders();
        if (orders == null || orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            orders.forEach(order -> {
                System.out.println("Order ID: " + order.getId());
                order.getOrderItems().forEach(item -> {
                    System.out.println("Item: " + item.getBeverage().getName());
                });
            });
        }
        model.addAttribute("orders", orders == null ? new ArrayList<>() : orders);
        return "orders"; // Render orders.html template
    }



}
