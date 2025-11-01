package com.deliverytech.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoEstabelecimentoResponseDTO {

    private String id;

    private ProdutoResponseDTO produto;

    private EstabelecimentoResponseDTO estabelecimento;

    private BigDecimal precoUnitario;

    private BigDecimal precoPromocional;

    private LocalDateTime dataHoraInicioPromocao;

    private LocalDateTime dataHoraFimPromocao;

    private Boolean emPromocao;

}
