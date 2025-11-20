package com.deliverytech.delivery.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        String seed = "01234567890123456789012345678901";
        String secret = Base64.getEncoder().encodeToString(seed.getBytes(StandardCharsets.UTF_8));
        jwtUtil = new JwtUtil(secret, Duration.ofHours(1).toMillis());
    }

    @Test
    void generateTokenShouldContainCustomClaims() {
        Usuario usuario = Usuario.builder()
                .id(100L)
                .email("jwt@test.com")
                .senha("ignored")
                .role(Role.CLIENTE)
                .restauranteId(5L)
                .build();

        String token = jwtUtil.generateToken(usuario);

        assertNotNull(token);
        assertEquals("jwt@test.com", jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.validateToken(token, usuario));
        Number userId = jwtUtil.extractClaim(token, claims -> claims.get("userId", Number.class));
        Number restauranteId = jwtUtil.extractClaim(token, claims -> claims.get("restauranteId", Number.class));
        assertEquals(100L, userId.longValue());
        assertEquals("CLIENTE", jwtUtil.extractClaim(token, claims -> claims.get("role", String.class)));
        assertEquals(5L, restauranteId.longValue());
    }
}
