package com.codeWithRaman.implementation.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.codeWithRaman.implementation.model.Crate;
import com.codeWithRaman.implementation.model.Bottle;
import com.codeWithRaman.implementation.service.BeverageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import com.codeWithRaman.implementation.service.BeverageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Controller
public class DashboardController {

    private final BeverageService beverageService;

    public DashboardController(BeverageService beverageService) {
        this.beverageService = beverageService;
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        // Add beverages
        model.addAttribute("beverages", beverageService.getAllBeverages());

        // Retrieve the login success message
        Object loginSuccess = session.getAttribute("loginSuccess");
        if (loginSuccess != null) {
            model.addAttribute("loginSuccess", loginSuccess);
            session.removeAttribute("loginSuccess"); // Show it only once
        }

        return "admin";
    }

    @PostMapping("/admin/addBottle")
    public String addBottle(
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam double volume,
            @RequestParam boolean alcoholic,
            @RequestParam int inStock,
            @RequestParam String supplier,
            @RequestParam String beveragePic,
            Model model) {

        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user (or you can fetch the full User object if needed)
        String username = authentication.getName();

        // Add the username to the model
        model.addAttribute("username", username);

        // Create and populate a new Bottle
        Bottle bottle = new Bottle();
        bottle.setName(name);
        bottle.setPrice(price);
        bottle.setVolume(volume);
        bottle.setAlcoholic(alcoholic);
        bottle.setInStock(inStock);
        bottle.setSupplier(supplier);
        bottle.setBeveragePic(beveragePic);

        // Save the Bottle to the database
        beverageService.saveBeverage(bottle);
        return "redirect:/admin"; // Redirect back to Admin page
    }

    @PostMapping("/admin/addCrate")
    public String addCrate(
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam int noOfBottles,
            @RequestParam int cratesInStock,
            @RequestParam String beveragePic,
            Model model) {

        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username of the logged-in user (or you can fetch the full User object if needed)
        String username = authentication.getName();

        // Add the username to the model
        model.addAttribute("username", username);

        // Create and populate a new Crate
        Crate crate = new Crate();
        crate.setName(name);
        crate.setPrice(price);
        crate.setNoOfBottles(noOfBottles);
        crate.setCratesInStock(cratesInStock);
        crate.setBeveragePic(beveragePic);

        // Save the Crate to the database
        beverageService.saveBeverage(crate);
        return "redirect:/admin"; // Redirect back to Admin page
    }

    @GetMapping("/user")
    public String userDashboard(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        model.addAttribute("username", username);
        model.addAttribute("beverages", beverageService.getAllBeverages());

        // ✅ Fix: Retrieve `loginSuccess` before removing it
        Object loginSuccess = session.getAttribute("loginSuccess");

        if (loginSuccess != null) {
            model.addAttribute("loginSuccess", loginSuccess);
            session.removeAttribute("loginSuccess");  // Ensure it's removed AFTER Thymeleaf gets it
        }

        return "user";
    }

    // Catch-all for any unmatched GET or POST requests
    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
    public String handleInvalidUrl() {
        // Fetch the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user has ROLE_ADMIN
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            // If the user is an admin, redirect to /admin
            return "redirect:/admin";
        }

        // If the user is not an admin, redirect to /user
        return "redirect:/user";
    }




}
