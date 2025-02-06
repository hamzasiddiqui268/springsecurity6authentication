package com.codeWithRaman.implementation;

import com.codeWithRaman.implementation.model.User;
import com.codeWithRaman.implementation.model.Role;
import com.codeWithRaman.implementation.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.codeWithRaman.implementation.repository.RoleRepository;
import com.codeWithRaman.implementation.repository.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository; // Inject the userRepository here

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() throws Exception {
        // Ensure roles exist in the database
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new Role("ROLE_ADMIN");
            roleRepository.save(roleAdmin);
        }

        Role roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser == null) {
            roleUser = new Role("ROLE_USER");
            roleRepository.save(roleUser);
        }

        // Ensure the 'admin' user exists before the tests with encoded password
        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User("admin", passwordEncoder.encode("admin123"), "admin@domain.com", true);
            admin.addRole(roleAdmin);
            admin.addRole(roleUser);
            userRepository.save(admin);
        }

        // Ensure the 'user' user exists before the tests with encoded password
        if (!userRepository.findByUsername("user").isPresent()) {
            User user = new User("user", passwordEncoder.encode("user123"), "user@domain.com", true);
            user.addRole(roleUser); // Only the user role for this user
            userRepository.save(user);
        }

        // Ensure the 'existinguser' user exists for the duplicate username test
        if (!userRepository.findByUsername("existinguser").isPresent()) {
            User existingUser = new User("existinguser", passwordEncoder.encode("password123"), "existinguser@domain.com", true);
            existingUser.addRole(roleUser); // Add role to the user
            userRepository.save(existingUser);
        }
    }




    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = {"ADMIN", "USER"})
    public void testLoginAsAdmin() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "admin")
                        .param("password", "admin123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin?success=true"));
    }

    @Test
    @WithMockUser(username = "user", password = "user123", roles = {"USER"})
    public void testLoginAsUser() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "user123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user?success=true"));
    }


    @Test
    public void testLoginFailure() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "wronguser")
                        .param("password", "wrongpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void testRegistrationFailureDuplicateUsername() throws Exception {
        // Register the existing user first
        userService.registerUser(new User("existinguser", "password123", "existinguser@domain.com", true), "ROLE_USER");

        // Now test duplicate username registration
        mockMvc.perform(post("/register")
                        .param("username", "existinguser")  // Try to register with the existing username
                        .param("password", "password123"))
                .andExpect(status().isOk())  // Expect the response to be 200, since you're rendering the same page
                .andExpect(model().attributeExists("error"))  // Ensure error message exists
                .andExpect(model().attribute("error", "Username already taken")) // Validate error message
                .andExpect(model().attributeExists("roles")); // Ensure roles are passed back to the view
    }










    @Test
    public void testRegistrationSuccess() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "newuser")
                        .param("password", "newpassword")
                        .param("roleName", "ROLE_USER"))
                .andExpect(status().is3xxRedirection())  // Expect a redirection on success
                .andExpect(redirectedUrl("/login"))     // Verify the redirection URL
                .andExpect(flash().attributeExists("success"));
    }

}
