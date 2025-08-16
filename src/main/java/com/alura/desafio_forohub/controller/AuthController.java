package com.alura.desafio_forohub.controller;

import com.alura.desafio_forohub.config.JwtUtils;
import com.alura.desafio_forohub.dto.AuthRequest;
import com.alura.desafio_forohub.dto.AuthResponse;
import com.alura.desafio_forohub.service.impl.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final com.alura.desafio_forohub.service.impl.UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager,
                          com.alura.desafio_forohub.service.impl.UserDetailsServiceImpl userDetailsService,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            // Autenticación usando AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            // Obtener detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

            // Generar token JWT
            String token = jwtUtils.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(new AuthResponse("Usuario no encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthResponse("Credenciales inválidas"));
        }
    }
}