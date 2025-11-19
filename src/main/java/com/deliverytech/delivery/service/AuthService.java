package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.auth.request.LoginRequest;
import com.deliverytech.delivery.dto.auth.request.RegisterRequest;
import com.deliverytech.delivery.dto.auth.response.LoginResponse;
import com.deliverytech.delivery.dto.auth.response.UserResponse;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.repository.UsuarioRepository;
import com.deliverytech.delivery.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse register(@Valid RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        Role role = Optional.ofNullable(request.getRole()).orElse(Role.CLIENTE);

        if (role == Role.RESTAURANTE && request.getRestauranteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "restauranteId é obrigatório para role RESTAURANTE");
        }

        if (role != Role.RESTAURANTE && request.getRestauranteId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "restauranteId só pode ser definido para role RESTAURANTE");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(role)
                .restauranteId(request.getRestauranteId())
                .ativo(Optional.ofNullable(request.getAtivo()).orElse(true))
                .build();

        Usuario saved = usuarioRepository.save(usuario);
        return toUserResponse(saved);
    }

    public LoginResponse login(@Valid LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Conta inativa");
        }

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        String token = jwtUtil.generateToken(usuario);
        return toLoginResponse(usuario, token);
    }

    public UserResponse me() {
        Usuario usuario = getAuthenticatedUsuario();
        return toUserResponse(usuario);
    }

    private LoginResponse toLoginResponse(Usuario usuario, String token) {
        Instant expiresAt = Instant.now().plusMillis(jwtUtil.getExpirationMillis());
        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresAt(expiresAt)
                .user(toUserResponse(usuario))
                .build();
    }

    private UserResponse toUserResponse(Usuario usuario) {
        return UserResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .restauranteId(usuario.getRestauranteId())
                .ativo(usuario.getAtivo())
                .build();
    }

    private Usuario getAuthenticatedUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Usuario usuario) {
            return usuario;
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
    }
}