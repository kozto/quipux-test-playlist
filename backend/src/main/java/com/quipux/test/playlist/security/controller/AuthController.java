package com.quipux.test.playlist.security.controller;

import com.quipux.test.playlist.security.model.User;
import com.quipux.test.playlist.security.repository.UserRepository;
import com.quipux.test.playlist.security.dto.AuthRequest;
import com.quipux.test.playlist.security.dto.AuthResponse;
import com.quipux.test.playlist.security.dto.RegisterRequest;
import com.quipux.test.playlist.security.jwt.JwtService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Oscar Chamorro
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // Logger

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {
        
        try {
            
            logger.info("Intentando autenticar usuario: {}", authRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            
            // Si la autenticacion es exitosa:
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);
            logger.info("Usuario {} autenticado exitosamente.", authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(jwt));

        } catch (AuthenticationException e) {
            
            logger.warn("Fallo de autenticacion para usuario {}: {}", authRequest.getUsername(), e.getMessage());
            
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", System.currentTimeMillis());
            body.put("status", HttpStatus.UNAUTHORIZED.value());
            body.put("error", "No Autorizado");
            body.put("message", "Credenciales invalidas o usuario no encontrado."); 
            body.put("path", "/auth/login");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error: El nombre de usuario ya esta en uso!");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Set<String> roles = new HashSet<>();
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            roles.add("ROLE_USER");
        } else {
            roles.addAll(registerRequest.getRoles());
        }
        
        User user = new User(
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                roles
        );
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente!");
    }
    
}
