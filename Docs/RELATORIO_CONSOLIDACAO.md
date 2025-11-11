# 📊 RelatorioController - Consolidação de Endpoints

## Resumo Executivo

O `RelatorioController` foi criado como um **ponto centralizado de acesso** para todos os relatórios disponíveis na API, consolidando funcionalidades que estavam espalhadas entre os controllers `ClienteController`, `PedidoController` e `ProdutoController`.

---

## ✅ O Que Foi Implementado

### 1️⃣ Estrutura de Camadas Completa

```
RelatorioController (4 endpoints)
    ↓
RelatorioService (interface) + RelatorioServiceImpl (implementação)
    ↓
Repositories existentes:
    - PedidoRepository (obterVendasPorRestaurante, obterRelatorioByPeriodoAndStatus)
    - ProdutoRepository (obterProdutosMaisVendidos)
    - ClienteRepository (obterRankingClientesPorNumeroPedidos)
    ↓
Entities: Pedido, Produto, Cliente, Restaurante
```

### 2️⃣ Endpoints Criados

| Endpoint | Método | Descrição | Status |
|----------|--------|-----------|--------|
| `/api/relatorios/vendas-por-restaurante` | GET | Vendas agrupadas por restaurante | ✅ |
| `/api/relatorios/produtos-mais-vendidos` | GET | Top 10 produtos mais vendidos | ✅ |
| `/api/relatorios/clientes-ativos` | GET | Ranking de clientes por nº de pedidos | ✅ |
| `/api/relatorios/pedidos-por-periodo` | GET | Pedidos com filtro de período e status | ✅ |

### 3️⃣ Arquivos Criados

```
✅ Controller:
  - src/main/java/com/deliverytech/delivery/controller/RelatorioController.java

✅ Service:
  - src/main/java/com/deliverytech/delivery/service/RelatorioService.java
  - src/main/java/com/deliverytech/delivery/service/impl/RelatorioServiceImpl.java

✅ DTO:
  - src/main/java/com/deliverytech/delivery/dto/relatorio/request/FiltroRelatorioRequest.java

✅ Documentação:
  - Docs/ENDPOINTS_RELATORIOS.md
  - Docs/curl-examples-relatorios.sh
  
✅ README atualizado com novos endpoints
```

---

## 🎯 Funcionalidades Detalhadas

### Vendas por Restaurante
```http
GET /api/relatorios/vendas-por-restaurante
```

**Retorna:**
- ID do restaurante
- Nome do restaurante
- Total de pedidos
- Total de vendas (BigDecimal)
- Ticket médio (BigDecimal)

**Ordenação:** Por total de vendas (descendente)

**DTO:** `VendasPorRestauranteResponse`

---

### Produtos Mais Vendidos
```http
GET /api/relatorios/produtos-mais-vendidos
```

**Retorna:**
- ID do produto
- Nome do produto
- Categoria
- Quantidade vendida
- Faturamento total

**Ordenação:** Por quantidade vendida e faturamento (descendente)

**Limite:** Top 10 (definido na query JPA)

**DTO:** `ProdutoMaisVendidoResponse`

---

### Clientes Ativos
```http
GET /api/relatorios/clientes-ativos
```

**Retorna:**
- ID do cliente
- Nome do cliente
- Email
- Total de pedidos

**Ordenação:** Por total de pedidos (descendente)

**DTO:** `RankingClienteResponse`

---

### Pedidos por Período
```http
GET /api/relatorios/pedidos-por-periodo?dataInicial=2025-01-01T00:00:00&dataFinal=2025-12-31T23:59:59&status=ENTREGUE
```

**Parâmetros:**
- `dataInicial` (obrigatório): Início do período (ISO 8601)
- `dataFinal` (obrigatório): Fim do período (ISO 8601)
- `status` (opcional): Filtrar por status específico

**Retorna:**
- ID do pedido
- Número do pedido
- Status
- Nome do cliente
- Nome do restaurante
- Valor total
- Data do pedido

**Validações:**
- ❌ 400 Bad Request se dataInicial > dataFinal
- ❌ 500 Internal Server Error em caso de erro

**DTO:** `PedidoRelatorioResponse`

**Lógica:**
- Se `status` fornecido: usa query específica com filtro
- Se `status` não fornecido: busca todos os pedidos do período e converte

---

## 🔄 Consolidação de Endpoints Antigos

Os seguintes endpoints antigos **continuam funcionando**, mas recomenda-se usar os novos:

