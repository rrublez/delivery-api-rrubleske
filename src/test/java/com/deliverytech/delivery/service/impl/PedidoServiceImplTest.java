package com.deliverytech.delivery.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.deliverytech.delivery.dto.pedido.request.AtualizarStatusPedidoRequest;
import com.deliverytech.delivery.dto.pedido.request.CalcularPedidoRequest;
import com.deliverytech.delivery.dto.pedido.request.PedidoRequest;
import com.deliverytech.delivery.dto.pedido.response.CalcularPedidoResponse;
import com.deliverytech.delivery.dto.pedido.response.PedidoRelatorioResponse;
import com.deliverytech.delivery.dto.pedido.response.PedidoResponse;
import com.deliverytech.delivery.dto.shared.request.PedidoProdutoRequest;
import com.deliverytech.delivery.dto.shared.response.VendasPorRestauranteResponse;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.entity.Role;
import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.exception.ResourceNotFoundException;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.security.SecurityUtils;
import com.deliverytech.delivery.util.TestDataBuilder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoServiceImpl - Unit Tests")
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;
    
    @Mock
    private ClienteRepository clienteRepository;
    
    @Mock
    private RestauranteRepository restauranteRepository;
    
    @Mock
    private ProdutoRepository produtoRepository;

    private PedidoServiceImpl pedidoService;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoServiceImpl(
            pedidoRepository,
            clienteRepository,
            restauranteRepository,
            produtoRepository
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("criarPedido() Tests")
    class CriarPedidoTests {

        @Test
        @DisplayName("Should create pedido successfully with valid data")
        void shouldCreatePedidoSuccessfully() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            Produto produto = TestDataBuilder.buildProduto();
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            PedidoProdutoRequest itemRequest = TestDataBuilder.buildPedidoProdutoRequest(1L, 2);
            itensRequest.add(itemRequest);
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequest(1L, 1L, itensRequest);
            Pedido pedidoSalvo = TestDataBuilder.buildPedido();
            
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

            // When
            PedidoResponse response = pedidoService.criarPedido(request);

            // Then
            assertNotNull(response);
            assertEquals(pedidoSalvo.getId(), response.getId());
            assertEquals(request.getNumeroPedido(), response.getNumeroPedido());
            
            verify(clienteRepository).findById(1L);
            verify(restauranteRepository).findById(1L);
            verify(produtoRepository).findById(1L);
            verify(pedidoRepository).save(any(Pedido.class));
        }

        @Test
        @DisplayName("Should throw exception when cliente not found")
        void shouldThrowExceptionWhenClienteNotFound() {
            // Given
            PedidoRequest request = TestDataBuilder.buildPedidoRequest();
            when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.criarPedido(request)
            );
            
            assertEquals("Cliente inválido", exception.getMessage());
            verify(clienteRepository).findById(anyLong());
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when restaurante not found")
        void shouldThrowExceptionWhenRestauranteNotFound() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            PedidoRequest request = TestDataBuilder.buildPedidoRequest();
            
            when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(anyLong())).thenReturn(Optional.empty());

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.criarPedido(request)
            );
            
            assertEquals("Restaurante inválido", exception.getMessage());
            verify(restauranteRepository).findById(anyLong());
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when produto not found")
        void shouldThrowExceptionWhenProdutoNotFound() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            itensRequest.add(TestDataBuilder.buildPedidoProdutoRequest(999L, 2));
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequest(1L, 1L, itensRequest);
            
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pedidoService.criarPedido(request)
            );
            
            assertEquals("Produto não encontrado", exception.getMessage());
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should set default status to PENDENTE when not provided")
        void shouldSetDefaultStatusToPendente() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            Produto produto = TestDataBuilder.buildProduto();
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequestWithoutStatus(1L, 1L);
            Pedido pedidoSalvo = TestDataBuilder.buildPedido();
            pedidoSalvo.setStatus("PENDENTE");
            
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produto));
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

            // When
            PedidoResponse response = pedidoService.criarPedido(request);

            // Then
            assertEquals("PENDENTE", response.getStatus());
            verify(pedidoRepository).save(argThat(pedido -> 
                "PENDENTE".equals(pedido.getStatus())
            ));
        }

        @Test
        @DisplayName("Should set dataPedido to current time")
        void shouldSetDataPedidoToCurrentTime() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            Produto produto = TestDataBuilder.buildProduto();
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequest();
            Pedido pedidoSalvo = TestDataBuilder.buildPedido();
            
            when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(anyLong())).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produto));
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

            LocalDateTime before = LocalDateTime.now().minusSeconds(1);
            
            // When
            pedidoService.criarPedido(request);
            
            LocalDateTime after = LocalDateTime.now().plusSeconds(1);

            // Then
            verify(pedidoRepository).save(argThat(pedido -> 
                pedido.getDataPedido() != null &&
                pedido.getDataPedido().isAfter(before) &&
                pedido.getDataPedido().isBefore(after)
            ));
        }

        @Test
        @DisplayName("Should calculate subtotal correctly for single item")
        void shouldCalculateSubtotalForSingleItem() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            Produto produto = TestDataBuilder.buildProduto(1L, "Pizza", new BigDecimal("35.00"));
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            PedidoProdutoRequest itemRequest = new PedidoProdutoRequest();
            itemRequest.setProdutoId(1L);
            itemRequest.setQuantidade(2);
            itemRequest.setPrecoUnitario(new BigDecimal("35.00"));
            itensRequest.add(itemRequest);
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequest(1L, 1L, itensRequest);
            Pedido pedidoSalvo = TestDataBuilder.buildPedido();
            
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

            // When
            pedidoService.criarPedido(request);

            // Then
            verify(pedidoRepository).save(argThat(pedido -> 
                pedido.getValorTotal().compareTo(new BigDecimal("70.00")) == 0
            ));
        }

        @Test
        @DisplayName("Should calculate subtotal correctly for multiple items")
        void shouldCalculateSubtotalForMultipleItems() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            Produto produto1 = TestDataBuilder.buildProduto(1L, "Pizza", new BigDecimal("35.00"));
            Produto produto2 = TestDataBuilder.buildProduto(2L, "Coca-Cola", new BigDecimal("5.00"));
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            
            PedidoProdutoRequest item1 = new PedidoProdutoRequest();
            item1.setProdutoId(1L);
            item1.setQuantidade(2);
            item1.setPrecoUnitario(new BigDecimal("35.00"));
            itensRequest.add(item1);
            
            PedidoProdutoRequest item2 = new PedidoProdutoRequest();
            item2.setProdutoId(2L);
            item2.setQuantidade(3);
            item2.setPrecoUnitario(new BigDecimal("5.00"));
            itensRequest.add(item2);
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequest(1L, 1L, itensRequest);
            Pedido pedidoSalvo = TestDataBuilder.buildPedido();
            
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto1));
            when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto2));
            when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArguments()[0]);
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

            // When
            pedidoService.criarPedido(request);

            // Then
            // Expected: (35 * 2) + (5 * 3) = 70 + 15 = 85
            verify(pedidoRepository).save(argThat(pedido -> 
                pedido.getValorTotal().compareTo(new BigDecimal("85.00")) == 0
            ));
        }

        @Test
        @DisplayName("Should throw exception when produto is unavailable")
        void shouldThrowExceptionWhenProdutoUnavailable() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            Produto produto = TestDataBuilder.buildUnavailableProduto();
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            itensRequest.add(TestDataBuilder.buildPedidoProdutoRequest(1L, 2));
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequest(1L, 1L, itensRequest);
            
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.criarPedido(request)
            );
            
            assertTrue(exception.getMessage().contains("não está disponível"));
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when stock is insufficient")
        void shouldThrowExceptionWhenStockInsufficient() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            Produto produto = TestDataBuilder.buildProduto(1L, "Pizza", new BigDecimal("35.00"), true, 1);
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            itensRequest.add(TestDataBuilder.buildPedidoProdutoRequest(1L, 5)); // Requesting more than available
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequest(1L, 1L, itensRequest);
            
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.criarPedido(request)
            );
            
            assertTrue(exception.getMessage().contains("Estoque insuficiente"));
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when stock is zero")
        void shouldThrowExceptionWhenStockIsZero() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            Produto produto = TestDataBuilder.buildProdutoWithZeroStock();
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            itensRequest.add(TestDataBuilder.buildPedidoProdutoRequest(1L, 1));
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequest(1L, 1L, itensRequest);
            
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.criarPedido(request)
            );
            
            assertTrue(exception.getMessage().contains("Estoque insuficiente"));
        }

        @Test
        @DisplayName("Should reduce stock when creating pedido")
        void shouldReduceStockWhenCreatingPedido() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            Produto produto = TestDataBuilder.buildProduto(1L, "Pizza", new BigDecimal("35.00"), true, 10);
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            itensRequest.add(TestDataBuilder.buildPedidoProdutoRequest(1L, 3));
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequest(1L, 1L, itensRequest);
            Pedido pedidoSalvo = TestDataBuilder.buildPedido();
            
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArguments()[0]);
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

            // When
            pedidoService.criarPedido(request);

            // Then
            verify(produtoRepository).save(argThat(p -> 
                p.getEstoque() == 7 // 10 - 3 = 7
            ));
        }

        @Test
        @DisplayName("Should allow creating pedido with exact stock amount")
        void shouldAllowCreatingPedidoWithExactStock() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            Produto produto = TestDataBuilder.buildProduto(1L, "Pizza", new BigDecimal("35.00"), true, 5);
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            itensRequest.add(TestDataBuilder.buildPedidoProdutoRequest(1L, 5)); // Exact amount
            
            PedidoRequest request = TestDataBuilder.buildPedidoRequest(1L, 1L, itensRequest);
            Pedido pedidoSalvo = TestDataBuilder.buildPedido();
            
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArguments()[0]);
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

            // When
            PedidoResponse response = pedidoService.criarPedido(request);

            // Then
            assertNotNull(response);
            verify(produtoRepository).save(argThat(p -> 
                p.getEstoque() == 0 // 5 - 5 = 0
            ));
        }
    }

    @Nested
    @DisplayName("atualizarStatus() Tests")
    class AtualizarStatusTests {

        @Test
        @DisplayName("Should update pedido status successfully")
        void shouldUpdateStatusSuccessfully() {
            // Given
            Pedido pedido = TestDataBuilder.buildPedido();
            AtualizarStatusPedidoRequest request = new AtualizarStatusPedidoRequest();
            request.setStatus("ENTREGUE");
            
            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

            // When
            PedidoResponse response = pedidoService.atualizarStatus(1L, request);

            // Then
            assertNotNull(response);
            verify(pedidoRepository).save(argThat(p -> 
                "ENTREGUE".equals(p.getStatus())
            ));
        }

        @Test
        @DisplayName("Should throw exception when pedido not found for status update")
        void shouldThrowExceptionWhenPedidoNotFoundForUpdate() {
            // Given
            AtualizarStatusPedidoRequest request = new AtualizarStatusPedidoRequest();
            request.setStatus("ENTREGUE");
            
            when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pedidoService.atualizarStatus(999L, request)
            );
            
            assertEquals("Pedido não encontrado", exception.getMessage());
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should update status from PENDENTE to ENTREGUE")
        void shouldUpdateFromPendenteToEntregue() {
            // Given
            Pedido pedido = TestDataBuilder.buildPedido(1L, "PED-001", "PENDENTE");
            AtualizarStatusPedidoRequest request = new AtualizarStatusPedidoRequest();
            request.setStatus("ENTREGUE");
            
            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

            // When
            pedidoService.atualizarStatus(1L, request);

            // Then
            verify(pedidoRepository).save(argThat(p -> 
                "ENTREGUE".equals(p.getStatus())
            ));
        }
    }

    @Nested
    @DisplayName("cancelarPedido() Tests")
    class CancelarPedidoTests {

        @Test
        @DisplayName("Should cancel pedido successfully")
        void shouldCancelPedidoSuccessfully() {
            // Given
            Pedido pedido = TestDataBuilder.buildPedido();
            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

            // When
            pedidoService.cancelarPedido(1L);

            // Then
            verify(pedidoRepository).save(argThat(p -> 
                "CANCELADO".equals(p.getStatus())
            ));
        }

        @Test
        @DisplayName("Should throw exception when pedido not found for cancellation")
        void shouldThrowExceptionWhenPedidoNotFoundForCancellation() {
            // Given
            when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pedidoService.cancelarPedido(999L)
            );
            
            assertEquals("Pedido não encontrado", exception.getMessage());
            verify(pedidoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("obterPorId() Tests")
    class ObterPorIdTests {

        @Test
        @DisplayName("Should return pedido when found by ID")
        void shouldReturnPedidoWhenFound() {
            // Given
            Pedido pedido = TestDataBuilder.buildPedido();
            when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

            // When
            PedidoResponse response = pedidoService.obterPorId(1L);

            // Then
            assertNotNull(response);
            assertEquals(pedido.getId(), response.getId());
            verify(pedidoRepository).findById(1L);
        }

        @Test
        @DisplayName("Should throw exception when pedido not found by ID")
        void shouldThrowExceptionWhenNotFound() {
            // Given
            when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pedidoService.obterPorId(999L)
            );
            
            assertEquals("Pedido não encontrado", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("calcularTotal() Tests")
    class CalcularTotalTests {

        @Test
        @DisplayName("Should calculate total with delivery fee")
        void shouldCalculateTotalWithDeliveryFee() {
            // Given
            Restaurante restaurante = TestDataBuilder.buildRestaurante(1L, "Restaurante", new BigDecimal("5.00"));
            Produto produto = TestDataBuilder.buildProduto(1L, "Pizza", new BigDecimal("35.00"));
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            PedidoProdutoRequest item = new PedidoProdutoRequest();
            item.setProdutoId(1L);
            item.setQuantidade(2);
            item.setPrecoUnitario(new BigDecimal("35.00"));
            itensRequest.add(item);
            
            CalcularPedidoRequest request = new CalcularPedidoRequest();
            request.setRestauranteId(1L);
            request.setItens(itensRequest);
            
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            // When
            CalcularPedidoResponse response = pedidoService.calcularTotal(request);

            // Then
            assertNotNull(response);
            assertEquals(new BigDecimal("70.00"), response.getSubtotal()); // 35 * 2
            assertEquals(new BigDecimal("5.00"), response.getTaxaEntrega());
            assertEquals(new BigDecimal("75.00"), response.getValorTotal()); // 70 + 5
        }

        @Test
        @DisplayName("Should calculate total for multiple items with delivery fee")
        void shouldCalculateTotalForMultipleItems() {
            // Given
            Restaurante restaurante = TestDataBuilder.buildRestaurante(1L, "Restaurante", new BigDecimal("10.00"));
            Produto produto1 = TestDataBuilder.buildProduto(1L, "Pizza", new BigDecimal("35.00"));
            Produto produto2 = TestDataBuilder.buildProduto(2L, "Bebida", new BigDecimal("5.00"));
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            
            PedidoProdutoRequest item1 = new PedidoProdutoRequest();
            item1.setProdutoId(1L);
            item1.setQuantidade(1);
            item1.setPrecoUnitario(new BigDecimal("35.00"));
            itensRequest.add(item1);
            
            PedidoProdutoRequest item2 = new PedidoProdutoRequest();
            item2.setProdutoId(2L);
            item2.setQuantidade(2);
            item2.setPrecoUnitario(new BigDecimal("5.00"));
            itensRequest.add(item2);
            
            CalcularPedidoRequest request = new CalcularPedidoRequest();
            request.setRestauranteId(1L);
            request.setItens(itensRequest);
            
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto1));
            when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto2));

            // When
            CalcularPedidoResponse response = pedidoService.calcularTotal(request);

            // Then
            assertEquals(new BigDecimal("45.00"), response.getSubtotal()); // (35 * 1) + (5 * 2)
            assertEquals(new BigDecimal("10.00"), response.getTaxaEntrega());
            assertEquals(new BigDecimal("55.00"), response.getValorTotal()); // 45 + 10
        }

        @Test
        @DisplayName("Should throw exception when restaurante not found for calculation")
        void shouldThrowExceptionWhenRestauranteNotFoundForCalculation() {
            // Given
            CalcularPedidoRequest request = new CalcularPedidoRequest();
            request.setRestauranteId(999L);
            request.setItens(new ArrayList<>());
            
            when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pedidoService.calcularTotal(request)
            );
            
            assertEquals("Restaurante não encontrado", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when produto not found in calculation")
        void shouldThrowExceptionWhenProdutoNotFoundInCalculation() {
            // Given
            Restaurante restaurante = TestDataBuilder.buildRestaurante();
            
            List<PedidoProdutoRequest> itensRequest = new ArrayList<>();
            itensRequest.add(TestDataBuilder.buildPedidoProdutoRequest(999L, 1));
            
            CalcularPedidoRequest request = new CalcularPedidoRequest();
            request.setRestauranteId(1L);
            request.setItens(itensRequest);
            
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(produtoRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pedidoService.calcularTotal(request)
            );
            
            assertEquals("Produto não encontrado", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("canAccess() Tests")
    class CanAccessTests {

        @Test
        @DisplayName("Should return false when pedidoId is null")
        void shouldReturnFalseWhenPedidoIdIsNull() {
            // When
            boolean result = pedidoService.canAccess(null);

            // Then
            assertFalse(result);
            verify(pedidoRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Should return true for ADMIN role")
        void shouldReturnTrueForAdmin() {
            try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
                // Given
                mockedSecurityUtils.when(() -> SecurityUtils.hasRole("ADMIN")).thenReturn(true);

                // When
                boolean result = pedidoService.canAccess(1L);

                // Then
                assertTrue(result);
                verify(pedidoRepository, never()).findById(any());
            }
        }

        @Test
        @DisplayName("Should return true for CLIENTE role with matching email")
        void shouldReturnTrueForClienteWithMatchingEmail() {
            try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
                // Given
                Cliente cliente = TestDataBuilder.buildCliente(1L, "João", "joao@email.com");
                Pedido pedido = TestDataBuilder.buildPedido();
                pedido.setCliente(cliente);
                
                Usuario usuario = new Usuario();
                usuario.setEmail("joao@email.com");
                usuario.setRole(Role.CLIENTE);
                
                mockedSecurityUtils.when(() -> SecurityUtils.hasRole("ADMIN")).thenReturn(false);
                mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(Optional.of(usuario));
                
                when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

                // When
                boolean result = pedidoService.canAccess(1L);

                // Then
                assertTrue(result);
            }
        }

        @Test
        @DisplayName("Should return false for CLIENTE role with different email")
        void shouldReturnFalseForClienteWithDifferentEmail() {
            try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
                // Given
                Cliente cliente = TestDataBuilder.buildCliente(1L, "João", "joao@email.com");
                Pedido pedido = TestDataBuilder.buildPedido();
                pedido.setCliente(cliente);
                
                Usuario usuario = new Usuario();
                usuario.setEmail("outro@email.com");
                usuario.setRole(Role.CLIENTE);
                
                mockedSecurityUtils.when(() -> SecurityUtils.hasRole("ADMIN")).thenReturn(false);
                mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(Optional.of(usuario));
                
                when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

                // When
                boolean result = pedidoService.canAccess(1L);

                // Then
                assertFalse(result);
            }
        }

        @Test
        @DisplayName("Should return true for RESTAURANTE role with matching ID")
        void shouldReturnTrueForRestauranteWithMatchingId() {
            try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
                // Given
                Restaurante restaurante = TestDataBuilder.buildRestaurante(1L, "Restaurante", new BigDecimal("5.00"));
                Pedido pedido = TestDataBuilder.buildPedido();
                pedido.setRestaurante(restaurante);
                
                Usuario usuario = new Usuario();
                usuario.setEmail("restaurante@email.com");
                usuario.setRole(Role.RESTAURANTE);
                usuario.setRestauranteId(1L);
                
                mockedSecurityUtils.when(() -> SecurityUtils.hasRole("ADMIN")).thenReturn(false);
                mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(Optional.of(usuario));
                
                when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

                // When
                boolean result = pedidoService.canAccess(1L);

                // Then
                assertTrue(result);
            }
        }

        @Test
        @DisplayName("Should return false for RESTAURANTE role with different ID")
        void shouldReturnFalseForRestauranteWithDifferentId() {
            try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
                // Given
                Restaurante restaurante = TestDataBuilder.buildRestaurante(1L, "Restaurante", new BigDecimal("5.00"));
                Pedido pedido = TestDataBuilder.buildPedido();
                pedido.setRestaurante(restaurante);
                
                Usuario usuario = new Usuario();
                usuario.setEmail("restaurante@email.com");
                usuario.setRole(Role.RESTAURANTE);
                usuario.setRestauranteId(2L);
                
                mockedSecurityUtils.when(() -> SecurityUtils.hasRole("ADMIN")).thenReturn(false);
                mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(Optional.of(usuario));
                
                when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

                // When
                boolean result = pedidoService.canAccess(1L);

                // Then
                assertFalse(result);
            }
        }

        @Test
        @DisplayName("Should return false when pedido not found")
        void shouldReturnFalseWhenPedidoNotFound() {
            try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
                // Given
                mockedSecurityUtils.when(() -> SecurityUtils.hasRole("ADMIN")).thenReturn(false);
                when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

                // When
                boolean result = pedidoService.canAccess(999L);

                // Then
                assertFalse(result);
            }
        }
    }

    @Nested
    @DisplayName("Query Methods Tests")
    class QueryMethodsTests {

        @Test
        @DisplayName("Should find pedidos by cliente ID")
        void shouldFindPedidosByClienteId() {
            // Given
            List<Pedido> pedidos = Arrays.asList(
                TestDataBuilder.buildPedido(1L, "PED-001", "PENDENTE"),
                TestDataBuilder.buildPedido(2L, "PED-002", "ENTREGUE")
            );
            
            when(pedidoRepository.findByClienteId(1L)).thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.findByClienteId(1L);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(pedidoRepository).findByClienteId(1L);
        }

        @Test
        @DisplayName("Should find pedidos by status")
        void shouldFindPedidosByStatus() {
            // Given
            List<Pedido> pedidos = Collections.singletonList(
                TestDataBuilder.buildPedido(1L, "PED-001", "PENDENTE")
            );
            
            when(pedidoRepository.findByStatus("PENDENTE")).thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.findByStatus("PENDENTE");

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("PENDENTE", result.get(0).getStatus());
        }

        @Test
        @DisplayName("Should return empty list when no pedidos found by status")
        void shouldReturnEmptyListWhenNoPedidosFoundByStatus() {
            // Given
            when(pedidoRepository.findByStatus("INEXISTENTE")).thenReturn(Collections.emptyList());

            // When
            List<PedidoResponse> result = pedidoService.findByStatus("INEXISTENTE");

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should find top 10 pedidos by valor total descending")
        void shouldFindTop10ByValorTotal() {
            // Given
            List<Pedido> pedidos = TestDataBuilder.buildClienteList(10)
                .stream()
                .map(c -> TestDataBuilder.buildPedido())
                .toList();
            
            when(pedidoRepository.findTop10ByOrderByValorTotalDesc()).thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.findTop10ByOrderByValorTotalDesc();

            // Then
            assertNotNull(result);
            assertEquals(10, result.size());
            verify(pedidoRepository).findTop10ByOrderByValorTotalDesc();
        }

        @Test
        @DisplayName("Should find pedidos between dates")
        void shouldFindPedidosBetweenDates() {
            // Given
            LocalDateTime inicio = LocalDateTime.now().minusDays(7);
            LocalDateTime fim = LocalDateTime.now();
            
            List<Pedido> pedidos = Arrays.asList(
                TestDataBuilder.buildPedido(1L, "PED-001", "PENDENTE"),
                TestDataBuilder.buildPedido(2L, "PED-002", "ENTREGUE")
            );
            
            when(pedidoRepository.findByDataPedidoBetween(inicio, fim)).thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.findByDataPedidoBetween(inicio, fim);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(pedidoRepository).findByDataPedidoBetween(inicio, fim);
        }

        @Test
        @DisplayName("Should find top 5 maiores pedidos por restaurante")
        void shouldFindTop5MaioresPedidosPorRestaurante() {
            // Given
            List<Pedido> pedidos = Arrays.asList(
                TestDataBuilder.buildPedido(1L, "PED-001", "ENTREGUE"),
                TestDataBuilder.buildPedido(2L, "PED-002", "ENTREGUE")
            );
            
            when(pedidoRepository.findTop5MaioresPedidosPorRestaurante(1L)).thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.findTop5MaioresPedidosPorRestaurante(1L);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(pedidoRepository).findTop5MaioresPedidosPorRestaurante(1L);
        }

        @Test
        @DisplayName("Should find pedidos com valor acima de threshold")
        void shouldFindPedidosComValorAcimaDe() {
            // Given
            BigDecimal valor = new BigDecimal("100.00");
            List<Pedido> pedidos = Collections.singletonList(
                TestDataBuilder.buildPedido()
            );
            
            when(pedidoRepository.findPedidosComValorAcimaDe(valor)).thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.findPedidosComValorAcimaDe(valor);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(pedidoRepository).findPedidosComValorAcimaDe(valor);
        }

        @Test
        @DisplayName("Should find pedidos por restaurante")
        void shouldFindPedidosPorRestaurante() {
            // Given
            List<Pedido> pedidos = Arrays.asList(
                TestDataBuilder.buildPedido(1L, "PED-001", "PENDENTE"),
                TestDataBuilder.buildPedido(2L, "PED-002", "ENTREGUE")
            );
            
            when(pedidoRepository.findByRestauranteId(1L)).thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.pedidosPorRestaurante(1L);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(pedidoRepository).findByRestauranteId(1L);
        }
    }

    @Nested
    @DisplayName("listarComFiltros() Tests")
    class ListarComFiltrosTests {

        @Test
        @DisplayName("Should filter by status and date range when all provided")
        void shouldFilterByStatusAndDateRange() {
            // Given
            String status = "PENDENTE";
            LocalDateTime inicio = LocalDateTime.now().minusDays(7);
            LocalDateTime fim = LocalDateTime.now();
            
            List<Pedido> pedidos = Collections.singletonList(
                TestDataBuilder.buildPedido(1L, "PED-001", status)
            );
            
            when(pedidoRepository.findByStatusAndDataPedidoBetween(status, inicio, fim))
                .thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.listarComFiltros(status, inicio, fim);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(pedidoRepository).findByStatusAndDataPedidoBetween(status, inicio, fim);
        }

        @Test
        @DisplayName("Should filter by status only when dates not provided")
        void shouldFilterByStatusOnly() {
            // Given
            String status = "PENDENTE";
            List<Pedido> pedidos = Collections.singletonList(
                TestDataBuilder.buildPedido(1L, "PED-001", status)
            );
            
            when(pedidoRepository.findByStatus(status)).thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.listarComFiltros(status, null, null);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(pedidoRepository).findByStatus(status);
        }

        @Test
        @DisplayName("Should filter by date range only when status not provided")
        void shouldFilterByDateRangeOnly() {
            // Given
            LocalDateTime inicio = LocalDateTime.now().minusDays(7);
            LocalDateTime fim = LocalDateTime.now();
            
            List<Pedido> pedidos = Arrays.asList(
                TestDataBuilder.buildPedido(1L, "PED-001", "PENDENTE"),
                TestDataBuilder.buildPedido(2L, "PED-002", "ENTREGUE")
            );
            
            when(pedidoRepository.findByDataPedidoBetween(inicio, fim)).thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.listarComFiltros(null, inicio, fim);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(pedidoRepository).findByDataPedidoBetween(inicio, fim);
        }

        @Test
        @DisplayName("Should return empty list when no filters provided")
        void shouldReturnEmptyListWhenNoFiltersProvided() {
            // When
            List<PedidoResponse> result = pedidoService.listarComFiltros(null, null, null);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(pedidoRepository, never()).findByStatus(any());
            verify(pedidoRepository, never()).findByDataPedidoBetween(any(), any());
        }
    }

    @Nested
    @DisplayName("Report Methods Tests")
    class ReportMethodsTests {

        @Test
        @DisplayName("Should get vendas por restaurante")
        void shouldGetVendasPorRestaurante() {
            // Given
            List<VendasPorRestauranteResponse> vendas = new ArrayList<>();
            when(pedidoRepository.obterVendasPorRestaurante()).thenReturn(vendas);

            // When
            List<VendasPorRestauranteResponse> result = pedidoService.obterVendasPorRestaurante();

            // Then
            assertNotNull(result);
            assertSame(vendas, result);
            verify(pedidoRepository).obterVendasPorRestaurante();
        }

        @Test
        @DisplayName("Should get relatorio by periodo and status")
        void shouldGetRelatorioByPeriodoAndStatus() {
            // Given
            LocalDateTime inicio = LocalDateTime.now().minusDays(30);
            LocalDateTime fim = LocalDateTime.now();
            String status = "ENTREGUE";
            
            List<PedidoRelatorioResponse> relatorio = new ArrayList<>();
            when(pedidoRepository.obterRelatorioByPeriodoAndStatus(inicio, fim, status))
                .thenReturn(relatorio);

            // When
            List<PedidoRelatorioResponse> result = pedidoService.obterRelatorioByPeriodoAndStatus(
                inicio, fim, status
            );

            // Then
            assertNotNull(result);
            assertSame(relatorio, result);
            verify(pedidoRepository).obterRelatorioByPeriodoAndStatus(inicio, fim, status);
        }

        @Test
        @DisplayName("Should delegate pedidosPorCliente to findByClienteId")
        void shouldDelegatePedidosPorCliente() {
            // Given
            List<Pedido> pedidos = Collections.singletonList(TestDataBuilder.buildPedido());
            when(pedidoRepository.findByClienteId(1L)).thenReturn(pedidos);

            // When
            List<PedidoResponse> result = pedidoService.pedidosPorCliente(1L);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(pedidoRepository).findByClienteId(1L);
        }
    }
}
