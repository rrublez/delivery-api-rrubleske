package com.deliverytech.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.deliverytech.delivery.dto.auth.request.LoginRequest;
import com.deliverytech.delivery.dto.auth.request.RegisterRequest;
import com.deliverytech.delivery.dto.auth.response.LoginResponse;
import com.deliverytech.delivery.dto.auth.response.UserResponse;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.repository.UsuarioRepository;
import com.deliverytech.delivery.security.JwtUtil;

@SuppressWarnings({"nullness", "null"})
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_success() {
        RegisterRequest request = RegisterRequest.builder()
                .nome("Fulano")
                .email("fulano@test.com")
                .senha("Password123!")
                .role(Role.CLIENTE)
                .build();

        doReturn(Optional.empty()).when(usuarioRepository).findByEmail(request.getEmail());
        doReturn("hashed").when(passwordEncoder).encode(request.getSenha());
        Usuario savedUsuario = Usuario.builder()
                .id(1L)
                .nome(request.getNome())
                .email(request.getEmail())
                .role(Role.CLIENTE)
                .senha("hashed")
                .ativo(true)
                .build();
        stubSave(savedUsuario);

        UserResponse response = authService.register(request);

        assertEquals(1L, response.getId());
        assertEquals("Fulano", response.getNome());

        verify(passwordEncoder).encode(request.getSenha());
    }

    @Test
    void register_duplicateEmail() {
        RegisterRequest request = RegisterRequest.builder().email("ja@test.com").senha("pass").nome("Ja").build();
        doReturn(Optional.of(new Usuario())).when(usuarioRepository).findByEmail(request.getEmail());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.register(request));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void login_success() {
        LoginRequest request = LoginRequest.builder().email("user@test.com").senha("pass").build();
        Usuario usuario = Usuario.builder().id(2L).email(request.getEmail()).senha("hashed").nome("User").role(Role.CLIENTE).ativo(true).build();
        doReturn(Optional.of(usuario)).when(usuarioRepository).findByEmail(request.getEmail());
        doReturn(true).when(passwordEncoder).matches(request.getSenha(), usuario.getSenha());
        doReturn("jwt").when(jwtUtil).generateToken(usuario);
        doReturn(3600000L).when(jwtUtil).getExpirationMillis();

        LoginResponse response = authService.login(request);

        assertEquals(2L, response.getUser().getId());
        assertEquals("User", response.getUser().getNome());
        assertEquals("jwt", response.getToken());
    }

    @Test
    void login_inactiveAccount() {
        LoginRequest request = LoginRequest.builder().email("user@test.com").senha("pass").build();
        Usuario usuario = Usuario.builder().email(request.getEmail()).senha("hashed").ativo(false).build();
        doReturn(Optional.of(usuario)).when(usuarioRepository).findByEmail(request.getEmail());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.login(request));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @Test
    void login_invalidCredentials() {
        LoginRequest request = LoginRequest.builder().email("user@test.com").senha("pass").build();
        Usuario usuario = Usuario.builder().email(request.getEmail()).senha("hash").ativo(true).build();
        doReturn(Optional.of(usuario)).when(usuarioRepository).findByEmail(request.getEmail());
        doReturn(false).when(passwordEncoder).matches(request.getSenha(), usuario.getSenha());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.login(request));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @Test
    void me_returnsAuthenticatedUser() {
        Usuario usuario = Usuario.builder().id(3L).email("me@test.com").nome("Me").role(Role.CLIENTE).build();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserResponse response = authService.me();

        assertEquals("me@test.com", response.getEmail());
        assertEquals(3L, response.getId());
    }

    @SuppressWarnings("nullness")
    private void stubSave(Usuario usuario) {
        doReturn(usuario).when(usuarioRepository).save(notNull(Usuario.class));
    }

    @AfterEach
    void cleanSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
