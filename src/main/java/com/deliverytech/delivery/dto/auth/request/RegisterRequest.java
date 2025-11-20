package com.deliverytech.delivery.dto.auth.request;

import com.deliverytech.delivery.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RegisterRequest", description = "Dados para registrar um novo usuário")
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 80)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String senha;

    @Schema(description = "Perfil do usuário (CLIENTE, RESTAURANTE, ADMIN, ENTREGADOR)")
    private Role role;

    @Schema(description = "Restaurante vinculado ao usuário (obrigatório para RESTAURANTE)")
    private Long restauranteId;

    @Schema(description = "Define se o usuário está ativo. Padrão true")
    private Boolean ativo;
}