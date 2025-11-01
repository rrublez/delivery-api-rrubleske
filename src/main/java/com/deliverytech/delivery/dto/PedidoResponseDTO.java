package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    private String id;

    private ClienteResponseDTO cliente;

    private EstabelecimentoResponseDTO estabelecimento;

    private EnderecoResponseDTO endereco;

    private String status;

    private BigDecimal valorTotal;

    private List<ItemPedidoResponseDTO> itens;

    private LocalDateTime dataPedido;

}
