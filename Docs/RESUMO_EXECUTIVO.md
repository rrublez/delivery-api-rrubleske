# 📋 Resumo Executivo - Testes Manuais Delivery API

## 🎯 Visão Geral Rápida

| Métrica | Valor |
|---------|-------|
| **Total de Testes** | 32 testes |
| **Grupos** | 4 grupos organizados |
| **Status HTTP Testados** | 201, 200, 400, 422 |
| **Objetos Criados** | 3 Clientes + 2 Restaurantes + 6 Produtos + 3 Pedidos |
| **Tempo Estimado** | ~45 minutos |
| **Endpoints Testados** | 20+ endpoints |
| **Collections** | Postman (.json) + Bruno (.bru) |

---

## 📦 Arquivos Criados

```
delivery-api-rrubleske/
├── GUIA_TESTES_MANUAIS.md ..................... 📖 Guia completo (45 KB)
├── GUIA_IMPORTACAO_COLLECTIONS.md ............ 📥 Como importar (15 KB)
├── TESTES_MANUAIS_README.md .................. 📋 README rápido (20 KB)
├── TESTES_MANUAIS.html ....................... 🌐 Versão visual (30 KB)
├── delivery-api-postman.json ................. 📮 Collection Postman (50 KB)
├── delivery-api-bruno.bru .................... 🎯 Collection Bruno (20 KB)
└── curl-examples.sh .......................... 🖥️  Exemplos cURL (15 KB)
```

---

## 🧪 Estrutura dos 32 Testes

### Grupo 1: Testes de Validação (12 testes) ❌
Validam rejeição de dados inválidos

| # | Teste | Campo | Valores Inválidos | Status |
|---|-------|-------|-------------------|--------|
| 1 | Cliente - Email | email | "email-invalido" | 400 |
| 2 | Cliente - CPF | cpf | "00000000000" | 400 |
| 3 | Cliente - Nome | nome | "AB" | 400 |
| 4 | Cliente - Telefone | telefone | "123" | 400 |
| 5 | Restaurante - CNPJ | cnpj | "00000000000000" | 400 |
| 6 | Restaurante - Taxa | taxaEntrega | -5.00 | 400 |
| 7 | Produto - Preço Zero | preco | 0.00 | 400 |
| 8 | Produto - Preço Negativo | preco | -10.00 | 400 |
| 9 | Pedido - Número | numeroPedido | "pedido-123" | 400 |
| 10 | Pedido - Status | status | "INVALIDO" | 400 |
| 11 | Pedido - Quantidade | quantidade | 0 | 400 |
| 12 | Pedido - Itens | itens | [] | 400 |

### Grupo 2: Criação de Dados (11 testes) ✅
Criam base de dados para outros testes

```
📌 CLIENTES (3)
  └─ João Silva (joao@email.com, CPF: 12345678901)
  └─ Maria Santos (maria@email.com, CPF: 98765432109)
  └─ Pedro Oliveira (pedro@email.com, CPF: 55555555555)

🏪 RESTAURANTES (2)
  └─ Pizza Palace (Pizzaria, Taxa: R$ 5,00)
  └─ Sushi House (Japonesa, Taxa: R$ 8,50)

🍕 PRODUTOS (6)
  └─ Pizza Margherita (R$ 45,00)
  └─ Pizza Pepperoni (R$ 50,00)
  └─ Refrigerante 2L (R$ 8,00)
  └─ Combo Sushi Premium (R$ 120,00)
  └─ Temaki Salmão (R$ 35,00)
  └─ Sakê 300ml (R$ 25,00)

📦 PEDIDOS (3)
  └─ Pedido 1: PENDENTE, 2 itens, R$ 61,00
  └─ Pedido 2: ENTREGUE, 9 itens, R$ 282,00
  └─ Pedido 3: ENTREGUE, 7 itens, R$ 395,00
```

