# Crediário API

API REST para sistema de crediário parcelado, desenvolvida com Spring Boot como parte de um projeto de estudos em desenvolvimento de software profissional.

---

## Fases do projeto

| Fase | Descrição | Repositório |
|------|-----------|-------------|
| Fase 1 | Java puro no terminal, dados em memória | [Crediario](https://github.com/Wilsouuza/Crediario) |
| Fase 2 | Persistência com PostgreSQL via JDBC | [Crediario](https://github.com/Wilsouuza/Crediario) |
| Fase 3 | API REST com Spring Boot, JPA e Spring Security | [crediario-api](https://github.com/Wilsouuza/crediario-api) |

---

## Tecnologias utilizadas

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Data JPA**
- **Spring Security**
- **JWT (jjwt 0.12.6)**
- **PostgreSQL 17**
- **Lombok**
- **Bean Validation**
- **SpringDoc OpenAPI (Swagger)**
- **Maven**

---

## Funcionalidades

### Perfis de usuário
- **Admin** — gerencia usuários e configurações do sistema
- **Caixa** — cadastra clientes, registra compras e recebe pagamentos
- **Vendedor** — cadastra clientes
- **Cliente** — visualiza suas parcelas e histórico

### Regras de negócio
- Limite de crédito padrão configurável
- Cliente com parcela atrasada não pode realizar nova compra
- Valor mínimo de compra configurável
- Máximo de parcelas configurável
- Cálculo automático de multa (2%) e juros (0,1% ao dia) para parcelas em atraso
- Comissão de R$4,00 por cliente cadastrado
- Bônus de R$10,00 se o cliente fizer a primeira compra em até 24 horas do cadastro

---

## Arquitetura

```
src/main/java/com/crediario/crediario_api/
├── business/
│   ├── config/         → SystemConfig, SwaggerConfig, JwtService, JwtFilter, SecurityConfig, DataInitializer
│   ├── dto/            → Records de request e response por entidade
│   ├── entity/         → Entidades JPA
│   ├── enums/          → InstallmentStatus, PaymentMethod, UserType
│   ├── exception/      → BusinessException, GlobalExceptionHandler
│   ├── mapper/         → Conversão entre entidades e DTOs
│   └── service/        → Regras de negócio
├── controller/         → Endpoints REST
└── infrastructure/
    └── repository/     → Interfaces Spring Data JPA
```

---

## Autenticação

A API usa **JWT (JSON Web Token)** para autenticação. O fluxo é:

```
1. POST /users/login com login e senha
2. API retorna um token JWT
3. Incluir o token no header de cada requisição:
   Authorization: Bearer {token}
```

### Permissões por perfil

| Endpoint | Método | Perfil necessário |
|----------|--------|-------------------|
| `/users/login` | POST | Público |
| `/swagger-ui/**` | GET | Público |
| `/users` | POST | ADMIN |
| `/users/{id}` | DELETE | ADMIN |
| `/customers` | POST | CASHIER, SELLER |
| `/purchases` | POST | CASHIER |
| `/payments` | POST | CASHIER |
| Demais endpoints | * | Autenticado |

---

## Endpoints

### Users `/users`
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/users/login` | Realiza login e retorna token JWT |
| GET | `/users` | Lista todos os usuários |
| GET | `/users/{userType}` | Lista usuários por tipo |
| POST | `/users` | Cria novo usuário |
| PUT | `/users/password` | Altera senha |
| DELETE | `/users/{id}` | Remove usuário |

### Customers `/customers`
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/customers` | Lista todos os clientes |
| GET | `/customers/{id}` | Busca cliente por id |
| GET | `/customers/cpf/{cpf}` | Busca cliente por CPF |
| GET | `/customers/{id}/balance` | Consulta limite disponível |
| POST | `/customers` | Cadastra novo cliente |

### Purchases `/purchases`
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/purchases` | Lista todas as compras |
| GET | `/purchases/{id}` | Busca compra por id |
| GET | `/purchases/customer/{customerId}` | Lista compras de um cliente |
| POST | `/purchases` | Registra nova compra |

### Installments `/installments`
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/installments/purchase/{purchaseId}` | Lista parcelas de uma compra |
| GET | `/installments/customer/{cpf}` | Lista parcelas de um cliente |
| PUT | `/installments/update-overdue` | Atualiza parcelas vencidas |

### Payments `/payments`
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/payments` | Lista todos os pagamentos |
| GET | `/payments/customer/{cpf}` | Lista pagamentos de um cliente |
| GET | `/payments/purchase/{purchaseId}` | Lista pagamentos de uma compra |
| GET | `/payments/date-range` | Lista pagamentos por período |
| POST | `/payments` | Registra novo pagamento |

---

## Como executar

### Pré-requisitos
- Java 21
- PostgreSQL 17
- Maven

### 1. Clone o repositório
```bash
git clone https://github.com/Wilsouuza/crediario-api.git
cd crediario-api
```

### 2. Configure o banco de dados
Crie o arquivo `src/main/resources/application.properties` baseado no exemplo abaixo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/crediario
spring.datasource.username=postgres
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

spring.application.name=crediario-api

jwt.secret=sua-chave-secreta-com-pelo-menos-32-caracteres
jwt.expiration=86400000
```

> ⚠️ O arquivo `application.properties` está no `.gitignore` e não deve ser versionado.

### 3. Execute o projeto
```bash
./mvnw spring-boot:run
```

### 4. Usuário padrão
Na primeira execução, o sistema cria automaticamente um usuário administrador:
- **Login:** `admin`
- **Senha:** `admin123`

### 5. Acesse a documentação
```
http://localhost:8080/swagger-ui/index.html
```

---

## Testando os endpoints

O projeto inclui arquivos `.http` na pasta `requests/` para testar os endpoints diretamente pelo IntelliJ HTTP Client:

```
requests/
├── users.http
├── customers.http
├── purchases.http
├── installments.http
└── payments.http
```

---

## Configurações do sistema

| Configuração | Valor padrão |
|---|---|
| Taxa de juros diária | 0,1% |
| Multa por atraso | 2% |
| Máximo de parcelas | 6 |
| Valor mínimo de compra | R$ 10,00 |
| Limite de crédito padrão | R$ 500,00 |

---

## Autor

Wilson — estudante de Análise e Desenvolvimento de Sistemas, 3º semestre.

Técnico em Informática pelo IFBA.

[GitHub](https://github.com/Wilsouuza)
