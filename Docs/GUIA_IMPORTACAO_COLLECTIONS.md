# 📚 Guia de Importação das Collections

Documento de instruções para importar as collections de teste nos clientes HTTP.

---

## 📥 Importação no Postman

### Passo 1: Abrir o Postman
- Acesse [postman.com](https://www.postman.com) ou abra a aplicação instalada

### Passo 2: Importar Collection
1. Clique no botão **"Import"** (canto superior esquerdo)
2. Selecione a aba **"File"**
3. Clique em **"Upload Files"**
4. Navegue até o arquivo `delivery-api-postman.json`
5. Clique em **"Open"**

### Passo 3: Confirmar Importação
- O Postman exibirá um resumo da collection
- Clique em **"Import"** para confirmar

### Resultado
A collection aparecerá na lateral esquerda organizada em grupos:
- ✅ 0 - Setup e Validação
- ✅ 1 - Testes de Validação
- ✅ 2 - Criação de Dados Base
- ✅ 3 - Consultas Simples
- ✅ 4 - Relatórios e Endpoints Complexos

---

## 📥 Importação no Bruno

### Passo 1: Abrir o Bruno
- Acesse [usebruno.com](https://www.usebruno.com) ou baixe para desktop
- Bruno é um cliente HTTP leve e open-source

### Passo 2: Importar Collection
1. Clique no menu principal
2. Selecione **"File"** → **"Import Collection"**
3. Navegue até o arquivo `delivery-api-bruno.bru`
4. Clique em **"Open"**

### Passo 3: Confirmar Importação
- Bruno carregará a collection
- Os testes aparecerão organizados na interface

### Resultado
A collection estará disponível com todos os endpoints organizados

---

## 🔧 Configurar Variável de Ambiente

### No Postman

1. Clique no botão de **Engrenagem** (⚙️) no canto superior direito
2. Selecione **"Environments"**
3. Clique em **"Create New"**
4. Nome: `Delivery API Local`
5. Adicione a variável:
   - **Key:** `baseUrl`
   - **Initial Value:** `http://localhost:8080`
   - **Current Value:** `http://localhost:8080`
6. Clique em **"Save"**

### Selecionar Ambiente
- No canto superior direito, ao lado de "No Environment"
- Clique e selecione **"Delivery API Local"**

### No Bruno

Bruno usa variáveis de ambiente de forma diferente. As URLs já estão configuradas como `{{baseUrl}}` nos testes:

1. Abra o arquivo `delivery-api-bruno.bru`
2. No topo, há uma linha: `var baseUrl = http://localhost:8080`
3. Edite se necessário para outro ambiente

---

## ✅ Como Executar os Testes

### Fluxo Recomendado

#### 1️⃣ Setup (5 minutos)
1. Execute: **"0.1 - Health Check"**
2. Esperado: Status 200 (API respondendo)

#### 2️⃣ Validações (15 minutos)
1. Execute todos os testes do grupo **"1 - Testes de Validação"**
2. **Esperado:** Status 400/422 em todos (devem falhar com mensagens apropriadas)
3. Use o console (abaixo em Postman) para ver erros

#### 3️⃣ Criar Dados (10 minutos)
Execute na ordem:
1. **2.1 - CLIENTES:** Criar 3 clientes (João, Maria, Pedro)
2. **2.2 - RESTAURANTES:** Criar 2 restaurantes (Pizza Palace, Sushi House)
3. **2.3 - PRODUTOS:** Criar 6 produtos (variados)
4. **2.4 - PEDIDOS:** Criar 3 pedidos (variados)

⚠️ **Importante:** Copiar os IDs retornados após cada criação (irá automaticamente em Postman)

#### 4️⃣ Consultas Simples (10 minutos)
Execute todos os testes do grupo **"3 - Consultas Simples"**

**Esperado:** 
- Status 200
- Dados retornados
- Verifica se dados foram salvos corretamente

#### 5️⃣ Relatórios (15 minutos)
Execute todos os testes do grupo **"4 - Relatórios e Endpoints Complexos"**

**Esperado:**
- Total de vendas por restaurante: 2 restaurantes com valores
- Pedidos valor > 100: 1 pedido
- Pedidos valor > 50: 2-3 pedidos
- Produtos mais vendidos: Lista com quantidades
- Ranking clientes: 3 clientes
- Faturamento por categoria: 4 categorias

---

## 🎯 Checklist de Execução

- [ ] API rodando: `curl -X GET "http://localhost:8080/health"`
- [ ] Health check retorna 200
- [ ] Todos os testes de validação retornam erro (esperado)
- [ ] 3 Clientes criados com sucesso
- [ ] 2 Restaurantes criados com sucesso
- [ ] 6 Produtos criados com sucesso
- [ ] 3 Pedidos criados com sucesso
- [ ] Todas as consultas simples retornam dados
- [ ] Todos os relatórios retornam dados válidos

---

## 🐛 Troubleshooting

### Problema: "Cannot read property 'json' of undefined"
**Solução:** 
- A resposta não é JSON válido
- Verifique se o endpoint está correto
- Verifique a URL base

### Problema: "Failed to write variable"
**Solução:**
- Apenas em Postman com a automação de scripts
- Ignorar aviso - variáveis podem ser preenchidas manualmente

### Problema: IDs aparecem como vazio (null)
**Solução:**
- Crie os dados base manualmente primeiro
- Ou copie os IDs retornados para as próximas requisições

### Problema: Email/CPF/CNPJ duplicado
**Solução:**
- Limpar o banco de dados
- Mudar valores nos testes para algo único
- Ou recriar nova instância da aplicação

### Problema: "localhost refused"
**Solução:**
- Verifique se a API está rodando
- Teste: `curl -X GET "http://localhost:8080/health"`
- Reinicie a aplicação

---

## 📊 Variáveis Automáticas (Postman)

Após criar cada recurso, o Postman automaticamente salva os IDs em variáveis:

```
cliente1_id    = ID do João
cliente2_id    = ID da Maria
cliente3_id    = ID do Pedro
restaurante1_id = ID Pizza Palace
restaurante2_id = ID Sushi House
produto1_id     = ID Pizza Margherita
produto2_id     = ID Pizza Pepperoni
produto3_id     = ID Refrigerante
produto4_id     = ID Combo Sushi
produto5_id     = ID Temaki Salmão
produto6_id     = ID Sakê
pedido1_id      = ID Pedido 1
pedido2_id      = ID Pedido 2
pedido3_id      = ID Pedido 3
```

Estas variáveis são usadas automaticamente nos testes subsequentes.

---

## 💡 Dicas Úteis

### Postman
- Use **Pre-request Script** para configurar dados antes
- Use **Tests** para validar respostas
- Use **Environments** para múltiplos ambientes (dev, prod)
- Exporte resultados com **Run Collection**

### Bruno
- Arquivo `.bru` é em formato de texto
- Fácil de versioná-lo em Git
- Sincronização automática com repositório
- Suporta variáveis locais

### Ambos
- Salve as collections no Git para controle de versão
- Use em CI/CD para testes automatizados
- Importe em equipe para executar testes consistentes

---

## 📞 Suporte

Se encontrar problemas:

1. **Verifique o Guia de Testes Manuais:** `GUIA_TESTES_MANUAIS.md`
2. **Logs da API:** `logs/app.log`
3. **H2 Console:** http://localhost:8080/h2-console (user: sa, sem senha)
4. **Abra issue no repositório:** Com detalhes do erro

---

**Versão:** 1.0 | **Atualizado:** Novembro 2025 | **API:** Delivery Tech v1.0.0
