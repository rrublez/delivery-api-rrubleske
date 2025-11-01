package com.deliverytech.delivery.service.impl;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.entity.*;
import com.deliverytech.delivery.repository.*;
import com.deliverytech.delivery.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementação do PedidoService.
 * Fornece operações de consulta de histórico de consumo de clientes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final EnderecoRepository enderecoRepository;

    @Override
    public ClienteHistoricoResponseDTO getHistoricoByDocumento(String documento) {
        log.info("Buscando histórico de consumo para cliente com documento: {}", documento);

        List<Pedido> pedidos = pedidoRepository.findByClienteDocumento(documento);

        if (pedidos.isEmpty()) {
            log.warn("Nenhum pedido encontrado para o documento: {}", documento);
            throw new IllegalArgumentException("Nenhum pedido encontrado para o cliente com documento: " + documento);
        }

        Pedido primeiroPedido = pedidos.get(0);
        return this.construirHistoricoResponse(primeiroPedido.getCliente().getNome(),
                documento,
                primeiroPedido.getCliente().getEmail(),
                primeiroPedido.getCliente().getTelefone(),
                primeiroPedido.getCliente().getId(),
                pedidos);
    }

    @Override
    public ClienteHistoricoResponseDTO getHistoricoByNumeroPedido(String numeroPedido) {
        log.info("Buscando histórico de consumo pelo número de pedido: {}", numeroPedido);

        Pedido pedido = pedidoRepository.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> {
                    log.warn("Pedido não encontrado: {}", numeroPedido);
                    return new IllegalArgumentException("Pedido não encontrado: " + numeroPedido);
                });

        String clienteId = pedido.getCliente().getId();
        List<Pedido> todosPedidosCliente = pedidoRepository.findByClienteIdOrderByDataPedidoDesc(clienteId);

        return this.construirHistoricoResponse(
                pedido.getCliente().getNome(),
                pedido.getCliente().getDocumentoIdentificacao(),
                pedido.getCliente().getEmail(),
                pedido.getCliente().getTelefone(),
                clienteId,
                todosPedidosCliente);
    }

    /**
     * Constrói a resposta completa do histórico de consumo.
     * Inclui os 3 produtos mais pedidos nos últimos 10 pedidos.
     *
     * @param nomeCliente nome do cliente
     * @param documento CPF do cliente
     * @param email email do cliente
     * @param telefone telefone do cliente
     * @param clienteId ID do cliente
     * @param todosPedidos lista de todos os pedidos do cliente
     * @return ClienteHistoricoResponseDTO completo
     */
    private ClienteHistoricoResponseDTO construirHistoricoResponse(
            String nomeCliente,
            String documento,
            String email,
            String telefone,
            String clienteId,
            List<Pedido> todosPedidos) {

        // Construir histórico de pedidos (do mais recente ao mais antigo)
        List<PedidoHistoricoDTO> historicoDTO = todosPedidos.stream()
                .map(this::mapearPedidoParaHistoricoDTO)
                .collect(Collectors.toList());

        // Buscar os 3 produtos mais pedidos nos últimos 10 pedidos
        List<PedidoHistoricoDTO> ultimos10 = historicoDTO.stream()
                .limit(10)
                .collect(Collectors.toList());

        List<ProdutoMaisPedidoDTO> top3Produtos = this.extrairTop3Produtos(ultimos10);

        return ClienteHistoricoResponseDTO.builder()
                .clienteId(clienteId)
                .nomeCliente(nomeCliente)
                .documentoIdentificacao(documento)
                .email(email)
                .telefone(telefone)
                .topProdutos(top3Produtos)
                .historicoPedidos(historicoDTO)
                .build();
    }

    /**
     * Mapeia uma entidade Pedido para PedidoHistoricoDTO.
     *
     * @param pedido entidade Pedido
     * @return PedidoHistoricoDTO mapeado
     */
    private PedidoHistoricoDTO mapearPedidoParaHistoricoDTO(Pedido pedido) {
        List<ItemHistoricoDTO> itensDTO = pedido.getItens().stream()
                .map(this::mapearItemPedidoParaHistoricoDTO)
                .collect(Collectors.toList());

        return PedidoHistoricoDTO.builder()
                .id(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .dataPedido(pedido.getDataPedido())
                .status(pedido.getStatus())
                .valorTotal(pedido.getValorTotal())
                .itens(itensDTO)
                .build();
    }

    /**
     * Mapeia uma entidade ItemPedido para ItemHistoricoDTO.
     *
     * @param itemPedido entidade ItemPedido
     * @return ItemHistoricoDTO mapeado
     */
    private ItemHistoricoDTO mapearItemPedidoParaHistoricoDTO(ItemPedido itemPedido) {
        return ItemHistoricoDTO.builder()
                .id(itemPedido.getId())
                .nomeProduto(itemPedido.getProdutoEstabelecimento().getProduto().getNome())
                .descricaoProduto(itemPedido.getProdutoEstabelecimento().getProduto().getDescricao())
                .quantidade(itemPedido.getQuantidade())
                .valorUnitario(itemPedido.getValorUnitario())
                .valorTotal(itemPedido.getValorTotal())
                .emPromocao(itemPedido.getEmPromocao())
                .build();
    }

    /**
     * Extrai os 3 produtos mais pedidos a partir do histórico dos últimos 10 pedidos.
     *
     * @param ultimos10Pedidos lista com os últimos 10 pedidos
     * @return lista com os 3 produtos mais pedidos
     */
    private List<ProdutoMaisPedidoDTO> extrairTop3Produtos(List<PedidoHistoricoDTO> ultimos10Pedidos) {
        // Mapear todos os itens dos últimos 10 pedidos
        Map<String, ProdutoStats> produtosMap = new HashMap<>();

        for (PedidoHistoricoDTO pedido : ultimos10Pedidos) {
            for (ItemHistoricoDTO item : pedido.getItens()) {
                String produtoId = item.getId();
                produtosMap.computeIfAbsent(produtoId, k -> new ProdutoStats(
                        item.getId(),
                        item.getNomeProduto()))
                        .adicionarItem(item.getQuantidade());
            }
        }

        // Ordenar por frequência (vezes pedido) e retornar os 3 primeiros
        return produtosMap.values().stream()
                .sorted((a, b) -> b.getTotalVezesPedido() != null && a.getTotalVezesPedido() != null ?
                        b.getTotalVezesPedido().compareTo(a.getTotalVezesPedido()) : 0)
                .limit(3)
                .map(stats -> ProdutoMaisPedidoDTO.builder()
                        .produtoId(stats.getProdutoId())
                        .nomeProduto(stats.getNomeProduto())
                        .totalVezesPedido(stats.getTotalVezesPedido())
                        .totalUnidadesPedidas(stats.getTotalUnidades())
                        .build())
                .collect(Collectors.toList());
    }

        /**
     * Gera um número único de pedido no formato YYYYMM-xxxxx.
     * Onde YYYYMM é ano e mês atual, e xxxxx é um código aleatório.
     *
     * @return String com número do pedido no formato YYYYMM-xxxxx
     */
    private String gerarNumeroPedido() {
        String yyyymm = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMM"));
        String codigo = String.format("%05x", new Random().nextInt(0x100000)).substring(0, 5);
        return yyyymm + "-" + codigo;
    }

    /**
     * Verifica se um produto está em promoção válida no momento atual.
     * Um produto está em promoção quando:
     * - Possui preço promocional configurado (não null)
     * - Data/hora atual está dentro do período de promoção
     *
     * @param produtoEstabelecimento produto para verificar
     * @return true se está em promoção, false caso contrário
     */
    public Boolean verificarSeEmPromocao(ProdutoEstabelecimento produtoEstabelecimento) {
        if (produtoEstabelecimento == null || produtoEstabelecimento.getPrecoPromocional() == null) {
            return false;
        }

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicio = produtoEstabelecimento.getDataHoraInicioPromocao();
        LocalDateTime fim = produtoEstabelecimento.getDataHoraFimPromocao();

        // Verifica se as datas estão configuradas e se agora está dentro do período
        if (inicio != null && fim != null) {
            return agora.isAfter(inicio) && agora.isBefore(fim);
        }

        return false;
    }

    /**
     * Classe interna auxiliar para rastrear estatísticas de produtos.
     */
    private static class ProdutoStats {
        private String produtoId;
        private String nomeProduto;
        private Integer totalVezesPedido;
        private Integer totalUnidades;

        public ProdutoStats(String produtoId, String nomeProduto) {
            this.produtoId = produtoId;
            this.nomeProduto = nomeProduto;
            this.totalVezesPedido = 0;
            this.totalUnidades = 0;
        }

        public void adicionarItem(Integer quantidade) {
            this.totalVezesPedido++;
            this.totalUnidades += quantidade;
        }

        public String getProdutoId() {
            return produtoId;
        }

        public String getNomeProduto() {
            return nomeProduto;
        }

        public Integer getTotalVezesPedido() {
            return totalVezesPedido;
        }

        public Integer getTotalUnidades() {
            return totalUnidades;
        }
    }

    @Override
    @Transactional
    public PedidoResponseDTO create(PedidoRequestDTO pedidoRequestDTO) {
        log.info("Criando novo pedido para cliente: {}, estabelecimento: {}", 
                pedidoRequestDTO.getClienteId(), pedidoRequestDTO.getEstabelecimentoId());

        // 1. Validar se cliente existe
        Cliente cliente = clienteRepository.findById(pedidoRequestDTO.getClienteId())
                .orElseThrow(() -> {
                    log.warn("Cliente não encontrado: {}", pedidoRequestDTO.getClienteId());
                    return new IllegalArgumentException("Cliente não encontrado com ID: " + pedidoRequestDTO.getClienteId());
                });

        // 2. Validar se estabelecimento existe
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(pedidoRequestDTO.getEstabelecimentoId())
                .orElseThrow(() -> {
                    log.warn("Estabelecimento não encontrado: {}", pedidoRequestDTO.getEstabelecimentoId());
                    return new IllegalArgumentException("Estabelecimento não encontrado com ID: " + pedidoRequestDTO.getEstabelecimentoId());
                });

        // 3. Validar se endereço existe
        Endereco endereco = enderecoRepository.findById(pedidoRequestDTO.getEnderecoId())
                .orElseThrow(() -> {
                    log.warn("Endereço não encontrado: {}", pedidoRequestDTO.getEnderecoId());
                    return new IllegalArgumentException("Endereço não encontrado com ID: " + pedidoRequestDTO.getEnderecoId());
                });

        // 4. Processar itens do pedido
        List<ItemPedido> itensProcessados = new ArrayList<>();
        BigDecimal valorTotalPedido = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemRequest : pedidoRequestDTO.getItens()) {
            // Validar se produtoEstabelecimento existe
            ProdutoEstabelecimento produtoEstabelecimento = estabelecimento.getProdutos().stream()
                    .filter(p -> p.getId().equals(itemRequest.getProdutoEstabelecimentoId()))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.warn("Produto não encontrado no estabelecimento: {}", itemRequest.getProdutoEstabelecimentoId());
                        return new IllegalArgumentException("Produto não encontrado no estabelecimento");
                    });

            // Verificar se está em promoção
            Boolean emPromocao = this.verificarSeEmPromocao(produtoEstabelecimento);
            
            // Selecionar preço (promocional ou normal)
            BigDecimal valorUnitario = emPromocao && produtoEstabelecimento.getPrecoPromocional() != null
                    ? produtoEstabelecimento.getPrecoPromocional()
                    : produtoEstabelecimento.getPrecoUnitario();

            // Calcular valorTotal do item
            BigDecimal valorTotalItem = valorUnitario.multiply(new BigDecimal(itemRequest.getQuantidade()));

            // Criar ItemPedido
            ItemPedido itemPedido = ItemPedido.builder()
                    .produtoEstabelecimento(produtoEstabelecimento)
                    .quantidade(itemRequest.getQuantidade())
                    .valorUnitario(valorUnitario)
                    .valorTotal(valorTotalItem)
                    .emPromocao(emPromocao)
                    .build();

            itensProcessados.add(itemPedido);
            valorTotalPedido = valorTotalPedido.add(valorTotalItem);

            log.debug("Item processado - Produto: {}, Em Promoção: {}, Valor Unitário: {}", 
                    produtoEstabelecimento.getProduto().getNome(), emPromocao, valorUnitario);
        }

        // 6. Gerar numeroPedido no formato YYYYMM-xxxxx
        String numeroPedido = this.gerarNumeroPedido();

        // 5. Criar e persistir Pedido
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .estabelecimento(estabelecimento)
                .endereco(endereco)
                .numeroPedido(numeroPedido)
                .status("PENDENTE")
                .valorTotal(valorTotalPedido)
                .dataPedido(LocalDateTime.now())
                .build();

        // Associar itens ao pedido
        for (ItemPedido item : itensProcessados) {
            item.setPedido(pedido);
        }
        pedido.setItens(itensProcessados);

        // Persistir pedido e itens
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        log.info("Pedido criado com sucesso - Número: {}, Valor Total: {}", numeroPedido, valorTotalPedido);

        // 8. Retornar PedidoResponseDTO
        return this.mapearPedidoParaResponseDTO(pedidoSalvo);
    }

    @Override
    public PedidoResponseDTO getByNumeroPedido(String numeroPedido) {
        log.info("Buscando pedido pelo número: {}", numeroPedido);

        Pedido pedido = pedidoRepository.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> {
                    log.warn("Pedido não encontrado: {}", numeroPedido);
                    return new IllegalArgumentException("Pedido não encontrado: " + numeroPedido);
                });

        return this.mapearPedidoParaResponseDTO(pedido);
    }

    /**
     * Mapeia uma entidade Pedido para PedidoResponseDTO.
     *
     * @param pedido entidade Pedido
     * @return PedidoResponseDTO mapeado
     */
    private PedidoResponseDTO mapearPedidoParaResponseDTO(Pedido pedido) {
        List<ItemPedidoResponseDTO> itensDTO = pedido.getItens().stream()
                .map(this::mapearItemPedidoParaResponseDTO)
                .collect(Collectors.toList());

        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .status(pedido.getStatus())
                .valorTotal(pedido.getValorTotal())
                .dataPedido(pedido.getDataPedido())
                .itens(itensDTO)
                .build();
    }

    /**
     * Mapeia uma entidade ItemPedido para ItemPedidoResponseDTO.
     *
     * @param itemPedido entidade ItemPedido
     * @return ItemPedidoResponseDTO mapeado
     */
    private ItemPedidoResponseDTO mapearItemPedidoParaResponseDTO(ItemPedido itemPedido) {
        return ItemPedidoResponseDTO.builder()
                .id(itemPedido.getId())
                .quantidade(itemPedido.getQuantidade())
                .valorUnitario(itemPedido.getValorUnitario())
                .valorTotal(itemPedido.getValorTotal())
                .emPromocao(itemPedido.getEmPromocao())
                .build();
    }
}

