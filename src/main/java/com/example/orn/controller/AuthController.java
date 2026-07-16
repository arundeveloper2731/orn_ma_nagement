package com.example.orn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orn.dto.AuthResponse;
import com.example.orn.entity.LoginRequest;
import com.example.orn.model.User;
import com.example.orn.security.JwtUtil;
import com.example.orn.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            User user = userService.findByUserName(request.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not found");
            }

            if (user.getPassword() == null
                    || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid password");
            }

            String role = (user.getRole() == null || user.getRole().isBlank()) ? "USER" : user.getRole();
            String token = jwtUtil.generateToken(user.getUsername(), role);

            return ResponseEntity.ok(new AuthResponse("Login Successful", token, user.getUsername(), role));

        } catch (Exception e) {
            log.error("Login failed for username={}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed due to a server error. Please try again.");
        }
    }
}
