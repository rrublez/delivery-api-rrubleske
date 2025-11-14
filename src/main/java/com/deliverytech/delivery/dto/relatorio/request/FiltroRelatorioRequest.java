package com.deliverytech.delivery.dto.relatorio.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroRelatorioRequest {

  @NotNull(message = "Data inicial é obrigatória")
  private LocalDateTime dataInicial;

  @NotNull(message = "Data final é obrigatória")
  private LocalDateTime dataFinal;

  private String status;

}
