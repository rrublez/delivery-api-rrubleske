# 🚀 Delivery Tech API

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.11-brightgreen)
![Coverage](https://img.shields.io/badge/JaCoCo-80%25+-orange)

API REST de delivery desenvolvida com **Spring Boot 3.4.11** e **Java 21**, testes automatizados (JUnit 5, Mockito, Cucumber) e deploy-ready para Docker/Kubernetes.

---

## 🧱 Tecnologias

| Camada | Stack |
|--------|-------|
| **Backend** | Spring Boot 3.4.11, Spring MVC, Spring Security (JWT), Spring Data JPA |
| **Banco** | H2 (dev/test), PostgreSQL (prod) |
| **Testes** | JUnit 5, Mockito, Cucumber, JaCoCo |
| **Infra** | Docker, Kubernetes, Logback |
| **Docs** | springdoc-openapi (Swagger UI) |

---

## ▶️ Executar

```bash
# Compilar e rodar
mvn clean package
java -jar target/delivery-api-1.0.0.jar

# Ou via Maven wrapper
./mvnw spring-boot:run
```

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html  
- **Actuator**: http://localhost:8080/actuator/health  
- **H2 Console**: http://localhost:8080/h2-console (user: `sa`, senha vazia)

---

## ⚙️ Testes e Cobertura

```bash
mvn test
```

### Acessar relatório de cobertura

Após executar os testes, abra no navegador:

```
target/site/jacoco/index.html
```

O JaCoCo valida mínimos de **80% linhas** e **70% branches** (configurado no `pom.xml`).

---

## ☁️ Deploy (Docker + Kubernetes)

### Docker

```bash
# Build da imagem (usando GitHub Container Registry)
mvn clean package -DskipTests
docker build -t ghcr.io/rrublez/delivery-api-rrubleske:latest .
docker push ghcr.io/rrublez/delivery-api-rrubleske:latest
```

### Docker Compose (ambiente local com Postgres)

```bash
docker compose up --build
```

O `docker-compose.yml` sobe Postgres 16 + aplicação na mesma rede, com variáveis de ambiente para conexão.

### Kubernetes (Local com Minikube)

#### Pré-requisitos

Instale o [Minikube](https://minikube.sigs.k8s.io/docs/start/) e inicie o cluster:

```bash
minikube start --memory=4096 --cpus=2
```

#### Build e deploy local

```bash
# Usar o Docker daemon do Minikube para build local
eval $(minikube docker-env)

# Build da imagem localmente
docker build -t ghcr.io/rrublez/delivery-api-rrubleske:latest .

# Aplicar os manifests (na ordem correta)
kubectl apply -f k8s/delivery-api-secret.yaml
kubectl apply -f k8s/delivery-api-configmap.yaml
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/postgres-service.yaml
kubectl apply -f k8s/delivery-api-deployment.yaml
kubectl apply -f k8s/delivery-api-service.yaml

# Aguardar pods ficarem prontos
kubectl wait --for=condition=ready pod -l app=postgres --timeout=120s
kubectl wait --for=condition=ready pod -l app=delivery-api --timeout=180s
```

#### Acessar a aplicação

```bash
# Expor o serviço localmente
minikube service delivery-api-service --url
```

Ou via port-forward:

```bash
kubectl port-forward svc/delivery-api-service 8080:80
```

Acesse: http://localhost:8080/swagger-ui/index.html

#### Comandos úteis

```bash
# Ver status dos pods
kubectl get pods

# Ver logs da aplicação
kubectl logs -l app=delivery-api -f

# Ver logs do PostgreSQL
kubectl logs -l app=postgres -f

# Limpar recursos
kubectl delete -f k8s/
```

- **Deployment**: 2 réplicas, probes em `/actuator/health`, limites de recursos
- **Service**: LoadBalancer na porta 80 → 8080

---

## 📐 Diagramas

### Arquitetura Geral

```mermaid
flowchart LR
    Cliente([Cliente HTTP])
    
    Cliente --> Controllers
    Controllers --> JwtFilter
    JwtFilter --> AuthService
    AuthService --> UserDetails[UserDetailsService]
    
    Controllers --> Services
    Services --> Repositories
    Repositories --> DB[(H2 / PostgreSQL)]
    
    Controllers --> Swagger[Swagger UI]
    Controllers --> Actuator[Actuator]
```

### Fluxo de Requisição

```mermaid
sequenceDiagram
    participant C as Cliente
    participant Ctrl as Controller
    participant Svc as Service
    participant Repo as Repository
    participant DB as Banco

    C->>Ctrl: Request HTTP
    Ctrl->>Svc: Processar
    Svc->>Repo: Query/Persist
    Repo->>DB: SQL
    DB-->>Repo: Resultado
    Repo-->>Svc: Entidade
    Svc-->>Ctrl: DTO
    Ctrl-->>C: Response JSON
```

### Fluxo de Pedido

```mermaid
sequenceDiagram
    participant Cliente
    participant PedidoController
    participant PedidoService
    participant PedidoRepository
    participant Banco

    Cliente->>PedidoController: POST /api/pedidos
    PedidoController->>PedidoService: Validar e salvar
    PedidoService->>PedidoRepository: Persistir
    PedidoRepository->>Banco: INSERT
    Banco-->>PedidoRepository: OK
    PedidoRepository-->>PedidoService: Pedido salvo
    PedidoService-->>PedidoController: PedidoResponse
    PedidoController-->>Cliente: 201 Created
```

### Diagrama de Classes

```mermaid
classDiagram
    class ClienteController {
        +listar() ResponseEntity
        +criar() ResponseEntity
        +atualizar() ResponseEntity
    }
    
    class ClienteService {
        +listarTodos() List
        +criar() ClienteResponse
        +atualizar() ClienteResponse
    }
    
    class PedidoController {
        +listar() ResponseEntity
        +criar() ResponseEntity
        +atualizarStatus() ResponseEntity
    }
    
    class PedidoService {
        +listar() List
        +criar() PedidoResponse
        +atualizarStatus() void
    }
    
    ClienteController --> ClienteService
    PedidoController --> PedidoService
    ClienteService --> ClienteRepository
    PedidoService --> PedidoRepository
```

### Entidades

```mermaid
erDiagram
    CLIENTE ||--o{ PEDIDO : faz
    RESTAURANTE ||--o{ PEDIDO : recebe
    RESTAURANTE ||--o{ PRODUTO : oferece
    PEDIDO ||--|{ ITEM_PEDIDO : contem
    PRODUTO ||--o{ ITEM_PEDIDO : compoe
    USUARIO ||--|| CLIENTE : autentica
```

---

## 🔧 Configuração

| Variável | Valor Default | Descrição |
|----------|---------------|-----------|
| `SERVER_PORT` | 8080 | Porta da aplicação |
| `SPRING_DATASOURCE_URL` | jdbc:h2:mem:delivery | URL do banco |
| `SPRING_PROFILES_ACTIVE` | default | Profile ativo |

As configurações estão em `src/main/resources/application.yml`. Para produção, use os manifestos em `k8s/` ou variáveis do `docker-compose.yml`.

---

## 👨‍💻 Desenvolvedor

**Rafael Rubleske**  
Análise e Desenvolvimento de Sistemas - UniRitter  
📧 rubleske@gmail.com
