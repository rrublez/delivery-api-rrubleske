package com.deliverytech.delivery.dto.pedido.request;

import com.deliverytech.delivery.dto.shared.request.PedidoProdutoRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PedidoRequest", description = "Request para criação de um novo pedido")
public class PedidoRequest {

  @NotBlank(message = "Número do pedido é obrigatório")
  @Size(min = 5, max = 20, message = "Número do pedido deve ter entre 5 e 20 caracteres")
  @Pattern(regexp = "^[A-Z0-9-]+$", message = "Número do pedido deve conter apenas letras maiúsculas, números e hífen")
  @Schema(description = "Número único do pedido", example = "PED-2025-001", minLength = 5, maxLength = 20)
  private String numeroPedido;

  @NotBlank(message = "Status é obrigatório")
  @Pattern(regexp = "PENDENTE|ENTREGUE|CANCELADO", message = "Status deve ser PENDENTE, ENTREGUE ou CANCELADO")
  @Schema(description = "Status do pedido", example = "PENDENTE", allowableValues = {"PENDENTE", "ENTREGUE", "CANCELADO"})
  private String status;

  @NotNull(message = "ID do cliente é obrigatório")
  @Schema(description = "ID do cliente que realizou o pedido", example = "1")
  private Long clienteId;

  @NotNull(message = "ID do restaurante é obrigatório")
  @Schema(description = "ID do restaurante que preparará o pedido", example = "1")
  private Long restauranteId;

  @NotEmpty(message = "Pedido deve conter pelo menos um item")
  @Valid
  @Schema(description = "Lista de produtos do pedido (mínimo 1 item)")
  private List<PedidoProdutoRequest> itens = new ArrayList<>();

}
