# Como rodar o projeto ManuFactoy

Este projeto possui duas partes principais: o backend (Spring Boot + PostgreSQL) e o frontend (React + Vite).

## Pré-requisitos
- Docker e Docker Compose
- Java 21
- Maven
- Node.js (recomendado versão 18+)

---

## Backend

O backend está localizado na pasta `back/` e utiliza Spring Boot, Maven e PostgreSQL.

### 1. Subindo o banco de dados

Execute o comando abaixo na pasta `back/` para iniciar o banco PostgreSQL via Docker Compose:

```bash
docker-compose up -d
```

O banco ficará disponível em `localhost:5432` com:
- Database: `manu_facturing`
- User: `root`
- Password: `root`

### 2. Rodando a aplicação

Você pode rodar o backend usando Maven:

```bash
./mvnw spring-boot:run
```

Ou, se preferir, use o Maven instalado:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

### 3. Documentação Swagger (OpenAPI)

Após iniciar o backend, acesse a documentação interativa da API (Swagger UI) em:

```
http://localhost:8080/swagger-ui/index.html
```

Você pode explorar e testar os endpoints diretamente pela interface.


### 4. Testes automatizados

Para rodar os testes do backend, execute na pasta `back/`:

```bash
./mvnw test
```

Ou, se preferir, use o Maven instalado:

```bash
mvn test
```

Os testes estão localizados em `back/src/test/java/`.



## Frontend

O frontend está na pasta `front/` e utiliza React, Vite e Tailwind.

### 1. Instalando dependências

Entre na pasta `front/` e execute:

```bash
npm install
```

### 2. Rodando o servidor de desenvolvimento

Ainda na pasta `front/`, execute:

```bash
npm run dev
```

O frontend estará disponível em `http://localhost:5173`.

---

## Observações
- O backend espera o banco de dados rodando em `localhost:5432`.
- As configurações do banco estão em `back/src/main/resources/application.properties`.

---

## Referências
- [Spring Boot](https://spring.io/projects/spring-boot)
- [React](https://react.dev/)
- [Vite](https://vitejs.dev/)
- [Docker Compose](https://docs.docker.com/compose/)

---