package com.deliverytech.delivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import com.deliverytech.delivery.dto.auth.request.LoginRequest;
import com.deliverytech.delivery.dto.auth.request.RegisterRequest;
import com.deliverytech.delivery.dto.auth.response.AuthResponse;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.repository.UsuarioRepository;

@SuppressWarnings({"nullness", "null"})
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @SuppressWarnings("nullness")
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

        AuthResponse response = authService.register(request);

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

        AuthResponse response = authService.login(request);

        assertEquals(2L, response.getId());
        assertEquals("User", response.getNome());
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
    @SuppressWarnings("nullness")
    private void stubSave(Usuario usuario) {
        doReturn(usuario).when(usuarioRepository).save(notNull(Usuario.class));
    }
}