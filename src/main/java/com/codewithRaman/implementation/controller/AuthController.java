package com.codeWithRaman.implementation.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codeWithRaman.implementation.model.Role;
import com.codeWithRaman.implementation.model.User;
import com.codeWithRaman.implementation.service.UserService;

@Controller
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		User user = new User();
		List<Role> roles = userService.getAllRoles(); // Fetch all roles dynamically
		model.addAttribute("user", user);
		model.addAttribute("roles", roles); // Pass roles to the view
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") User user, String roleName, Model model, RedirectAttributes redirectAttributes) {
		// Check for duplicate username
		if (userService.findByUsername(user.getUsername()) != null) {
			model.addAttribute("error", "Username already taken. Please try again.");
			List<Role> roles = userService.getAllRoles(); // Fetch all roles again if there's an error
			model.addAttribute("roles", roles); // Add roles back to the model
			return "register"; // Return to registration page with error
		}

		// Register the user
		userService.registerUser(user, roleName);

		redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
		return "redirect:/login"; // Redirect on successful registration
	}

	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}
}
