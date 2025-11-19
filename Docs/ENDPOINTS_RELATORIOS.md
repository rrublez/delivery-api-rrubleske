# Relatório Controller - Endpoints de Relatórios

## Visão Geral
O `RelatorioController` consolida todos os endpoints de relatório em um único ponto de acesso bem organizado.

## Base URL
```
/api/relatorios
```

## 🔐 Autenticação e Segurança

- Apenas `POST /api/auth/register`, `POST /api/auth/login`, `GET /api/restaurantes`, `GET /api/produtos` e `GET /actuator/health` podem ser acessados sem credenciais.
- Todos os endpoints de relatório demandam `Authorization: Bearer <token>` com `role` apropriado (`ADMIN`, `RESTAURANTE` ou `CLIENTE`).
- Usuários inativos ou com `role` incorreto receberão `401 Unauthorized` mesmo com headers válidos.

```bash
curl -X GET "http://localhost:8080/api/relatorios/vendas-por-restaurante" \
  -H "Authorization: Bearer $TOKEN"
```

## Endpoints

### 1. Vendas por Restaurante
**GET** `/api/relatorios/vendas-por-restaurante`

Retorna as vendas totais agrupadas por restaurante, incluindo quantidade de pedidos, total de vendas e ticket médio.

**Resposta (200 OK):**
```json
[
  {
    "restauranteId": 1,
    "restauranteNome": "Pizzaria XYZ",
    "totalPedidos": 15,
    "totalVendas": 450.00,
    "ticketMedio": 30.00
  },
  {
    "restauranteId": 2,
    "restauranteNome": "Burgeria ABC",
    "totalPedidos": 12,
    "totalVendas": 240.00,
    "ticketMedio": 20.00
  }
]
```

**Campos da Resposta:**
- `restauranteId`: ID do restaurante
- `restauranteNome`: Nome do restaurante
- `totalPedidos`: Quantidade total de pedidos
- `totalVendas`: Valor total vendido (BigDecimal)
- `ticketMedio`: Valor médio por pedido (BigDecimal)

---

### 2. Produtos Mais Vendidos
**GET** `/api/relatorios/produtos-mais-vendidos`

Retorna o top 10 de produtos mais vendidos, ordenados por quantidade vendida e faturamento.

**Resposta (200 OK):**
```json
[
  {
    "produtoId": 5,
    "produtoNome": "Pizza Margherita",
    "categoria": "Pizzas",
    "quantidadeVendida": 250,
    "faturamento": 2500.00
  },
  {
    "produtoId": 8,
    "produtoNome": "Refrigerante 2L",
    "categoria": "Bebidas",
    "quantidadeVendida": 180,
    "faturamento": 360.00
  }
]
```

**Campos da Resposta:**
- `produtoId`: ID do produto
- `produtoNome`: Nome do produto
- `categoria`: Categoria do produto
- `quantidadeVendida`: Quantidade total vendida (Long)
- `faturamento`: Faturamento total (BigDecimal)

---

### 3. Clientes Ativos
**GET** `/api/relatorios/clientes-ativos`

Retorna o ranking de clientes mais ativos, ordenados pela quantidade de pedidos realizados.

**Resposta (200 OK):**
```json
[
  {
    "clienteId": 1,
    "clienteNome": "João Silva",
    "email": "joao@email.com",
    "totalPedidos": 25
  },
  {
    "clienteId": 3,
    "clienteNome": "Maria Santos",
    "email": "maria@email.com",
    "totalPedidos": 18
  }
]
```

**Campos da Resposta:**
- `clienteId`: ID do cliente
- `clienteNome`: Nome do cliente
- `email`: Email do cliente
- `totalPedidos`: Quantidade total de pedidos (Long)

---

### 4. Pedidos por Período
**GET** `/api/relatorios/pedidos-por-periodo`

Retorna pedidos dentro de um período específico, opcionalmente filtrado por status.

**Parâmetros de Query:**
- `dataInicial` (obrigatório): Data inicial do período (formato ISO 8601: `YYYY-MM-DDTHH:mm:ss`)
- `dataFinal` (obrigatório): Data final do período (formato ISO 8601: `YYYY-MM-DDTHH:mm:ss`)
- `status` (opcional): Status do pedido (PENDENTE, CONFIRMADO, ENTREGUE, CANCELADO)

**Exemplo:**
```
GET /api/relatorios/pedidos-por-periodo?dataInicial=2025-01-01T00:00:00&dataFinal=2025-12-31T23:59:59&status=ENTREGUE
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "numeroPedido": "PED-001",
    "status": "ENTREGUE",
    "clienteNome": "João Silva",
    "restauranteNome": "Pizzaria XYZ",
    "valorTotal": 85.50,
    "dataPedido": "2025-11-10T14:30:00"
  },
  {
    "id": 2,
    "numeroPedido": "PED-002",
    "status": "ENTREGUE",
    "clienteNome": "Maria Santos",
    "restauranteNome": "Burgeria ABC",
    "valorTotal": 42.00,
    "dataPedido": "2025-11-10T15:45:00"
  }
]
```

