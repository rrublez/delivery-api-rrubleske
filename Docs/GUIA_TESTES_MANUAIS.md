# Guia de Testes Manuais - Delivery API

**Versão:** 1.0  
**Data:** Novembro 2025  
**Ambiente:** Local (http://localhost:8080)

---

## 📋 Índice
1. [Visão Geral](#visão-geral)
2. [Configuração do Ambiente](#configuração-do-ambiente)
3. [Estrutura de Testes](#estrutura-de-testes)
4. [Dados de Teste](#dados-de-teste)
5. [Fluxo de Testes](#fluxo-de-testes)
6. [Testes de Validação](#testes-de-validação)
7. [Testes de Endpoints Complexos](#testes-de-endpoints-complexos)
8. [Troubleshooting](#troubleshooting)

---

## 🎯 Visão Geral

Este guia descreve o processo de testes manuais para a API de Delivery. Você pode executar os testes usando:

- **Postman** (importar `delivery-api-postman.json`)
- **Bruno** (importar `delivery-api-bruno.json`)
- **cURL** ou qualquer cliente HTTP

### Objetivos dos Testes

✅ Validar as regras de negócio
✅ Testar as validações de entrada
✅ Verificar os endpoints complexos (relatórios)
✅ Confirmar integridade dos dados
✅ Testar fluxos de criação de dados

---

## 🔧 Configuração do Ambiente

### Pré-requisitos

- Java 21 instalado
- Maven instalado
- Postman ou Bruno instalado (opcional, mas recomendado)
- cURL instalado (geralmente pré-instalado em Linux/Mac)

### Iniciando a Aplicação

```bash
# Navegar até o diretório do projeto
cd delivery-api-rrubleske

# Executar a aplicação
./mvnw spring-boot:run

# Ou compile e execute
./mvnw clean install
java -jar target/delivery-api-1.0.0.jar
```

**A API estará disponível em:** `http://localhost:8080`

### Verificar se a API está rodando

```bash
curl -X GET "http://localhost:8080/health"
```

Resposta esperada:
```json
{
  "status": "UP"
}
```

---

## 📊 Estrutura de Testes

Os testes estão organizados em **4 grupos principais**:

### 1. **Testes de Validação** 
Testa valores inválidos e regras de validação

- ❌ Validação de CPF inválido
- ❌ Validação de CNPJ inválido
- ❌ Email duplicado
- ❌ Nome vazio
- ❌ Valores negativos/zero
- ❌ Campos obrigatórios ausentes

### 2. **Criação de Dados Base**
Cria objetos necessários para testes complexos

- ✅ Criação de 3 Clientes
- ✅ Criação de 2 Restaurantes (categorias diferentes)
- ✅ Criação de 5 Produtos variados
- ✅ Criação de 3 Pedidos (variados em quantidade de itens)

### 3. **Consultas Simples**
Testa endpoints de busca básica

- 🔍 Buscar cliente por email
- 🔍 Buscar produtos por categoria
- 🔍 Listar restaurantes por ramo de atividade
- 🔍 Listar pedidos por status

### 4. **Relatórios e Endpoints Complexos**
Testa endpoints de análise de dados

- 📊 Total de vendas por restaurante
- 📊 Pedidos com valor acima de X
- 📊 Relatório por período e status
- 📊 Produtos mais vendidos
- 📊 Ranking de clientes por número de pedidos
- 📊 Faturamento por categoria

---

## 📝 Dados de Teste

### 🧑 Clientes (3 registros)

| ID | Nome | Email | CPF | Telefone | Status |
|---|---|---|---|---|---|
| (auto) | João Silva | joao@email.com | 12345678901 | 11987654321 | Ativo |
| (auto) | Maria Santos | maria@email.com | 98765432109 | 11912345678 | Ativo |
| (auto) | Pedro Oliveira | pedro@email.com | 55555555555 | 11955555555 | Ativo |

### 🏪 Restaurantes (2 registros - categorias diferentes)

| ID | Nome | Categoria (Ramo) | CNPJ | Telefone | Taxa Entrega | Status |
|---|---|---|---|---|---|---|
| (auto) | Pizza Palace | Pizzaria | 11222333000181 | 1133334444 | 5.00 | Ativo |
| (auto) | Sushi House | Japonesa | 11444555000182 | 1144445555 | 8.50 | Ativo |

### 🍕 Produtos (5+ registros)

#### Pizza Palace (Pizzaria)
| ID | Nome | Categoria | Preço | Disponível |
|---|---|---|---|---|
| (auto) | Pizza Margherita | Pizzas | 45.00 | Sim |
| (auto) | Pizza Pepperoni | Pizzas | 50.00 | Sim |
| (auto) | Refrigerante 2L | Bebidas | 8.00 | Sim |

#### Sushi House (Japonesa)
| ID | Nome | Categoria | Preço | Disponível |
|---|---|---|---|---|
| (auto) | Combo Sushi Premium | Combos | 120.00 | Sim |
| (auto) | Temaki Salmão | Temakis | 35.00 | Sim |
| (auto) | Sakê 300ml | Bebidas | 25.00 | Sim |

### 📦 Pedidos (3 registros)

#### Pedido 1 - Poucos itens
- **Cliente:** João Silva
- **Restaurante:** Pizza Palace
- **Status:** PENDENTE
- **Itens:** Pizza Margherita (1x), Refrigerante (2x)
- **Valor esperado:** ~61.00

#### Pedido 2 - Muitos itens
- **Cliente:** Maria Santos
- **Restaurante:** Pizza Palace
- **Status:** ENTREGUE
- **Itens:** Pizza Pepperoni (3x), Pizza Margherita (2x), Refrigerante (4x)
- **Valor esperado:** ~282.00

#### Pedido 3 - Valor alto
- **Cliente:** Pedro Oliveira
- **Restaurante:** Sushi House
- **Status:** ENTREGUE
- **Itens:** Combo Sushi Premium (2x), Temaki Salmão (3x), Sakê (2x)
- **Valor esperado:** ~395.00

---

## 🎬 Fluxo de Testes

### Passo 1: Validar Ambiente
```bash
curl -X GET "http://localhost:8080/health"
```

### Passo 2: Executar Testes de Validação
Importar collection e executar grupo "1 - Testes de Validação"

**Objetivo:** Verificar se as validações funcionam corretamente

### Passo 3: Criar Dados Base
Executar grupo "2 - Criação de Dados Base" na ordem:
1. Criar Clientes
2. Criar Restaurantes
3. Criar Produtos
4. Criar Pedidos

**Importante:** Copiar os IDs retornados para usar nos próximos passos!

### Passo 4: Consultas Simples
Executar grupo "3 - Consultas Simples"

**Objetivo:** Validar que os dados foram salvos corretamente

### Passo 5: Testes de Relatórios
Executar grupo "4 - Relatórios e Endpoints Complexos"

**Objetivo:** Verificar se os relatórios retornam dados esperados

---

## ✔️ Testes de Validação

Cada teste abaixo deve **retornar status 400 ou 422** com mensagem de erro apropriada.

### Validação de Cliente

#### ❌ Teste: Email Inválido
```json
{
  "nome": "Teste",
  "email": "email-invalido",
  "telefone": "11987654321",
  "cpf": "12345678901",
  "ativo": true
}
```
**Esperado:** Error - "Email deve ser válido"

#### ❌ Teste: CPF Duplicado
```json
{
  "nome": "Teste",
  "email": "teste@email.com",
  "telefone": "11987654321",
  "cpf": "12345678901",
  "ativo": true
}
```
**Esperado:** Error - "CPF já está registrado no sistema" (após criar primeiro cliente)

#### ❌ Teste: CPF Inválido
```json
{
  "nome": "Teste",
  "email": "teste@email.com",
  "telefone": "11987654321",
  "cpf": "00000000000",
  "ativo": true
}
```
**Esperado:** Error - "CPF deve ser válido"

#### ❌ Teste: Nome Muito Curto
```json
{
  "nome": "AB",
  "email": "teste@email.com",
  "telefone": "11987654321",
  "cpf": "12345678901",
  "ativo": true
}
```
**Esperado:** Error - "Nome deve ter entre 3 e 50 caracteres"

#### ❌ Teste: Telefone Inválido (muito curto)
```json
{
  "nome": "Teste",
  "email": "teste@email.com",
  "telefone": "123",
  "cpf": "12345678901",
  "ativo": true
}
```
**Esperado:** Error - "Telefone deve ter entre 10 e 15 caracteres"

#### ❌ Teste: Campo Obrigatório Ausente (email)
```json
{
  "nome": "Teste",
  "telefone": "11987654321",
  "cpf": "12345678901",
  "ativo": true
}
```
**Esperado:** Error 400 - "Email é obrigatório"

---

### Validação de Restaurante

#### ❌ Teste: CNPJ Inválido
```json
{
  "nome": "Teste Restaurante",
  "endereco": "Rua Teste, 123",
  "telefone": "1133334444",
  "cnpj": "00000000000000",
  "ramoAtividade": "Pizzaria",
  "ativo": true,
  "taxaEntrega": 5.00
}
```
**Esperado:** Error - "CNPJ deve ser válido"

#### ❌ Teste: Taxa Entrega Negativa
```json
{
  "nome": "Teste Restaurante",
  "endereco": "Rua Teste, 123",
  "telefone": "1133334444",
  "cnpj": "11222333000181",
  "ramoAtividade": "Pizzaria",
  "ativo": true,
  "taxaEntrega": -5.00
}
```
**Esperado:** Error - "Taxa de entrega não pode ser negativa"

#### ❌ Teste: CNPJ Duplicado
```json
{
  "nome": "Outro Nome",
  "endereco": "Rua Teste, 456",
  "telefone": "1166667777",
  "cnpj": "11222333000181",
  "ramoAtividade": "Pizzaria",
  "ativo": true,
  "taxaEntrega": 5.00
}
```
**Esperado:** Error - "CNPJ já está registrado no sistema" (após criar primeiro restaurante)

#### ❌ Teste: Endereço Muito Curto
```json
{
  "nome": "Teste",
  "endereco": "Rua",
  "telefone": "1133334444",
  "cnpj": "11222333000181",
  "ramoAtividade": "Pizzaria",
  "ativo": true,
  "taxaEntrega": 5.00
}
```
**Esperado:** Error - "Endereço deve ter entre 5 e 255 caracteres"

---

### Validação de Produto

#### ❌ Teste: Preço Zero
```json
{
  "nome": "Produto Teste",
  "descricao": "Descrição do produto",
  "preco": 0.00,
  "disponivel": true,
  "categoria": "Categoria"
}
```
**Esperado:** Error - "Preço deve ser maior que 0"

#### ❌ Teste: Preço Negativo
```json
{
  "nome": "Produto Teste",
  "descricao": "Descrição do produto",
  "preco": -10.00,
  "disponivel": true,
  "categoria": "Categoria"
}
```
**Esperado:** Error - "Preço deve ser maior que 0"

#### ❌ Teste: Nome Muito Longo
```json
{
  "nome": "A muito longo nome de produto que excede cento e cem caracteres para testar a validação",
  "descricao": "Descrição do produto",
  "preco": 50.00,
  "disponivel": true,
  "categoria": "Categoria"
}
```
**Esperado:** Error - "Nome deve ter entre 3 e 100 caracteres"

---

### Validação de Pedido

#### ❌ Teste: Número de Pedido com caracteres inválidos
```json
{
  "numeroPedido": "pedido-123",
  "status": "PENDENTE",
  "clienteId": 1,
  "restauranteId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2,
      "precoUnitario": 45.00
    }
  ]
}
```
**Esperado:** Error - "Número do pedido deve conter apenas letras maiúsculas, números e hífen"

#### ❌ Teste: Status Inválido
```json
{
  "numeroPedido": "PEDIDO-001",
  "status": "INVALIDO",
  "clienteId": 1,
  "restauranteId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2,
      "precoUnitario": 45.00
    }
  ]
}
```
**Esperado:** Error - "Status deve ser PENDENTE, ENTREGUE ou CANCELADO"

#### ❌ Teste: Quantidade Zero
```json
{
  "numeroPedido": "PEDIDO-001",
  "status": "PENDENTE",
  "clienteId": 1,
  "restauranteId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 0,
      "precoUnitario": 45.00
    }
  ]
}
```
**Esperado:** Error - "Quantidade deve ser maior que 0"

#### ❌ Teste: Pedido sem itens
```json
{
  "numeroPedido": "PEDIDO-001",
  "status": "PENDENTE",
  "clienteId": 1,
  "restauranteId": 1,
  "itens": []
}
```
**Esperado:** Error - "Pedido deve conter pelo menos um item"

---

## 📊 Testes de Endpoints Complexos

Após criar todos os dados base, execute estes testes para validar os relatórios.

### 1. Total de Vendas por Restaurante

**Endpoint:** `GET /api/pedidos/relatorio/vendas-por-restaurante`

**Resultado esperado:**
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

**Validações:**
- ✅ Todos os restaurantes aparecem
- ✅ Valores estão corretos (soma dos pedidos ENTREGUE)
- ✅ Ordenação sensível

---

### 2. Pedidos com Valor Acima de X

**Endpoint:** `GET /api/pedidos/relatorio/valor-acima?valor=100`

**Resultado esperado:**
```json
[
  {
    "id": 3,
    "numeroPedido": "PEDIDO-003",
    "status": "ENTREGUE",
    "valorTotal": 395.00,
    ...
  }
]
```

**Validações:**
- ✅ Retorna apenas pedidos >= valor informado
- ✅ Valores de pedido estão corretos
- ✅ Pedidos com valor < 100 não aparecem

**Testes adicionais:**
- `?valor=50` → Deve retornar 2-3 pedidos
- `?valor=500` → Deve retornar 0 pedidos

---

### 3. Relatório por Período e Status

**Endpoint:** `GET /api/pedidos/relatorio/periodo-status`

**Parâmetros:**
- `dataInicial` = 2025-01-01T00:00:00
- `dataFinal` = 2025-12-31T23:59:59
- `status` = ENTREGUE

**Resultado esperado:**
```json
[
  {
    "id": 2,
    "numeroPedido": "PEDIDO-002",
    "status": "ENTREGUE",
    "dataPedido": "2025-11-06T14:30:00",
    "valorTotal": 282.00
  },
  {
    "id": 3,
    "numeroPedido": "PEDIDO-003",
    "status": "ENTREGUE",
    "dataPedido": "2025-11-06T15:45:00",
    "valorTotal": 395.00
  }
]
```

**Validações:**
- ✅ Filtra por período corretamente
- ✅ Filtra por status corretamente
- ✅ Retorna apenas ENTREGUE no exemplo

**Testes adicionais:**
- Testar com `status=PENDENTE`
- Testar com período mais curto
- Testar com período sem dados

---

### 4. Produtos Mais Vendidos

**Endpoint:** `GET /api/produtos/relatorio/mais-vendidos`

**Resultado esperado:**
```json
[
  {
    "produtoId": 1,
    "nomeProduto": "Pizza Margherita",
    "totalVendido": 3,
    "categoria": "Pizzas"
  },
  {
    "produtoId": 2,
    "nomeProduto": "Pizza Pepperoni",
    "totalVendido": 3,
    "categoria": "Pizzas"
  },
  {
    "produtoId": 5,
    "nomeProduto": "Temaki Salmão",
    "totalVendido": 3,
    "categoria": "Temakis"
  }
]
```

**Validações:**
- ✅ Ordena por quantidade vendida (DESC)
- ✅ Inclui corretamente quantidade de cada produto
- ✅ Produtos com 0 vendas não aparecem (ou aparecem com 0)

---

### 5. Ranking de Clientes por Número de Pedidos

**Endpoint:** `GET /api/clientes/relatorio/ranking-por-pedidos`

**Resultado esperado:**
```json
[
  {
    "clienteId": 1,
    "nomeCliente": "João Silva",
    "totalPedidos": 1
  },
  {
    "clienteId": 2,
    "nomeCliente": "Maria Santos",
    "totalPedidos": 1
  },
  {
    "clienteId": 3,
    "nomeCliente": "Pedro Oliveira",
    "totalPedidos": 1
  }
]
```

**Validações:**
- ✅ Todos os clientes com pedidos aparecem
- ✅ Contagem de pedidos está correta
- ✅ Ordenação é sensível

---

### 6. Faturamento por Categoria

**Endpoint:** `GET /api/produtos/relatorio/faturamento-por-categoria`

**Resultado esperado:**
```json
[
  {
    "categoria": "Pizzas",
    "totalFaturamento": 280.00
  },
  {
    "categoria": "Bebidas",
    "totalFaturamento": 99.00
  },
  {
    "categoria": "Combos",
    "totalFaturamento": 240.00
  },
  {
    "categoria": "Temakis",
    "totalFaturamento": 105.00
  }
]
```

**Validações:**
- ✅ Todas as categorias aparecem
- ✅ Valores de faturamento estão corretos
- ✅ Inclui apenas produtos vendidos

---

## 🐛 Troubleshooting

### Problema: "Connection refused" ao acessar API
**Solução:**
1. Verifique se a API está rodando: `curl -X GET "http://localhost:8080/health"`
2. Verifique se a porta 8080 está disponível: `lsof -i :8080`
3. Reinicie a aplicação

### Problema: "Email já está registrado"
**Solução:**
1. Use um email diferente nos testes
2. Limpe o banco de dados e reinicie a aplicação
3. Altere `spring.jpa.hibernate.ddl-auto=create-drop` no `application.properties`

### Problema: Validação de CPF/CNPJ não funciona
**Solução:**
1. Verifique se está usando CPF/CNPJ válido (library Hibernate Validator)
2. Use CPFs/CNPJs de teste fornecidos neste guia

### Problema: Relatórios retornam listas vazias
**Solução:**
1. Verifique se criou dados base conforme explicado
2. Verifique se copiou corretamente os IDs
3. Verifique o status dos pedidos (relatórios usam ENTREGUE por padrão)

### Problema: Erro 422 em "Unprocessable Entity"
**Solução:**
1. Verifique se todos os campos obrigatórios foram informados
2. Verifique o formato dos dados (tipos, tamanhos, padrões)
3. Consulte a seção "Testes de Validação" para ver quais campos são obrigatórios

---

## 📚 Referências Úteis

### Documentação da API
- **Base URL:** http://localhost:8080
- **Health Check:** GET /health
- **H2 Console:** http://localhost:8080/h2-console

### Recursos HTTP
- **POST:** Criar novo recurso
- **GET:** Recuperar recurso(s)
- **PUT:** Atualizar recurso (completo)
- **PATCH:** Atualizar recurso (parcial)
- **DELETE:** Remover recurso

### Status HTTP Esperados
- **201 Created:** Recurso criado com sucesso
- **200 OK:** Sucesso
- **400 Bad Request:** Erro de validação
- **422 Unprocessable Entity:** Erro de negócio
- **500 Internal Server Error:** Erro do servidor

---

## ✅ Checklist de Testes

- [ ] API rodando e respondendo a `/health`
- [ ] Testes de validação executados (devem falhar)
- [ ] 3 Clientes criados com sucesso
- [ ] 2 Restaurantes criados com sucesso
- [ ] 5+ Produtos criados com sucesso
- [ ] 3 Pedidos criados com sucesso
- [ ] Endpoint de vendas por restaurante retorna dados
- [ ] Endpoint de pedidos por valor retorna dados
- [ ] Endpoint de relatório por período funciona
- [ ] Endpoint de produtos mais vendidos retorna dados
- [ ] Endpoint de ranking de clientes funciona
- [ ] Endpoint de faturamento por categoria retorna dados

---

**Desenvolvido para Delivery Tech API v1.0.0**
