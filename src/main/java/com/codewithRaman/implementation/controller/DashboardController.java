package com.codeWithRaman.implementation.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user (or you can fetch the full User object if needed)
        String username = authentication.getName();

        // Add the username to the model
        model.addAttribute("username", username);

        // Return the view for admin dashboard
        return "admin";
    }

    @GetMapping("/user")
    public String userDashboard(Model model) {
        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user
        String username = authentication.getName();

        // Add the username to the model
        model.addAttribute("username", username);

        // Return the view for user dashboard
        return "user";
    }
}
