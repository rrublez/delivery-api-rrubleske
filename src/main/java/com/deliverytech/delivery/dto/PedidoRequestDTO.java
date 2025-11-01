package com.deliverytech.delivery.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

    @NotNull(message = "ID do cliente não pode ser vazio")
    private String clienteId;

    @NotNull(message = "ID do estabelecimento não pode ser vazio")
    private String estabelecimentoId;

    @NotNull(message = "ID do endereço de entrega não pode ser vazio")
    private String enderecoId;

    @NotEmpty(message = "Pedido deve ter pelo menos 1 item")
    @Valid
    private List<ItemPedidoRequestDTO> itens;

}

