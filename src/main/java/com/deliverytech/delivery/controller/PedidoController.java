package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ClienteHistoricoResponseDTO;
import com.deliverytech.delivery.dto.PedidoRequestDTO;
import com.deliverytech.delivery.dto.PedidoResponseDTO;
import com.deliverytech.delivery.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para gerenciar operações relacionadas a Pedidos.
 * Fornece endpoints para criação, consulta e análise de histórico de pedidos.
 *
 * Base path: /api/v1/pedidos
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    /**
     * Cria um novo pedido.
     *
     * @param pedidoRequestDTO dados do pedido a criar
     * @return ResponseEntity com PedidoResponseDTO criado
     */
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> create(@Valid @RequestBody PedidoRequestDTO pedidoRequestDTO) {
        log.info("Recebido request para criar novo pedido - Cliente: {}, Estabelecimento: {}", 
                pedidoRequestDTO.getClienteId(), pedidoRequestDTO.getEstabelecimentoId());
        try {
            PedidoResponseDTO pedido = pedidoService.create(pedidoRequestDTO);
            log.info("Pedido criado com sucesso - ID: {}", pedido.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (Exception e) {
            log.error("Erro ao criar pedido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    /**
     * Busca um pedido pelo número do pedido.
     *
     * @param numeroPedido número do pedido no formato YYYYMM-xxxxx (ex: 2510-a522c)
     * @return ResponseEntity com PedidoResponseDTO
     */
    @GetMapping("/{numeroPedido}")
    public ResponseEntity<PedidoResponseDTO> getByNumeroPedido(@PathVariable String numeroPedido) {
        log.info("Recebido request para buscar pedido - Número Pedido: {}", numeroPedido);
        try {
            PedidoResponseDTO pedido = pedidoService.getByNumeroPedido(numeroPedido);
            log.info("Pedido encontrado: {}", numeroPedido);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            log.warn("Pedido não encontrado - Número: {}, Mensagem: {}", numeroPedido, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Busca o histórico de consumo de um cliente pelo CPF.
     * Retorna todos os pedidos do cliente, do mais recente ao mais antigo,
     * com os 3 produtos mais pedidos nos últimos 10 pedidos.
     *
     * @param cpf CPF/Documento de identificação do cliente (11-14 dígitos)
     * @return ResponseEntity com ClienteHistoricoResponseDTO
     * @throws IllegalArgumentException se nenhum pedido for encontrado para o CPF
     */
    @GetMapping("/historico/cpf/{cpf}")
    public ResponseEntity<ClienteHistoricoResponseDTO> getHistoricoByDocumento(@PathVariable String cpf) {
        log.info("Recebido request para histórico de consumo - CPF: {}", cpf);
        try {
            ClienteHistoricoResponseDTO historico = pedidoService.getHistoricoByDocumento(cpf);
            log.info("Histórico encontrado para CPF: {}", cpf);
            return ResponseEntity.ok(historico);
        } catch (IllegalArgumentException e) {
            log.warn("Erro ao buscar histórico - CPF: {}, Mensagem: {}", cpf, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Busca o histórico de consumo de um cliente pelo número do pedido.
     * Retorna o cliente associado ao pedido com seu histórico completo,
     * do mais recente ao mais antigo, com os 3 produtos mais pedidos nos últimos 10 pedidos.
     *
     * @param numeroPedido número do pedido no formato YYYYMM-xxxxx (ex: 2510-a522c)
     * @return ResponseEntity com ClienteHistoricoResponseDTO
     * @throws IllegalArgumentException se o pedido não for encontrado
     */
    @GetMapping("/historico/pedido/{numeroPedido}")
    public ResponseEntity<ClienteHistoricoResponseDTO> getHistoricoByNumeroPedido(@PathVariable String numeroPedido) {
        log.info("Recebido request para histórico de consumo - Número Pedido: {}", numeroPedido);
        try {
            ClienteHistoricoResponseDTO historico = pedidoService.getHistoricoByNumeroPedido(numeroPedido);
            log.info("Histórico encontrado para pedido: {}", numeroPedido);
            return ResponseEntity.ok(historico);
        } catch (IllegalArgumentException e) {
            log.warn("Erro ao buscar histórico - Pedido: {}, Mensagem: {}", numeroPedido, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
