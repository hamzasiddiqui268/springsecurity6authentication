package com.codeWithRaman.implementation.controller;

import com.codeWithRaman.implementation.model.Beverage;
import com.codeWithRaman.implementation.model.OrderItem;
import com.codeWithRaman.implementation.model.Order;
import com.codeWithRaman.implementation.service.BeverageService;
import com.codeWithRaman.implementation.service.*;
import com.codeWithRaman.implementation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
// Import the HttpServletRequest

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.DecimalFormat;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

@Controller
public class BasketController {

    @Autowired
    private BeverageService beverageService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    /*@GetMapping("/basket")
    public String showBasket(HttpSession session, Model model) {
        List<OrderItem> basket = (List<OrderItem>) session.getAttribute("basket");
        if (basket == null) {
            basket = new ArrayList<>();  // Initialize if it's empty
        }

        // Calculate total price
        double totalPrice = 0;
        for (OrderItem item : basket) {
            totalPrice += item.getPrice();
        }

        // Format the total price to 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedTotalPrice = df.format(totalPrice);

        model.addAttribute("items", basket);
        model.addAttribute("totalPrice", formattedTotalPrice);  // Pass total price to the view
        return "basket";  // Render basket.html template
    }*/
    @GetMapping("/user/basket")
    public String showBasket(HttpSession session, Model model) {

        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user (or you can fetch the full User object if needed)
        String username = authentication.getName();

        // Add the username to the model
        model.addAttribute("username", username);

        // Retrieve the basket from the session
        List<OrderItem> basket = (List<OrderItem>) session.getAttribute("basket");

        // If the basket is null (i.e., no items in the basket), initialize it
        if (basket == null) {
            basket = new ArrayList<>();
        }

        // Calculate the total price
        double totalPrice = 0;
        for (OrderItem item : basket) {
            totalPrice += item.getPrice();
        }

        // Add the basket and the total price to the model so it can be accessed in the Thymeleaf template
        model.addAttribute("items", basket);
        model.addAttribute("totalPrice", totalPrice);

        // Return the name of the Thymeleaf template to render
        return "basket";  // This refers to the "basket.html" template
    }



    // Add beverage to basket
    // BasketController.java
    @PostMapping("/user/add-to-basket")
    public String addToBasket(@RequestParam Long beverageId, HttpSession session, Model model) {
        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user
        String username = authentication.getName();
        model.addAttribute("username", username);

        Beverage beverage = beverageService.getBeverageById(beverageId);

        // Retrieve the current basket from the session or create a new one
        List<OrderItem> basket = (List<OrderItem>) session.getAttribute("basket");
        if (basket == null) {
            basket = new ArrayList<>();
        }

        // Check if the item is already in the basket
        OrderItem existingItem = null;
        for (OrderItem item : basket) {
            if (item.getBeverage().getId().equals(beverageId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            // Increase the quantity if item already exists
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            orderItemService.saveOrderItem(existingItem); // Update in database if needed
        } else {
            // No duplicate found, add a new item
            OrderItem orderItem = new OrderItem();
            orderItem.setBeverage(beverage);
            orderItem.setPrice(beverage.getPrice());
            orderItem.setQuantity(1); // Default quantity

            // Save the new OrderItem to the database
            orderItem = orderItemService.saveOrderItem(orderItem);
            basket.add(orderItem);
        }

        // Save the updated basket back into the session
        session.setAttribute("basket", basket);

        return "redirect:/user/basket"; // Redirect to the basket page to view the updated contents
    }





    @PostMapping("/user/submit-order")
    public String submitOrder(HttpSession session, Model model) {
        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user (or you can fetch the full User object if needed)
        String username = authentication.getName();

        // Add the username to the model
        model.addAttribute("username", username);

        List<OrderItem> basket = (List<OrderItem>) session.getAttribute("basket");

        // Check if the basket is empty
        if (basket == null || basket.isEmpty()) {
            // Redirect back to the basket page with an error message
            model.addAttribute("error", "Your basket is empty. Add items to your basket before placing an order.");
            return "basket";
        }

        // Proceed with order creation if basket is not empty
        Order order = new Order();
        double totalPrice = 0;
        for (OrderItem item : basket) {
            totalPrice += item.getPrice();
        }
        order.setPrice(totalPrice);

        orderService.saveOrder(order);

        for (OrderItem item : basket) {
            item.setOrder(order);
            orderItemService.saveOrderItem(item);
        }

        // Clear the basket from the session
        session.removeAttribute("basket");

        // Pass order details to the model for confirmation
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderItems", basket);

        return "order-confirmation"; // Render the confirmation page
    }





    @PostMapping("/user/update-basket")
    public String updateBasket(@RequestParam Long itemId, @RequestParam int quantity, HttpSession session, Model model) {

        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user (or you can fetch the full User object if needed)
        String username = authentication.getName();

        // Add the username to the model
        model.addAttribute("username", username);

        // Retrieve basket from session
        List<OrderItem> basket = (List<OrderItem>) session.getAttribute("basket");

        if (basket != null) {
            for (OrderItem item : basket) {
                if (item.getId().equals(itemId)) {
                    // Update quantity only
                    item.setQuantity(quantity);

                    // Set price as the price per unit
                    item.setPrice(item.getBeverage().getPrice());
                    break;
                }
            }

            // Save updated basket back to session
            session.setAttribute("basket", basket);
        }

        return "redirect:/user/basket"; // Redirect back to the basket page
    }














    @PostMapping("/user/remove-item")
    public String removeItem(@RequestParam Long itemId, HttpSession session, Model model) {

        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user (or you can fetch the full User object if needed)
        String username = authentication.getName();

        // Add the username to the model
        model.addAttribute("username", username);

        List<OrderItem> basket = (List<OrderItem>) session.getAttribute("basket");
        if (basket != null) {
            basket.removeIf(item -> item.getId().equals(itemId)); // Remove item by itemId
            session.setAttribute("basket", basket); // Save updated basket in session
        }
        return "redirect:/user/basket"; // Redirect back to basket page
    }








}
