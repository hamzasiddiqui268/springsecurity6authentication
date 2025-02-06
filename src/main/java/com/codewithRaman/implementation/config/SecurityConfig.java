package com.codeWithRaman.implementation.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;


import com.codeWithRaman.implementation.model.User;
import com.codeWithRaman.implementation.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

	 private final UserService userService;

	    @Autowired
	    public SecurityConfig(@Lazy UserService userService) {
	        this.userService = userService;
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    public UserDetailsService userDetailsService() {
	        return username -> {
	            User user = userService.findByUsername(username);
	            if (user != null) {
	                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
	                        user.getRoles().stream()
	                                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName()))
	                                .toList());
	            }
	            throw new RuntimeException("User not found");
	        };
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	        AuthenticationManagerBuilder authenticationManagerBuilder =
	                http.getSharedObject(AuthenticationManagerBuilder.class);
	        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	        return authenticationManagerBuilder.build();
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	        .csrf(csrf -> csrf.disable())
	                .authorizeHttpRequests(authorize -> authorize
	                        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
	                        .requestMatchers("/admin/**").hasRole("ADMIN")
	                        .requestMatchers("/user/**").hasRole("USER")
	                        .anyRequest().authenticated()
	                )
	                .formLogin(form -> form
	                        .loginPage("/login")
	                        .successHandler(customAuthenticationSuccessHandler())
	                        .permitAll()
	                )
	                .logout(logout -> logout
	                        .logoutUrl("/logout")
	                        .logoutSuccessUrl("/login")
	                        .permitAll()
	                );
	        return http.build();
	    }

	@Bean
	public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
												Authentication authentication) throws IOException {
				boolean isAdmin = authentication.getAuthorities().stream()
						.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

				// Redirecting to the appropriate dashboard
				if (isAdmin) {
					response.sendRedirect("/admin?success=true");
				} else {
					response.sendRedirect("/user?success=true");
				}
			}
		};
	}



}
