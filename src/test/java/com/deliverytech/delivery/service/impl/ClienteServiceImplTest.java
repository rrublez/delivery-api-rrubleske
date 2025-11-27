package com.deliverytech.delivery.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.deliverytech.delivery.dto.cliente.request.ClienteRequest;
import com.deliverytech.delivery.dto.cliente.response.ClienteResponse;
import com.deliverytech.delivery.dto.shared.response.RankingClienteResponse;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.util.TestDataBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteServiceImpl - Unit Tests")
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    private ClienteServiceImpl clienteService;

    @BeforeEach
    void setUp() {
        clienteService = new ClienteServiceImpl(clienteRepository);
    }

    @Nested
    @DisplayName("criarCliente() Tests")
    class CriarClienteTests {

        @Test
        @DisplayName("Should create cliente with all fields provided")
        void shouldCreateClienteWithAllFields() {
            // Given
            ClienteRequest request = TestDataBuilder.buildClienteRequest();
            Cliente clienteSalvo = TestDataBuilder.buildCliente();
            
            when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

            // When
            ClienteResponse response = clienteService.criarCliente(request);

            // Then
            assertNotNull(response);
            assertEquals(clienteSalvo.getId(), response.getId());
            assertEquals(request.getNome(), response.getNome());
            assertEquals(request.getEmail(), response.getEmail());
            assertEquals(request.getTelefone(), response.getTelefone());
            assertEquals(request.getCpf(), response.getCpf());
            assertEquals(request.getAtivo(), response.getAtivo());
            
            verify(clienteRepository, times(1)).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Should set ativo to true when not provided")
        void shouldSetAtivoToTrueWhenNull() {
            // Given
            ClienteRequest request = TestDataBuilder.buildClienteRequestWithoutAtivo();
            Cliente clienteSalvo = TestDataBuilder.buildCliente();
            clienteSalvo.setAtivo(true);
            
            when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

            // When
            ClienteResponse response = clienteService.criarCliente(request);

            // Then
            assertNotNull(response);
            assertTrue(response.getAtivo(), "Ativo should default to true");
            
            verify(clienteRepository).save(argThat(cliente -> 
                cliente.getAtivo() != null && cliente.getAtivo()
            ));
        }

        @Test
        @DisplayName("Should create inactive cliente when ativo is false")
        void shouldCreateInactiveCliente() {
            // Given
            ClienteRequest request = TestDataBuilder.buildClienteRequest();
            request.setAtivo(false);
            
            Cliente clienteSalvo = TestDataBuilder.buildInactiveCliente();
            
            when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

            // When
            ClienteResponse response = clienteService.criarCliente(request);

            // Then
            assertNotNull(response);
            assertFalse(response.getAtivo(), "Cliente should be inactive");
            
            verify(clienteRepository).save(argThat(cliente -> 
                cliente.getAtivo() != null && !cliente.getAtivo()
            ));
        }

        @Test
        @DisplayName("Should map all fields correctly from request to entity")
        void shouldMapAllFieldsCorrectly() {
            // Given
            ClienteRequest request = TestDataBuilder.buildClienteRequest("Maria Santos", "maria@email.com");
            Cliente clienteSalvo = TestDataBuilder.buildCliente(2L, "Maria Santos", "maria@email.com");
            
            when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

            // When
            ClienteResponse response = clienteService.criarCliente(request);

            // Then
            verify(clienteRepository).save(argThat(cliente -> 
                cliente.getNome().equals("Maria Santos") &&
                cliente.getEmail().equals("maria@email.com") &&
                cliente.getTelefone().equals("11987654321") &&
                cliente.getCpf().equals("12345678901")
            ));
        }
    }

    @Nested
    @DisplayName("findByEmail() Tests")
    class FindByEmailTests {

        @Test
        @DisplayName("Should return list of clientes with matching email")
        void shouldReturnClientesWithMatchingEmail() {
            // Given
            String email = "joao.silva@email.com";
            List<Cliente> clientes = Arrays.asList(
                TestDataBuilder.buildCliente(1L, "João Silva", email)
            );
            
            when(clienteRepository.findByEmail(email)).thenReturn(clientes);

            // When
            List<ClienteResponse> result = clienteService.findByEmail(email);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(email, result.get(0).getEmail());
            
            verify(clienteRepository, times(1)).findByEmail(email);
        }

        @Test
        @DisplayName("Should return empty list when no cliente found")
        void shouldReturnEmptyListWhenNoClienteFound() {
            // Given
            String email = "naoexiste@email.com";
            when(clienteRepository.findByEmail(email)).thenReturn(Collections.emptyList());

            // When
            List<ClienteResponse> result = clienteService.findByEmail(email);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            
            verify(clienteRepository, times(1)).findByEmail(email);
        }

        @Test
        @DisplayName("Should handle multiple clientes with same email")
        void shouldHandleMultipleClientesWithSameEmail() {
            // Given
            String email = "shared@email.com";
            List<Cliente> clientes = Arrays.asList(
                TestDataBuilder.buildCliente(1L, "Cliente 1", email),
                TestDataBuilder.buildCliente(2L, "Cliente 2", email)
            );
            
            when(clienteRepository.findByEmail(email)).thenReturn(clientes);

            // When
            List<ClienteResponse> result = clienteService.findByEmail(email);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(c -> c.getEmail().equals(email)));
        }
    }

    @Nested
    @DisplayName("findByAtivoTrue() Tests")
    class FindByAtivoTrueTests {

        @Test
        @DisplayName("Should return active cliente when exists")
        void shouldReturnActiveClienteWhenExists() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente();
            when(clienteRepository.findByAtivoTrue()).thenReturn(cliente);

            // When
            ClienteResponse result = clienteService.findByAtivoTrue();

            // Then
            assertNotNull(result);
            assertTrue(result.getAtivo());
            assertEquals(cliente.getId(), result.getId());
            
            verify(clienteRepository, times(1)).findByAtivoTrue();
        }

        @Test
        @DisplayName("Should return null when no active cliente exists")
        void shouldReturnNullWhenNoActiveCliente() {
            // Given
            when(clienteRepository.findByAtivoTrue()).thenReturn(null);

            // When
            ClienteResponse result = clienteService.findByAtivoTrue();

            // Then
            assertNull(result, "Should return null when no active cliente found");
            
            verify(clienteRepository, times(1)).findByAtivoTrue();
        }

        @Test
        @DisplayName("Should map cliente to response correctly")
        void shouldMapClienteToResponseCorrectly() {
            // Given
            Cliente cliente = TestDataBuilder.buildCliente(5L, "Active User", "active@email.com");
            when(clienteRepository.findByAtivoTrue()).thenReturn(cliente);

            // When
            ClienteResponse result = clienteService.findByAtivoTrue();

            // Then
            assertNotNull(result);
            assertEquals(5L, result.getId());
            assertEquals("Active User", result.getNome());
            assertEquals("active@email.com", result.getEmail());
        }
    }

    @Nested
    @DisplayName("findByNomeContainingIgnoreCase() Tests")
    class FindByNomeContainingIgnoreCaseTests {

        @Test
        @DisplayName("Should find clientes with nome containing search term (case insensitive)")
        void shouldFindClientesWithNomeContainingTerm() {
            // Given
            String searchTerm = "silva";
            List<Cliente> clientes = Arrays.asList(
                TestDataBuilder.buildCliente(1L, "João Silva", "joao@email.com"),
                TestDataBuilder.buildCliente(2L, "Maria Silva", "maria@email.com")
            );
            
            when(clienteRepository.findByNomeContainingIgnoreCase(searchTerm)).thenReturn(clientes);

            // When
            List<ClienteResponse> result = clienteService.findByNomeContainingIgnoreCase(searchTerm);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            
            verify(clienteRepository, times(1)).findByNomeContainingIgnoreCase(searchTerm);
        }

        @Test
        @DisplayName("Should return empty list when no match found")
        void shouldReturnEmptyListWhenNoMatch() {
            // Given
            String searchTerm = "xyz";
            when(clienteRepository.findByNomeContainingIgnoreCase(searchTerm))
                .thenReturn(Collections.emptyList());

            // When
            List<ClienteResponse> result = clienteService.findByNomeContainingIgnoreCase(searchTerm);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle partial name matches")
        void shouldHandlePartialMatches() {
            // Given
            String searchTerm = "jo";
            List<Cliente> clientes = Arrays.asList(
                TestDataBuilder.buildCliente(1L, "João Silva", "joao@email.com"),
                TestDataBuilder.buildCliente(2L, "José Santos", "jose@email.com")
            );
            
            when(clienteRepository.findByNomeContainingIgnoreCase(searchTerm)).thenReturn(clientes);

            // When
            List<ClienteResponse> result = clienteService.findByNomeContainingIgnoreCase(searchTerm);

            // Then
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            // Given
            String searchTerm = "SILVA";
            List<Cliente> clientes = Arrays.asList(
                TestDataBuilder.buildCliente(1L, "João Silva", "joao@email.com")
            );
            
            when(clienteRepository.findByNomeContainingIgnoreCase(searchTerm)).thenReturn(clientes);

            // When
            List<ClienteResponse> result = clienteService.findByNomeContainingIgnoreCase(searchTerm);

            // Then
            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("existsByEmail() Tests")
    class ExistsByEmailTests {

        @Test
        @DisplayName("Should return true when email exists")
        void shouldReturnTrueWhenEmailExists() {
            // Given
            String email = "joao.silva@email.com";
            when(clienteRepository.existsByEmail(email)).thenReturn(true);

            // When
            boolean result = clienteService.existsByEmail(email);

            // Then
            assertTrue(result, "Should return true when email exists");
            
            verify(clienteRepository, times(1)).existsByEmail(email);
        }

        @Test
        @DisplayName("Should return false when email does not exist")
        void shouldReturnFalseWhenEmailDoesNotExist() {
            // Given
            String email = "naoexiste@email.com";
            when(clienteRepository.existsByEmail(email)).thenReturn(false);

            // When
            boolean result = clienteService.existsByEmail(email);

            // Then
            assertFalse(result, "Should return false when email does not exist");
            
            verify(clienteRepository, times(1)).existsByEmail(email);
        }

        @Test
        @DisplayName("Should check exact email match")
        void shouldCheckExactEmailMatch() {
            // Given
            String email = "test@example.com";
            when(clienteRepository.existsByEmail(email)).thenReturn(true);

            // When
            clienteService.existsByEmail(email);

            // Then
            verify(clienteRepository).existsByEmail(eq(email));
        }
    }

    @Nested
    @DisplayName("obterRankingClientesPorNumeroPedidos() Tests")
    class ObterRankingClientesPorNumeroPedidosTests {

        @Test
        @DisplayName("Should return ranking from repository")
        void shouldReturnRankingFromRepository() {
            // Given
            List<RankingClienteResponse> ranking = new ArrayList<>();
            when(clienteRepository.obterRankingClientesPorNumeroPedidos()).thenReturn(ranking);

            // When
            List<RankingClienteResponse> result = clienteService.obterRankingClientesPorNumeroPedidos();

            // Then
            assertNotNull(result);
            assertSame(ranking, result, "Should return the same list from repository");
            
            verify(clienteRepository, times(1)).obterRankingClientesPorNumeroPedidos();
        }

        @Test
        @DisplayName("Should delegate to repository without modification")
        void shouldDelegateToRepositoryWithoutModification() {
            // Given
            List<RankingClienteResponse> ranking = Collections.emptyList();
            when(clienteRepository.obterRankingClientesPorNumeroPedidos()).thenReturn(ranking);

            // When
            clienteService.obterRankingClientesPorNumeroPedidos();

            // Then
            verify(clienteRepository, times(1)).obterRankingClientesPorNumeroPedidos();
            verifyNoMoreInteractions(clienteRepository);
        }
    }
}
