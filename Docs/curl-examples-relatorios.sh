#!/bin/bash

# Exemplos de cURL para testar os endpoints de Relatórios
# Use este script para testar os endpoints: bash curl-examples-relatorios.sh

BASE_URL="http://localhost:8080/api"

echo "================================"
echo "TESTES DE ENDPOINTS DE RELATÓRIOS"
echo "================================"
echo ""

# 1. Vendas por Restaurante
echo "1️⃣  GET - Vendas por Restaurante"
echo "Endpoint: $BASE_URL/relatorios/vendas-por-restaurante"
curl -X GET "$BASE_URL/relatorios/vendas-por-restaurante" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 2. Produtos Mais Vendidos
echo "2️⃣  GET - Produtos Mais Vendidos"
echo "Endpoint: $BASE_URL/relatorios/produtos-mais-vendidos"
curl -X GET "$BASE_URL/relatorios/produtos-mais-vendidos" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 3. Clientes Ativos
echo "3️⃣  GET - Clientes Ativos"
echo "Endpoint: $BASE_URL/relatorios/clientes-ativos"
curl -X GET "$BASE_URL/relatorios/clientes-ativos" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 4. Pedidos por Período (sem status)
echo "4️⃣  GET - Pedidos por Período (sem filtro de status)"
echo "Endpoint: $BASE_URL/relatorios/pedidos-por-periodo?dataInicial=2025-01-01T00:00:00&dataFinal=2025-12-31T23:59:59"
curl -X GET "$BASE_URL/relatorios/pedidos-por-periodo?dataInicial=2025-01-01T00:00:00&dataFinal=2025-12-31T23:59:59" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 5. Pedidos por Período (com status)
echo "5️⃣  GET - Pedidos por Período (com filtro de status)"
echo "Endpoint: $BASE_URL/relatorios/pedidos-por-periodo?dataInicial=2025-01-01T00:00:00&dataFinal=2025-12-31T23:59:59&status=ENTREGUE"
curl -X GET "$BASE_URL/relatorios/pedidos-por-periodo?dataInicial=2025-01-01T00:00:00&dataFinal=2025-12-31T23:59:59&status=ENTREGUE" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

# 6. Pedidos por Período (data inválida - erro esperado)
echo "6️⃣  GET - Pedidos por Período (data inválida - esperado 400)"
echo "Endpoint: $BASE_URL/relatorios/pedidos-por-periodo?dataInicial=2025-12-31T23:59:59&dataFinal=2025-01-01T00:00:00"
curl -X GET "$BASE_URL/relatorios/pedidos-por-periodo?dataInicial=2025-12-31T23:59:59&dataFinal=2025-01-01T00:00:00" \
  -H "Content-Type: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo "================================"
echo "TESTES CONCLUÍDOS"
echo "================================"
