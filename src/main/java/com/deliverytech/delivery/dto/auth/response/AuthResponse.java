package com.deliverytech.delivery.dto.auth.response;

import com.deliverytech.delivery.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AuthResponse", description = "Retorno da API de autenticação")
public class AuthResponse {

    private Long id;

    private String nome;

    private String email;

    private Role role;

    private Long restauranteId;

    private Boolean ativo;

    private String token;
}