### Grupo 3: Consultas Simples (7 testes) 🔍
Recuperam dados salvos

- Buscar cliente por email
- Buscar produtos por categoria
- Listar restaurantes por ramo
- Listar pedidos por status
- Listar pedidos por cliente
- Listar produtos disponíveis
- Listar restaurantes ativos

### Grupo 4: Relatórios Complexos (8 testes) 📊
Agregam e analisam dados

| Endpoint | Descrição | Resultado |
|----------|-----------|-----------|
| `/relatorio/vendas-por-restaurante` | Total de vendas por restaurante | 2 linhas |
| `/relatorio/valor-acima?valor=100` | Pedidos > R$ 100 | 1 pedido |
| `/relatorio/valor-acima?valor=50` | Pedidos > R$ 50 | 2-3 pedidos |
| `/relatorio/periodo-status` | Por período e status | Filtra corretamente |
| `/relatorio/mais-vendidos` | Produtos ranking | 6 produtos |
| `/relatorio/ranking-por-pedidos` | Clientes ranking | 3 clientes |
| `/relatorio/faturamento-por-categoria` | Faturamento por tipo | 4 categorias |

---

## ⏱️ Timeline de Execução

```
┌─ 1 min ────────────────────────────────────────────────────────┐
│ ✓ Health Check                                                 │
│   GET /health → Status 200                                     │
└────────────────────────────────────────────────────────────────┘

┌─ 10 min ───────────────────────────────────────────────────────┐
│ ✓ Testes de Validação (12 testes)                              │
│   Todos devem retornar Status 400/422                          │
└────────────────────────────────────────────────────────────────┘

┌─ 10 min ───────────────────────────────────────────────────────┐
│ ✓ Criação de Dados (11 testes)                                 │
│   3 Clientes + 2 Restaurantes + 6 Produtos + 3 Pedidos        │
│   Todos devem retornar Status 201 Created                      │
└────────────────────────────────────────────────────────────────┘

┌─ 5 min ────────────────────────────────────────────────────────┐
│ ✓ Consultas Simples (7 testes)                                 │
│   Verificar que dados foram salvos corretamente                │
│   Todos devem retornar Status 200 OK                           │
└────────────────────────────────────────────────────────────────┘

┌─ 10 min ───────────────────────────────────────────────────────┐
│ ✓ Relatórios Complexos (8 testes)                              │
│   Validar agregações, cálculos, filtros                        │
│   Todos devem retornar Status 200 OK com dados                 │
└────────────────────────────────────────────────────────────────┘

TOTAL: ~45 minutos
```

---

## 🎯 Como Começar

### 1️⃣ Inicie a API
```bash
./mvnw spring-boot:run
# API estará disponível em http://localhost:8080
```

### 2️⃣ Importe a Collection
- **Postman**: Import → `delivery-api-postman.json`
- **Bruno**: File → Import → `delivery-api-bruno.bru`

### 3️⃣ Execute os Testes
Abra a collection e execute os grupos na ordem:
1. 0 - Setup e Validação
2. 1 - Testes de Validação
3. 2 - Criação de Dados Base
4. 3 - Consultas Simples
5. 4 - Relatórios

---

## 📊 Dados Esperados - Resultados dos Relatórios

### Vendas por Restaurante
```json
{
  "restaurante": "Pizza Palace",
  "totalVendas": 343.00  // Pedido 1 (61) + Pedido 2 (282)
}
{
  "restaurante": "Sushi House",
  "totalVendas": 395.00  // Pedido 3
}
```

### Pedidos com Valor > 100
```
Retorna: 1 pedido (Pedido 3 = R$ 395,00)
```

### Pedidos com Valor > 50
```
Retorna: 3 pedidos (Pedido 1, 2 e 3)
```

