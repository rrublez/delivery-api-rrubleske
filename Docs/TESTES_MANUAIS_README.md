# 📝 Testes Manuais - Delivery API

Documentação completa para testes manuais da API de Delivery com collections prontas para Postman e Bruno.

## 📂 Arquivos de Documentação

| Arquivo | Descrição |
|---------|-----------|
| **GUIA_TESTES_MANUAIS.md** | 📖 Guia completo com 4 grupos de testes, dados, validações e relatórios |
| **GUIA_IMPORTACAO_COLLECTIONS.md** | 📥 Instruções passo-a-passo para importar collections |
| **delivery-api-postman.json** | 📮 Collection completa para Postman (32 testes) |
| **delivery-api-bruno.bru** | 🎯 Collection completa para Bruno (32 testes) |

## 🚀 Início Rápido

### 1. Inicie a API
```bash
./mvnw spring-boot:run
```

### 2. Importe a Collection
- **Postman:** Import → Escolha `delivery-api-postman.json`
- **Bruno:** File → Import Collection → Escolha `delivery-api-bruno.bru`

### 3. Execute os Testes
Execute na ordem:
1. ✅ **0 - Setup e Validação** → Health check
2. ✅ **1 - Testes de Validação** → Validações (devem falhar)
3. ✅ **2 - Criação de Dados Base** → Criar dados para teste
4. ✅ **3 - Consultas Simples** → Verificar dados
5. ✅ **4 - Relatórios** → Testar endpoints complexos

## 📊 Estrutura dos Testes

### Grupo 1: Testes de Validação (12 testes)
Valida regras de negócio com valores inválidos:
- ❌ Email inválido
- ❌ CPF/CNPJ inválido  
- ❌ Valores negativas/zero
- ❌ Campos obrigatórios ausentes
- ❌ Tamanho de campos

**Status esperado:** 400 ou 422

### Grupo 2: Criação de Dados Base (11 testes)
Cria dados necessários para testes:
- ✅ 3 Clientes (João, Maria, Pedro)
- ✅ 2 Restaurantes (Pizza Palace, Sushi House)
- ✅ 6 Produtos (variados, 2 categorias)
- ✅ 3 Pedidos (poucos itens, muitos itens, valor alto)

**Status esperado:** 201 Created

### Grupo 3: Consultas Simples (7 testes)
Testa endpoints básicos de busca:
- 🔍 Buscar por email, categoria, ramo
- 🔍 Listar por status, disponibilidade
- 🔍 Filtrar por cliente

**Status esperado:** 200 OK

### Grupo 4: Relatórios Complexos (8 testes)
Testa endpoints de análise:
- 📊 Total de vendas por restaurante
- 📊 Pedidos com valor acima de X
- 📊 Relatório por período e status
- 📊 Produtos mais vendidos
- 📊 Ranking de clientes
- 📊 Faturamento por categoria

**Status esperado:** 200 OK com dados agregados

## 📋 Dados de Teste

### Clientes
```json
1. João Silva (joao@email.com, CPF: 12345678901)
2. Maria Santos (maria@email.com, CPF: 98765432109)
3. Pedro Oliveira (pedro@email.com, CPF: 55555555555)
```

### Restaurantes
```json
1. Pizza Palace (Pizzaria, CNPJ: 11222333000181, Taxa: R$5.00)
2. Sushi House (Japonesa, CNPJ: 11444555000182, Taxa: R$8.50)
```

### Produtos
```json
Pizza Palace:
  - Pizza Margherita: R$45.00
  - Pizza Pepperoni: R$50.00
  - Refrigerante 2L: R$8.00

Sushi House:
  - Combo Sushi Premium: R$120.00
  - Temaki Salmão: R$35.00
  - Sakê 300ml: R$25.00
```

### Pedidos
```json
Pedido 1 (PENDENTE): Pizza Margherita + 2x Refrigerante = R$61.00
Pedido 2 (ENTREGUE): 3x Pizza Pepperoni + 2x Pizza Margherita + 4x Refrigerante = R$282.00
Pedido 3 (ENTREGUE): 2x Combo Sushi + 3x Temaki + 2x Sakê = R$395.00
```

