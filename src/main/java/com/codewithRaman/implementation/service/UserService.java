package com.codeWithRaman.implementation.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codeWithRaman.implementation.model.Role;
import com.codeWithRaman.implementation.model.User;
import com.codeWithRaman.implementation.repository.RoleRepository;
import com.codeWithRaman.implementation.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user, String roleName) {
        // Check if the username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return null;  // Return null instead of throwing an exception
        }

        // Set the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Ensure the role is loaded from the database and is attached
        Role selectedRole = roleRepository.findByName(roleName);

        // If role is not found, throw an exception
        if (selectedRole == null) {
            throw new IllegalArgumentException("Role not found: " + roleName);
        }

        // Set roles
        Set<Role> roles = new HashSet<>();
        roles.add(selectedRole);
        user.setRoles(roles);

        // Save the user
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}
