package com.deliverytech.delivery.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoginResponse", description = "Token JWT e metadados retornados após autenticação")
public class LoginResponse {

    private String token;

    private String tokenType;

    private Instant expiresAt;

    private UserResponse user;
}