## 📌 Validações Importantes

Todos os seguintes cenários devem retornar erro **400/422**:

| Campo | Validação | Exemplo |
|-------|-----------|---------|
| Email | Formato válido | `email-invalido` ❌ |
| CPF | Válido (11 dígitos) | `00000000000` ❌ |
| CNPJ | Válido (14 dígitos) | `00000000000000` ❌ |
| Preço | > 0 | `0.00` ou `-10.00` ❌ |
| Nome | 3-50 caracteres | `AB` ❌ |
| Telefone | 10-15 caracteres | `123` ❌ |
| Status Pedido | PENDENTE/ENTREGUE/CANCELADO | `INVALIDO` ❌ |
| Quantidade | > 0 | `0` ❌ |
| Pedido Itens | Mínimo 1 | `[]` ❌ |

## 📊 Resultados Esperados - Relatórios

### Vendas por Restaurante
```json
[
  {
    "restauranteId": 1,
    "nomeRestaurante": "Pizza Palace",
    "totalVendas": 343.00
  },
  {
    "restauranteId": 2,
    "nomeRestaurante": "Sushi House",
    "totalVendas": 395.00
  }
]
```

### Produtos Mais Vendidos
```json
[
  {
    "produtoId": 1,
    "nomeProduto": "Pizza Margherita",
    "totalVendido": 3,
    "categoria": "Pizzas"
  }
]
```

### Ranking Clientes
```json
[
  {
    "clienteId": 1,
    "nomeCliente": "João Silva",
    "totalPedidos": 1
  }
]
```

### Faturamento por Categoria
```json
[
  {
    "categoria": "Pizzas",
    "totalFaturamento": 280.00
  }
]
```

## 🔧 Requisitos

- ✅ Java 21+
- ✅ API rodando em `http://localhost:8080`
- ✅ Postman (versão 7+) ou Bruno (desktop/web)
- ✅ cURL (opcional, para testes linha de comando)

## 📖 Documentação Completa

Para detalhes completos sobre cada teste, validações e troubleshooting, consulte:

- **[GUIA_TESTES_MANUAIS.md](./GUIA_TESTES_MANUAIS.md)** - Guia detalhado com screenshots e exemplos
- **[GUIA_IMPORTACAO_COLLECTIONS.md](./GUIA_IMPORTACAO_COLLECTIONS.md)** - Passo-a-passo de importação

## 🎯 Fluxo Típico de Teste

```
1. Health Check (1 min)
   ↓
2. Testes de Validação (10 min)
   ├─ Devem falhar com erro 400/422 ✅
   ↓
3. Criar Dados (10 min)
   ├─ 3 Clientes ✅
   ├─ 2 Restaurantes ✅
   ├─ 6 Produtos ✅
   ├─ 3 Pedidos ✅
   ↓
4. Consultas Simples (5 min)
   ├─ Verificar dados salvos ✅
   ↓
5. Relatórios (10 min)
   ├─ Validar agregações ✅
   ├─ Validar cálculos ✅
   ├─ Validar filtros ✅
```

**Tempo total:** ~45 minutos

## 💡 Exemplos cURL

### Health Check
```bash
curl -X GET "http://localhost:8080/health"
```

### Criar Cliente
```bash
curl -X POST "http://localhost:8080/api/clientes" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "11987654321",
    "cpf": "12345678901",
    "ativo": true
  }'
```

### Consultar Relatório
```bash
curl -X GET "http://localhost:8080/api/pedidos/relatorio/vendas-por-restaurante"
```

## 📞 Suporte e Dúvidas

Consulte a documentação completa nos guias fornecidos. Se encontrar erros:

1. Verifique se a API está rodando: `curl -X GET http://localhost:8080/health`
2. Consulte os logs: `tail -f logs/app.log`
3. Verifique o H2 Console: http://localhost:8080/h2-console (user: sa)

---

**Versão:** 1.0 | **Data:** Novembro 2025  
**API:** Delivery Tech v1.0.0 | **Java:** 21 | **Spring Boot:** 3.4.11
