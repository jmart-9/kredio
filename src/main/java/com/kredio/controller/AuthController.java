package com.kredio.controller;

import com.kredio.model.Role;
import com.kredio.model.Tenant;
import com.kredio.model.User;
import com.kredio.security.JwtUtil;
import com.kredio.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Tenant defaultTenant = new Tenant();
        defaultTenant.setId(1L);  // MVP: tenant por defecto

        User user = userService.registerUser(
                request.name(),
                request.email(),
                request.password(),
                request.role(),
                defaultTenant
        );

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getRole().name()));
    }
}

// DTOs como records (acceso directo sin "get")
record RegisterRequest(String name, String email, String password, Role role) {}
record LoginRequest(String email, String password) {}
record AuthResponse(String token, String role) {}