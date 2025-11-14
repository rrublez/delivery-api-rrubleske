#!/bin/bash
# 🍕 Exemplos de cURL para Endpoints de Produtos
# Ciclo Completo: CRUD + Filtros + Relatórios

# Configuração
BASE_URL="http://localhost:8080"
CONTENT_TYPE="Content-Type: application/json"

echo "╔═══════════════════════════════════════════════════════════════╗"
echo "║     🍕 EXEMPLOS DE CURL - ENDPOINTS DE PRODUTOS              ║"
echo "╚═══════════════════════════════════════════════════════════════╝"
echo ""

# ============================================================================
# 1. CRIAR PRODUTO
# ============================================================================
echo "📌 1. POST /api/produtos - Criar Produto"
echo "─────────────────────────────────────────────────────────────────"
curl -X POST "${BASE_URL}/api/produtos" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "nome": "Pizza Margherita",
    "descricao": "Pizza clássica com tomate, mozzarela e manjericão",
    "preco": 45.00,
    "disponivel": true,
    "categoria": "Pizzas"
  }' | jq '.'

echo -e "\n✓ Resposta esperada: 201 Created\n"

# ============================================================================
# 2. BUSCAR PRODUTO POR ID
# ============================================================================
echo "📌 2. GET /api/produtos/{id} - Buscar por ID"
echo "─────────────────────────────────────────────────────────────────"
curl -X GET "${BASE_URL}/api/produtos/1" | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK\n"

# ============================================================================
# 3. ATUALIZAR PRODUTO COMPLETO
# ============================================================================
echo "📌 3. PUT /api/produtos/{id} - Atualizar Produto"
echo "─────────────────────────────────────────────────────────────────"
curl -X PUT "${BASE_URL}/api/produtos/1" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "nome": "Pizza Margherita Premium",
    "descricao": "Pizza italiana premium com ingredientes importados",
    "preco": 55.00,
    "disponivel": true,
    "categoria": "Pizzas Premium"
  }' | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK\n"

# ============================================================================
# 4. TOGGLE DISPONIBILIDADE (PATCH)
# ============================================================================
echo "📌 4. PATCH /api/produtos/{id}/disponibilidade - Toggle"
echo "─────────────────────────────────────────────────────────────────"
curl -X PATCH "${BASE_URL}/api/produtos/1/disponibilidade" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "disponivel": false
  }' | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK (disponivel: false)\n"

# ============================================================================
# 5. REATIVAR DISPONIBILIDADE
# ============================================================================
echo "📌 5. PATCH /api/produtos/{id}/disponibilidade - Reativar"
echo "─────────────────────────────────────────────────────────────────"
curl -X PATCH "${BASE_URL}/api/produtos/1/disponibilidade" \
  -H "${CONTENT_TYPE}" \
  -d '{
    "disponivel": true
  }' | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK (disponivel: true)\n"

# ============================================================================
# 6. BUSCAR PRODUTOS POR RESTAURANTE
# ============================================================================
echo "📌 6. GET /api/restaurantes/{id}/produtos - Por Restaurante"
echo "─────────────────────────────────────────────────────────────────"
curl -X GET "${BASE_URL}/api/restaurantes/1/produtos" | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK com lista de produtos\n"

# ============================================================================
# 7. BUSCAR PRODUTOS POR CATEGORIA
# ============================================================================
echo "📌 7. GET /api/produtos/categoria/{categoria} - Por Categoria"
echo "─────────────────────────────────────────────────────────────────"
curl -X GET "${BASE_URL}/api/produtos/categoria/Pizzas" | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK com lista de produtos\n"

# ============================================================================
# 8. BUSCAR PRODUTOS POR NOME
# ============================================================================
echo "📌 8. GET /api/produtos/buscar?nome={nome} - Busca por Nome"
echo "─────────────────────────────────────────────────────────────────"
curl -X GET "${BASE_URL}/api/produtos/buscar?nome=Margherita" | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK com lista de produtos (case-insensitive)\n"

# ============================================================================
# 9. LISTAR PRODUTOS DISPONÍVEIS
# ============================================================================
echo "📌 9. GET /api/produtos/disponivel - Apenas Disponíveis"
echo "─────────────────────────────────────────────────────────────────"
curl -X GET "${BASE_URL}/api/produtos/disponivel" | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK com lista de produtos\n"

# ============================================================================
# 10. FILTRAR POR PREÇO MÁXIMO
# ============================================================================
echo "📌 10. GET /api/produtos/preco-maximo?preco=X - Filtrar Preço"
echo "─────────────────────────────────────────────────────────────────"
curl -X GET "${BASE_URL}/api/produtos/preco-maximo?preco=50.00" | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK com lista de produtos <= 50.00\n"

# ============================================================================
# 11. RELATÓRIO - PRODUTOS MAIS VENDIDOS
# ============================================================================
echo "📌 11. GET /api/produtos/relatorio/mais-vendidos - Top Vendidos"
echo "─────────────────────────────────────────────────────────────────"
curl -X GET "${BASE_URL}/api/produtos/relatorio/mais-vendidos" | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK com agregação de vendas\n"

# ============================================================================
# 12. RELATÓRIO - FATURAMENTO POR CATEGORIA
# ============================================================================
echo "📌 12. GET /api/produtos/relatorio/faturamento-por-categoria"
echo "─────────────────────────────────────────────────────────────────"
curl -X GET "${BASE_URL}/api/produtos/relatorio/faturamento-por-categoria" | jq '.'

echo -e "\n✓ Resposta esperada: 200 OK com agregação por categoria\n"

# ============================================================================
# 13. DELETAR PRODUTO
# ============================================================================
echo "📌 13. DELETE /api/produtos/{id} - Remover Produto"
echo "─────────────────────────────────────────────────────────────────"
curl -X DELETE "${BASE_URL}/api/produtos/1" -v

echo -e "\n✓ Resposta esperada: 204 No Content\n"

# ============================================================================
# RESUMO DE VALIDAÇÕES
# ============================================================================
echo "╔═══════════════════════════════════════════════════════════════╗"
echo "║          📋 VALIDAÇÕES E RESTRIÇÕES                          ║"
echo "╠═══════════════════════════════════════════════════════════════╣"
echo "║                                                               ║"
echo "║  Nome:            3-100 caracteres (obrigatório)             ║"
echo "║  Descrição:       5-255 caracteres (obrigatório)             ║"
echo "║  Preço:           > 0.00 (obrigatório)                       ║"
echo "║  Categoria:       3-20 caracteres (obrigatório)              ║"
echo "║  Disponível:      true/false (obrigatório)                   ║"
echo "║                                                               ║"
echo "║  Erros Esperados:                                            ║"
echo "║  • 400 Bad Request    - Validação falhou                     ║"
echo "║  • 404 Not Found      - Produto não existe                   ║"
echo "║  • 201 Created        - POST com sucesso                     ║"
echo "║  • 200 OK             - GET/PUT/PATCH com sucesso            ║"
echo "║  • 204 No Content     - DELETE com sucesso                   ║"
echo "║                                                               ║"
echo "╚═══════════════════════════════════════════════════════════════╝"