### Produtos Mais Vendidos
```
Pizza Margherita: 3 unidades
Pizza Pepperoni: 3 unidades
Refrigerante 2L: 6 unidades
Combo Sushi: 2 unidades
Temaki Salmão: 3 unidades
Sakê 300ml: 2 unidades
```

### Ranking de Clientes
```
João Silva: 1 pedido
Maria Santos: 1 pedido
Pedro Oliveira: 1 pedido
```

### Faturamento por Categoria
```
Pizzas: 280,00 (Margherita 90 + Pepperoni 150 + 40 desconto)
Bebidas: 99,00 (Refrigerante 48 + Sakê 51)
Combos: 240,00 (Combo Sushi 240)
Temakis: 105,00 (Temaki 105)
```

---

## ✅ Checklist de Verificação

- [ ] API respondendo: `curl http://localhost:8080/health`
- [ ] Health check: Status 200 (UP)
- [ ] Testes de validação: Todos com status 400/422 ✓
- [ ] 3 Clientes criados com sucesso
- [ ] 2 Restaurantes criados com sucesso
- [ ] 6 Produtos criados com sucesso
- [ ] 3 Pedidos criados com sucesso
- [ ] Todos os 7 testes de consulta retornam dados
- [ ] Todos os 8 testes de relatório retornam dados válidos
- [ ] Totais de pedidos estão corretos
- [ ] Agregações funcionam corretamente
- [ ] Filtros funcionam como esperado

---

## 🔧 Requisitos

✓ Java 21+  
✓ Maven 3.6+  
✓ Postman 7+ OU Bruno Desktop  
✓ cURL (opcional)  
✓ Navegador (para abrir TESTES_MANUAIS.html)  

---

## 📖 Documentação Disponível

| Arquivo | Descrição | Tamanho |
|---------|-----------|--------|
| `GUIA_TESTES_MANUAIS.md` | Guia detalhado completo | 45 KB |
| `GUIA_IMPORTACAO_COLLECTIONS.md` | Instruções passo-a-passo | 15 KB |
| `TESTES_MANUAIS_README.md` | README rápido | 20 KB |
| `TESTES_MANUAIS.html` | Versão visual interativa | 30 KB |
| `delivery-api-postman.json` | Collection Postman | 50 KB |
| `delivery-api-bruno.bru` | Collection Bruno | 20 KB |
| `curl-examples.sh` | Scripts cURL de exemplo | 15 KB |

---

## 💡 Dicas Importantes

### Postman
- As variáveis de ambiente são salvas automaticamente
- Use o Console (abaixo) para ver detalhes de respostas
- Cada teste é independente - pode executar isoladamente

### Bruno
- Arquivo é em formato texto (.bru)
- Perfeito para versionamento em Git
- IDs precisam ser ajustados manualmente

### Ambos
- Salve os resultados para referência
- Importe em sua equipe para testes consistentes
- Pode executar em CI/CD para automação

---

## 🐛 Troubleshooting Rápido

| Problema | Solução |
|----------|---------|
| "Connection refused" | Verifique se API está rodando: `./mvnw spring-boot:run` |
| "Email já registrado" | Use emails diferentes ou limpe o banco |
| "CPF/CNPJ inválido" | Use os valores fornecidos nesta documentação |
| "IDs retornam null" | Crie os dados antes ou copie IDs manualmente |
| "Relatórios vazios" | Verifique se criou os pedidos com status ENTREGUE |

---

## 📞 Próximos Passos

1. **Importe a collection** em Postman ou Bruno
2. **Execute o Health Check** para confirmar API
3. **Rode os testes de validação** para validar regras
4. **Crie os dados** necessários
5. **Teste as consultas** para confirmar salvamento
6. **Execute os relatórios** para validar agregações

---

**Versão:** 1.0 | **Data:** Novembro 2025  
**API:** Delivery Tech v1.0.0 | **Java:** 21 | **Spring Boot:** 3.4.11

Para mais detalhes, consulte `GUIA_TESTES_MANUAIS.md`
