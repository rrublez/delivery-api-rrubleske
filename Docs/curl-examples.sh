#!/bin/bash

# ============================================
# Exemplos de Testes com cURL - Delivery API
# ============================================
# Este script fornece exemplos de como testar
# a API usando cURL diretamente do terminal
#
# Uso: bash curl-examples.sh
# ============================================

set -e

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

API_URL=\"http://localhost:8080\"

# ============================================
# FUNÇÕES AUXILIARES
# ============================================

print_header() {
    echo -e \"${BLUE}\\n========================================${NC}\"
    echo -e \"${BLUE}$1${NC}\"
    echo -e \"${BLUE}========================================${NC}\"
}

print_success() {
    echo -e \"${GREEN}✅ $1${NC}\"
}

print_error() {
    echo -e \"${RED}❌ $1${NC}\"
}

print_info() {
    echo -e \"${YELLOW}ℹ️  $1${NC}\"
}

# ============================================
# 0. VALIDAÇÃO DO AMBIENTE
# ============================================

print_header \"0. VALIDAÇÃO DO AMBIENTE\"

echo \"Verificando se a API está rodando...\"
if curl -s \"${API_URL}/health\" > /dev/null 2>&1; then
    print_success \"API está respondendo em ${API_URL}\"
else
    print_error \"API não está respondendo em ${API_URL}\"
    print_info \"Inicie a API com: ./mvnw spring-boot:run\"
    exit 1
fi

# ============================================
# 1. TESTES DE VALIDAÇÃO (Devem falhar)
# ============================================

print_header \"1. TESTES DE VALIDAÇÃO (Status 400/422)\"

print_info \"1.1 - Email inválido\"
curl -X POST \"${API_URL}/api/clientes\" \\
  -H \"Content-Type: application/json\" \\
  -d '{
    \"nome\": \"Teste Validação\",
    \"email\": \"email-invalido\",
    \"telefone\": \"11987654321\",
    \"cpf\": \"12345678901\",
    \"ativo\": true
  }' 2>/dev/null | jq '.'

print_info \"1.2 - CPF inválido (sequência)\"
curl -X POST \"${API_URL}/api/clientes\" \\
  -H \"Content-Type: application/json\" \\
  -d '{
    \"nome\": \"Teste CPF\",
    \"email\": \"testecpf@email.com\",
    \"telefone\": \"11987654321\",
    \"cpf\": \"00000000000\",
    \"ativo\": true
  }' 2>/dev/null | jq '.'

print_info \"1.3 - Preço zero\"
curl -X POST \"${API_URL}/api/produtos\" \\
  -H \"Content-Type: application/json\" \\
  -d '{
    \"nome\": \"Produto Preço Zero\",
    \"descricao\": \"Teste\",
    \"preco\": 0.00,
    \"disponivel\": true,
    \"categoria\": \"Teste\"
  }' 2>/dev/null | jq '.'

# ============================================
# 2. CRIAR DADOS BASE
# ============================================

print_header \"2. CRIAR DADOS BASE\"

# 2.1 - Criar Cliente 1
print_info \"2.1 - Criar Cliente 1 (João Silva)\"
RESPONSE=$(curl -s -X POST \"${API_URL}/api/clientes\" \\
  -H \"Content-Type: application/json\" \\
  -d '{
    \"nome\": \"João Silva\",
    \"email\": \"joao@email.com\",
    \"telefone\": \"11987654321\",
    \"cpf\": \"12345678901\",
    \"ativo\": true
  }')

CLIENTE1_ID=$(echo $RESPONSE | jq -r '.id')
print_success \"Cliente criado com ID: $CLIENTE1_ID\"
echo $RESPONSE | jq '.'

# 2.2 - Criar Cliente 2
print_info \"2.2 - Criar Cliente 2 (Maria Santos)\"
RESPONSE=$(curl -s -X POST \"${API_URL}/api/clientes\" \\
  -H \"Content-Type: application/json\" \\
  -d '{
    \"nome\": \"Maria Santos\",
    \"email\": \"maria@email.com\",
    \"telefone\": \"11912345678\",
    \"cpf\": \"98765432109\",
    \"ativo\": true
  }')

CLIENTE2_ID=$(echo $RESPONSE | jq -r '.id')
print_success \"Cliente criado com ID: $CLIENTE2_ID\"

# 2.3 - Criar Restaurante 1
print_info \"2.3 - Criar Restaurante 1 (Pizza Palace)\"
RESPONSE=$(curl -s -X POST \"${API_URL}/api/restaurantes\" \\
  -H \"Content-Type: application/json\" \\
  -d '{
    \"nome\": \"Pizza Palace\",
    \"endereco\": \"Avenida Paulista, 1000\",
    \"telefone\": \"1133334444\",
    \"cnpj\": \"11222333000181\",
    \"ramoAtividade\": \"Pizzaria\",
    \"ativo\": true,
    \"taxaEntrega\": 5.00
  }')

RESTAURANTE1_ID=$(echo $RESPONSE | jq -r '.id')
print_success \"Restaurante criado com ID: $RESTAURANTE1_ID\"

# 2.4 - Criar Produto 1
print_info \"2.4 - Criar Produto 1 (Pizza Margherita)\"
RESPONSE=$(curl -s -X POST \"${API_URL}/api/produtos\" \\
  -H \"Content-Type: application/json\" \\
  -d '{
    \"nome\": \"Pizza Margherita\",
    \"descricao\": \"Pizza clássica com tomate, mozzarela e manjericão\",
    \"preco\": 45.00,
    \"disponivel\": true,
    \"categoria\": \"Pizzas\"
  }')

