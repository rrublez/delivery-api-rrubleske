package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.auth.request.LoginRequest;
import com.deliverytech.delivery.dto.auth.request.RegisterRequest;
import com.deliverytech.delivery.dto.auth.response.LoginResponse;
import com.deliverytech.delivery.dto.auth.response.UserResponse;
import com.deliverytech.delivery.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Endpoints básicos de registro e login para o sistema.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Cadastro e autenticação de usuários")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuário", description = "Cria um novo usuário com role e senha BCrypt")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar", description = "Valida e retorna os dados do token e do usuário autenticado")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Dados do usuário logado", description = "Retorna os dados públicos do usuário autenticado")
    public ResponseEntity<UserResponse> me() {
        UserResponse response = authService.me();
        return ResponseEntity.ok(response);
    }
}