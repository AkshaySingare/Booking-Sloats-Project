package com.example.BookingSystem.controller;

import com.example.BookingSystem.dto.CreateUserRequest;
import com.example.BookingSystem.dto.LoginRequest;
import com.example.BookingSystem.model.UserEntity;
import com.example.BookingSystem.repository.UserRepository;
import com.example.BookingSystem.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateUserRequest req) {

        if (userRepository.findByUsername(req.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole("USER");

        userRepository.save(user);

        return ResponseEntity.ok("User created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.username(),
                        req.password()
                )
        );

        String token = jwtService.generateToken(req.username());

        return ResponseEntity.ok(Map.of("token", token));
    }
}
