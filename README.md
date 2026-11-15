# Crediário API

API REST para sistema de crediário parcelado, desenvolvida com Spring Boot como parte de um projeto de estudos em desenvolvimento de software profissional.

---

## Fases do projeto

| Fase | Descrição | Repositório |
|------|-----------|-------------|
| Fase 1 | Java puro no terminal, dados em memória | [Crediario](https://github.com/Wilsouuza/Crediario) |
| Fase 2 | Persistência com PostgreSQL via JDBC | [Crediario](https://github.com/Wilsouuza/Crediario) |
| Fase 3 | API REST com Spring Boot, JPA, Spring Security e JWT | [crediario-api](https://github.com/Wilsouuza/crediario-api) |

---

## Tecnologias utilizadas

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Data JPA**
- **Spring Security** com senhas protegidas por **BCrypt**
- **JWT (jjwt 0.12.6)**
- **PostgreSQL 17** (via **Docker Compose**)
- **Lombok**
- **Bean Validation**
- **SpringDoc OpenAPI (Swagger)**
- **Maven**

---

## Funcionalidades

### Perfis de usuário
- **Admin**: gerencia usuários e configurações do sistema
- **Caixa**: cadastra clientes, registra compras e recebe pagamentos
- **Vendedor**: cadastra clientes
- **Cliente**: perfil previsto para consultar parcelas e histórico

### Regras de negócio
- Limite de crédito padrão configurável
- Cliente com parcela atrasada não pode realizar nova compra
- Valor mínimo de compra configurável
- Máximo de parcelas configurável
- Cálculo automático de multa (2%) e juros (0,1% ao dia) para parcelas em atraso
- Comissão de R$ 4,00 por cliente cadastrado
- Bônus de R$ 10,00 se o cliente fizer a primeira compra em até 24 horas do cadastro
- Operações com várias gravações (cadastro de cliente, compra e pagamento) executadas em transação

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

## Como executar

### Pré-requisitos
- Java 21
- Docker (com Docker Compose)

### 1. Clone o repositório
```bash
git clone https://github.com/Wilsouuza/crediario-api.git
cd crediario-api
```

### 2. Crie os arquivos de configuração
```bash
# Linux / macOS
cp .env.example .env
cp src/main/resources/application-example.properties src/main/resources/application.properties
```
```powershell
# Windows (PowerShell)
Copy-Item .env.example .env
Copy-Item src/main/resources/application-example.properties src/main/resources/application.properties
```

Os dois arquivos criados (`.env` e `application.properties`) estão no `.gitignore` e não devem ser versionados.

### 3. Preencha o `.env`

| Variável | Descrição |
|----------|-----------|
| `DB_PASSWORD` | Senha do banco PostgreSQL |
| `DB_PORT` | Porta do banco no seu computador (padrão `5432`; use `5433` se a 5432 estiver ocupada) |
| `JWT_SECRET` | Chave de assinatura dos tokens. **Mínimo de 32 caracteres** |
| `ADMIN_PASSWORD` | Senha do usuário administrador criado na primeira execução |

Para gerar um `JWT_SECRET`:
```bash
openssl rand -base64 48
```
```powershell
# Windows (PowerShell)
[Convert]::ToBase64String((1..48 | ForEach-Object { Get-Random -Maximum 256 }))
```

### 4. Suba o banco de dados
```bash
docker compose up -d
docker compose ps        # o serviço deve aparecer como "healthy"
```

### 5. Execute a API
```bash
./mvnw spring-boot:run
```

### 6. Usuário administrador
Na primeira execução, o sistema cria um administrador:
- **Login:** `admin`
- **Senha:** o valor de `ADMIN_PASSWORD` (se a variável não estiver definida, `admin123`, **apenas para desenvolvimento**)

### 7. Acesse a documentação
```
http://localhost:8080/swagger-ui/index.html
```

### Parar e limpar o banco
```bash
docker compose down        # para o banco, mantendo os dados
docker compose down -v     # para o banco e apaga os dados
```

### Problemas comuns

| Sintoma | Causa e solução |
|---------|-----------------|
| `port is already allocated` ao subir o Docker | A porta já está em uso. Defina `DB_PORT=5433` no `.env` e rode `docker compose up -d` de novo |
| Erro 500 ao fazer login | `JWT_SECRET` com menos de 32 caracteres. Gere um novo e reinicie a API |
| API não conecta no banco após mudar `DB_PASSWORD` | A senha só vale na criação do volume. Rode `docker compose down -v` e suba de novo (apaga os dados) |

---

## Autenticação

A API usa **JWT (JSON Web Token)**. As senhas são armazenadas com **BCrypt**. O fluxo é:

```
1. POST /users/login com login e senha
2. A API retorna um token JWT
3. Enviar o token no header de cada requisição protegida:
   Authorization: Bearer {token}
```

O token tem validade de 24 horas.

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

## Testando os endpoints

A pasta `requests/` tem arquivos `.http` para o IntelliJ HTTP Client:

```
requests/
├── users.http
├── customers.http
├── purchases.http
├── installments.http
└── payments.http
```

Rode primeiro o login em `users.http`. O token é guardado automaticamente na variável `{{token}}`, usada no header `Authorization` das demais requisições. Como o token vale por 24 horas, refaça o login quando ele expirar.

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

## Próximos passos

- Restringir o perfil Cliente aos próprios dados (parcelas e pagamentos)
- Testes automatizados das regras de multa, juros e validação de compra
- Migrations com Flyway no lugar de `ddl-auto=update`
- Execução agendada da atualização de parcelas vencidas

---

## Autor

Wilson: estudante de Análise e Desenvolvimento de Sistemas (IFBA Campus Valença), 4º semestre. Técnico em Informática pelo IFBA.

[GitHub](https://github.com/Wilsouuza) | [LinkedIn](https://www.linkedin.com/in/wilsonpsouza)