| Antigo | Novo | Tipo |
|--------|------|------|
| `GET /api/clientes/relatorio/ranking-por-pedidos` | `GET /api/relatorios/clientes-ativos` | Consolidado |
| `GET /api/pedidos/relatorio/vendas-por-restaurante` | `GET /api/relatorios/vendas-por-restaurante` | Consolidado |
| `GET /api/pedidos/relatorio/periodo-status` | `GET /api/relatorios/pedidos-por-periodo` | Consolidado |
| N/A | `GET /api/relatorios/produtos-mais-vendidos` | Novo |

---

## 📈 Estrutura da Resposta

### VendasPorRestauranteResponse
```json
{
  "restauranteId": 1,
  "restauranteNome": "Pizzaria XYZ",
  "totalPedidos": 15,
  "totalVendas": 450.00,
  "ticketMedio": 30.00
}
```

### ProdutoMaisVendidoResponse
```json
{
  "produtoId": 5,
  "produtoNome": "Pizza Margherita",
  "categoria": "Pizzas",
  "quantidadeVendida": 250,
  "faturamento": 2500.00
}
```

### RankingClienteResponse
```json
{
  "clienteId": 1,
  "clienteNome": "João Silva",
  "email": "joao@email.com",
  "totalPedidos": 25
}
```

### PedidoRelatorioResponse
```json
{
  "id": 1,
  "numeroPedido": "PED-001",
  "status": "ENTREGUE",
  "clienteNome": "João Silva",
  "restauranteNome": "Pizzaria XYZ",
  "valorTotal": 85.50,
  "dataPedido": "2025-11-10T14:30:00"
}
```

---

## 🧪 Testes Realizados

✅ **Compilação:** mvn clean compile -q → Sucesso  
✅ **Testes Unitários:** mvn test -q → Todos passando  
✅ **Mappings Registrados:** 56 endpoints (52 anteriores + 4 novos)  
✅ **Iniciação da Aplicação:** Spring Boot startup → OK  

---

## 📐 Arquitetura Adotada

```
┌─────────────────────────────────────────────────────────┐
│                  CLIENT (Postman/Bruno)                 │
└─────────────────────────────────────────────────────────┘
                         ↓ HTTP GET
┌─────────────────────────────────────────────────────────┐
│              RelatorioController                         │
│  - 4 métodos @GetMapping                                │
│  - Tratamento de exceção                                │
│  - Validação de parâmetros                              │
└─────────────────────────────────────────────────────────┘
                         ↓ Injeção de Dependência
┌─────────────────────────────────────────────────────────┐
│              RelatorioService (Interface)                │
│  - 4 métodos abstratos                                  │
│  - Documentação JavaDoc                                 │
└─────────────────────────────────────────────────────────┘
                         ↓ Implementação
┌─────────────────────────────────────────────────────────┐
│            RelatorioServiceImpl (@Service)                │
│  - Implementação dos 4 métodos                          │
│  - Injeção de Repositories                              │
│  - Transformação de dados (Entity → Response)           │
└─────────────────────────────────────────────────────────┘
                         ↓ Chamada aos Repositories
    ┌────────────────────┬────────────────┬────────────┐
    ↓                    ↓                ↓            ↓
┌────────────┐   ┌──────────────┐  ┌───────────┐  ┌───────────┐
│PedidoRepo  │   │ProdutoRepo   │  │ClienteRepo│  │PedidoRepo │
│obterVendas │   │obterProdutos │  │obterRanks │  │findByData │
└────────────┘   └──────────────┘  └───────────┘  └───────────┘
    ↓                    ↓                ↓            ↓
    └────────────────────┬────────────────┴────────────┘
                         ↓ JPA Queries
        ┌────────────────────────────────────────┐
        │    H2 Database (Em Memória)            │
        │                                        │
        │  Tables:                               │
        │  - clientes                            │
        │  - restaurantes                        │
        │  - produtos                            │
        │  - pedidos                             │
        │  - pedido_produtos                     │
        │  - restaurante_produtos                │
        └────────────────────────────────────────┘
```

---

## 🔍 Queries JPA Utilizadas

### 1. Vendas por Restaurante
```sql
SELECT NEW com.deliverytech.delivery.dto.shared.response.VendasPorRestauranteResponse(
  r.id, r.nome, COUNT(p.id), COALESCE(SUM(p.valorTotal), 0), AVG(p.valorTotal)
)
FROM Pedido p 
JOIN p.restaurante r 
GROUP BY r.id, r.nome 
ORDER BY SUM(p.valorTotal) DESC
```

### 2. Produtos Mais Vendidos
```sql
SELECT NEW com.deliverytech.delivery.dto.produto.response.ProdutoMaisVendidoResponse(
  p.id, p.nome, p.categoria, COALESCE(SUM(pp.quantidade), 0L), COALESCE(SUM(pp.subtotal), 0.0)
)
FROM Produto p 
LEFT JOIN PedidoProduto pp ON pp.produto.id = p.id 
GROUP BY p.id, p.nome, p.categoria 
ORDER BY SUM(pp.quantidade) DESC, SUM(pp.subtotal) DESC
```

