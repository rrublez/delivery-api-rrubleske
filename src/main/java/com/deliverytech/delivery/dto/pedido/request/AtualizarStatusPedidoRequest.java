package com.deliverytech.delivery.dto.pedido.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AtualizarStatusPedidoRequest", description = "Request para alterar o status de um pedido")
public class AtualizarStatusPedidoRequest {

  @NotBlank(message = "Status é obrigatório")
  @Pattern(regexp = "PENDENTE|CONFIRMADO|PREPARANDO|SAIU_ENTREGA|ENTREGUE|CANCELADO", 
           message = "Status deve ser PENDENTE, CONFIRMADO, PREPARANDO, SAIU_ENTREGA, ENTREGUE ou CANCELADO")
  @Schema(description = "Novo status do pedido", 
          example = "ENTREGUE", 
          allowableValues = {"PENDENTE", "CONFIRMADO", "PREPARANDO", "SAIU_ENTREGA", "ENTREGUE", "CANCELADO"})
  private String status;

}
