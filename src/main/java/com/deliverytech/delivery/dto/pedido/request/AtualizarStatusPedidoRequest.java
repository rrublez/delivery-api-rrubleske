package com.deliverytech.delivery.dto.pedido.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusPedidoRequest {

  @NotBlank(message = "Status é obrigatório")
  @Pattern(regexp = "PENDENTE|CONFIRMADO|PREPARANDO|SAIU_ENTREGA|ENTREGUE|CANCELADO", 
           message = "Status deve ser PENDENTE, CONFIRMADO, PREPARANDO, SAIU_ENTREGA, ENTREGUE ou CANCELADO")
  private String status;

}