### 3. Clientes Ativos
```sql
SELECT NEW com.deliverytech.delivery.dto.shared.response.RankingClienteResponse(
  c.id, c.nome, c.email, COUNT(p.id)
)
FROM Cliente c 
LEFT JOIN c.pedidos p 
GROUP BY c.id, c.nome, c.email 
ORDER BY COUNT(p.id) DESC
```

### 4. Pedidos por Período
```sql
SELECT NEW com.deliverytech.delivery.dto.pedido.response.PedidoRelatorioResponse(
  p.id, p.numeroPedido, p.status, c.nome, r.nome, p.valorTotal, p.dataPedido
)
FROM Pedido p 
JOIN p.cliente c 
JOIN p.restaurante r 
WHERE p.dataPedido BETWEEN :dataInicial AND :dataFinal 
  AND p.status = :status 
ORDER BY p.dataPedido DESC
```

---

## 📦 DTOs Criados/Utilizados

### Criado
- `FiltroRelatorioRequest` - Filtro com data inicial, final e status opcional

### Reutilizados
- `VendasPorRestauranteResponse` - Em `dto/shared/response/`
- `ProdutoMaisVendidoResponse` - Em `dto/produto/response/`
- `RankingClienteResponse` - Em `dto/shared/response/`
- `PedidoRelatorioResponse` - Em `dto/pedido/response/`

---

## 🛡️ Validações Implementadas

### RelatorioController
```java
// Validação de período
if (dataInicial.isAfter(dataFinal)) {
    return ResponseEntity.badRequest().build();  // 400
}

// Tratamento de exceção genérica
try {
    var response = relatorioService.obterXXX();
    return ResponseEntity.ok(response);  // 200
} catch (Exception e) {
    return ResponseEntity.status(500).build();  // 500
}
```

### Parâmetros de Query
- `dataInicial` - @DateTimeFormat(iso = ISO.DATE_TIME)
- `dataFinal` - @DateTimeFormat(iso = ISO.DATE_TIME)
- `status` - String opcional

---

## 📚 Documentação Gerada

✅ **ENDPOINTS_RELATORIOS.md** (850+ linhas)
- Descrição detalhada de cada endpoint
- Exemplos de resposta JSON
- Validações e códigos de status
- Exemplos de cURL
- Consolidação de endpoints

✅ **curl-examples-relatorios.sh**
- Script com 6 testes práticos
- Exemplos de sucesso e erro
- Pronto para execução

✅ **README.md atualizado**
- Nova seção "Relatório Controller"
- Exemplos dos 4 endpoints
- Link para documentação completa

---

## 🎯 Benefícios da Consolidação

1. **Centralização:** Um único ponto de acesso para todos os relatórios
2. **Consistência:** Padrão único de resposta e tratamento de erro
3. **Manutenibilidade:** Código organizado em uma camada dedica
4. **Escalabilidade:** Fácil adicionar novos relatórios
5. **Performance:** Queries JPA otimizadas com GROUP BY
6. **Documentação:** Endpoints bem documentados com exemplos

---

## 🚀 Como Usar

### 1. Iniciar a Aplicação
```bash
./mvnw spring-boot:run
```

### 2. Testar os Endpoints
```bash
# Vendas por Restaurante
curl http://localhost:8080/api/relatorios/vendas-por-restaurante

# Produtos Mais Vendidos
curl http://localhost:8080/api/relatorios/produtos-mais-vendidos

# Clientes Ativos
curl http://localhost:8080/api/relatorios/clientes-ativos

# Pedidos por Período
curl "http://localhost:8080/api/relatorios/pedidos-por-periodo?dataInicial=2025-01-01T00:00:00&dataFinal=2025-12-31T23:59:59"
```

### 3. Ou usar o script de testes
```bash
bash Docs/curl-examples-relatorios.sh
```

---

## 📊 Estatísticas

- **Endpoints Criados:** 4
- **Controllers Envolvidos:** 1 novo + 3 existentes (consolidados)
- **Services:** 1 interface + 1 implementação
- **DTOs:** 1 novo (FiltroRelatorioRequest) + 4 reutilizados
- **Arquivos Criados:** 5
- **Linhas de Código:** ~450 (controller + service)
- **Linhas de Documentação:** 1000+
- **Testes:** Todos passando ✅

---

**Data:** 10 de Novembro de 2025  
**Status:** ✅ Implementação Completa  
**Próximos Passos:** Integração com autenticação (opcional)