PRODUTO1_ID=$(echo $RESPONSE | jq -r '.id')
print_success \"Produto criado com ID: $PRODUTO1_ID\"

# 2.5 - Criar Produto 2
print_info \"2.5 - Criar Produto 2 (Refrigerante)\"
RESPONSE=$(curl -s -X POST \"${API_URL}/api/produtos\" \\
  -H \"Content-Type: application/json\" \\
  -d '{
    \"nome\": \"Refrigerante 2L\",
    \"descricao\": \"Refrigerante gelado de 2 litros\",
    \"preco\": 8.00,
    \"disponivel\": true,
    \"categoria\": \"Bebidas\"
  }')

PRODUTO2_ID=$(echo $RESPONSE | jq -r '.id')
print_success \"Produto criado com ID: $PRODUTO2_ID\"

# ============================================
# 3. CRIAR PEDIDO
# ============================================

print_header \"3. CRIAR PEDIDO\"

print_info \"Criar Pedido 1 (Cliente: $CLIENTE1_ID, Restaurante: $RESTAURANTE1_ID)\"
RESPONSE=$(curl -s -X POST \"${API_URL}/api/pedidos\" \\
  -H \"Content-Type: application/json\" \\
  -d \"{
    \\\"numeroPedido\\\": \\\"PEDIDO-001\\\",
    \\\"status\\\": \\\"PENDENTE\\\",
    \\\"clienteId\\\": $CLIENTE1_ID,
    \\\"restauranteId\\\": $RESTAURANTE1_ID,
    \\\"itens\\\": [
      {
        \\\"produtoId\\\": $PRODUTO1_ID,
        \\\"quantidade\\\": 1,
        \\\"precoUnitario\\\": 45.00
      },
      {
        \\\"produtoId\\\": $PRODUTO2_ID,
        \\\"quantidade\\\": 2,
        \\\"precoUnitario\\\": 8.00
      }
    ]
  }\")

PEDIDO1_ID=$(echo $RESPONSE | jq -r '.id')
print_success \"Pedido criado com ID: $PEDIDO1_ID\"
echo $RESPONSE | jq '.'

# ============================================
# 4. CONSULTAS SIMPLES
# ============================================

print_header \"4. CONSULTAS SIMPLES\"

print_info \"4.1 - Buscar cliente por email\"
curl -s -X GET \"${API_URL}/api/clientes/email/joao@email.com\" | jq '.'

print_info \"4.2 - Listar restaurantes por ramo\"
curl -s -X GET \"${API_URL}/api/restaurantes/ramo/Pizzaria\" | jq '.'

print_info \"4.3 - Listar produtos por categoria\"
curl -s -X GET \"${API_URL}/api/produtos/categoria/Pizzas\" | jq '.'

print_info \"4.4 - Listar pedidos por status\"
curl -s -X GET \"${API_URL}/api/pedidos/status/PENDENTE\" | jq '.'

print_info \"4.5 - Listar pedidos do cliente\"
curl -s -X GET \"${API_URL}/api/pedidos/cliente/$CLIENTE1_ID\" | jq '.'

# ============================================
# 5. RELATÓRIOS
# ============================================

print_header \"5. RELATÓRIOS\"

print_info \"5.1 - Vendas por Restaurante\"
curl -s -X GET \"${API_URL}/api/pedidos/relatorio/vendas-por-restaurante\" | jq '.'

print_info \"5.2 - Pedidos com valor acima de 50\"
curl -s -X GET \"${API_URL}/api/pedidos/relatorio/valor-acima?valor=50\" | jq '.'

print_info \"5.3 - Produtos Mais Vendidos\"
curl -s -X GET \"${API_URL}/api/produtos/relatorio/mais-vendidos\" | jq '.'

print_info \"5.4 - Ranking de Clientes\"
curl -s -X GET \"${API_URL}/api/clientes/relatorio/ranking-por-pedidos\" | jq '.'

print_info \"5.5 - Faturamento por Categoria\"
curl -s -X GET \"${API_URL}/api/produtos/relatorio/faturamento-por-categoria\" | jq '.'

print_info \"5.6 - Relatório por Período e Status\"
curl -s -X GET \\
  \"${API_URL}/api/pedidos/relatorio/periodo-status?dataInicial=2025-01-01T00:00:00&dataFinal=2025-12-31T23:59:59&status=PENDENTE\" \\
  | jq '.'

# ============================================
# RESUMO
# ============================================

print_header \"RESUMO DOS TESTES\"

print_success \"Todos os testes foram executados!\"
print_info \"IDs salvos:\"
echo \"  Cliente 1: $CLIENTE1_ID\"
echo \"  Cliente 2: $CLIENTE2_ID\"
echo \"  Restaurante 1: $RESTAURANTE1_ID\"
echo \"  Produto 1: $PRODUTO1_ID\"
echo \"  Produto 2: $PRODUTO2_ID\"
echo \"  Pedido 1: $PEDIDO1_ID\"

# ============================================
# EXEMPLOS ADICIONAIS
# ============================================

print_header \"EXEMPLOS ADICIONAIS\"

print_info \"Para adicionar mais clientes, use:\"
echo 'curl -X POST \"http://localhost:8080/api/clientes\" \\'
echo '  -H \"Content-Type: application/json\" \\'
echo '  -d \"{JSON_DO_CLIENTE}\"'

print_info \"Para filtrar com grep:\"
echo 'curl -s http://localhost:8080/api/clientes/email/joao@email.com | jq \".[] | .nome\"'

print_info \"Para contar resultados:\"
echo 'curl -s http://localhost:8080/api/produtos/relatorio/mais-vendidos | jq \". | length\"'

echo \"\"