**Campos da Resposta:**
- `id`: ID do pedido
- `numeroPedido`: Número único do pedido
- `status`: Status atual do pedido
- `clienteNome`: Nome do cliente
- `restauranteNome`: Nome do restaurante
- `valorTotal`: Valor total do pedido (BigDecimal)
- `dataPedido`: Data e hora do pedido (LocalDateTime)

**Códigos de Status HTTP:**
- `200 OK`: Relatório retornado com sucesso
- `400 Bad Request`: Data inicial é posterior à data final
- `500 Internal Server Error`: Erro no processamento do relatório

---

## Validações

### Filtro de Período
- `dataInicial` é obrigatória
- `dataFinal` é obrigatória
- `dataInicial` deve ser anterior a `dataFinal`
- Formato aceito: ISO 8601 (`YYYY-MM-DDTHH:mm:ss`)

### Filtro de Status
- Opcional
- Valores válidos: PENDENTE, CONFIRMADO, ENTREGUE, CANCELADO
- Se não fornecido, retorna pedidos com qualquer status

---

## Exemplos de Uso com cURL

### 1. Vendas por Restaurante
```bash
curl -X GET "http://localhost:8080/api/relatorios/vendas-por-restaurante"
```

### 2. Produtos Mais Vendidos
```bash
curl -X GET "http://localhost:8080/api/relatorios/produtos-mais-vendidos"
```

### 3. Clientes Ativos
```bash
curl -X GET "http://localhost:8080/api/relatorios/clientes-ativos"
```

### 4. Pedidos por Período
```bash
# Sem filtro de status (todos os pedidos do período)
curl -X GET "http://localhost:8080/api/relatorios/pedidos-por-periodo?dataInicial=2025-01-01T00:00:00&dataFinal=2025-12-31T23:59:59"

# Com filtro de status
curl -X GET "http://localhost:8080/api/relatorios/pedidos-por-periodo?dataInicial=2025-01-01T00:00:00&dataFinal=2025-12-31T23:59:59&status=ENTREGUE"
```

---

## Arquivo de Teste Manual

Ver arquivo: `Docs/curl-examples-relatorios.sh`

---

## Consolidação de Endpoints

Os seguintes endpoints antigos foram **consolidados** no RelatorioController:

| Antigo Endpoint | Novo Endpoint | Status |
|---|---|---|
| `GET /api/clientes/ranking` | `GET /api/relatorios/clientes-ativos` | ✅ Consolidado |
| `GET /api/pedidos/relatorios/vendas` | `GET /api/relatorios/vendas-por-restaurante` | ✅ Consolidado |
| `GET /api/pedidos/relatorios/relatorio` | `GET /api/relatorios/pedidos-por-periodo` | ✅ Consolidado |
| N/A | `GET /api/relatorios/produtos-mais-vendidos` | ✅ Novo |

**Nota:** Os endpoints antigos ainda funcionam, mas recomenda-se usar os novos endpoints do RelatorioController.

---

## Estrutura de Camadas

```
Controller: RelatorioController
    ↓
Service: RelatorioService (interface) / RelatorioServiceImpl (implementação)
    ↓
Repository: PedidoRepository, ProdutoRepository, ClienteRepository
    ↓
Entity: Pedido, Produto, Cliente, Restaurante
```

---

## DTOs Utilizados

### Response DTOs
- `VendasPorRestauranteResponse` (localizado em: `dto/shared/response/`)
- `ProdutoMaisVendidoResponse` (localizado em: `dto/produto/response/`)
- `RankingClienteResponse` (localizado em: `dto/shared/response/`)
- `PedidoRelatorioResponse` (localizado em: `dto/pedido/response/`)

### Request DTOs
- `FiltroRelatorioRequest` (localizado em: `dto/relatorio/request/`)

---

## Observações Importantes

1. **Ordenação Padrão**: 
   - Vendas por Restaurante: Ordenado por total de vendas (DESC)
   - Produtos Mais Vendidos: Ordenado por quantidade vendida e faturamento
   - Clientes Ativos: Ordenado por total de pedidos (DESC)
   - Pedidos por Período: Ordenado por data do pedido (DESC)

2. **Performance**: As queries utilizam JPA com GROUP BY para agregação eficiente

3. **Tratamento de Erros**: Erros internos retornam status 500

4. **Autenticação**: Nenhuma autenticação é requerida (pode ser adicionada futuramente)

