# Ecommerce Spring

Trabalho desenvolvido para a disciplina **Arquitetura de Sistemas Distribuídos** da **UTFPR**, **abril de 2026**. Aluno: **Matheus Eduardo Tem Pass**.

Aplicação de exemplo de **e-commerce** em **Spring Boot** (Java 17), com catálogo de produtos persistido em **MongoDB**, APIs REST para fluxo de compra (consulta de CEP, checkout) e página estática da loja em `src/main/resources/static/loja.html`. O servidor HTTP usa a porta **8081**.

## Como rodar com Dev Containers

1. Instale o [Docker](https://docs.docker.com/get-docker/) e a extensão **Dev Containers** no VS Code ou use o **Cursor** com suporte equivalente.

2. Abra a pasta deste repositório e escolha **“Reopen in Container”** / **“Dev Containers: Reopen in Container”**.

   O ambiente sobe dois serviços via Docker Compose:
   - **app**: imagem oficial Dev Containers Java 17 (workspace em `/workspace`).
   - **mongo**: MongoDB 7 (porta **27017** exposta no host).

3. (Opcional) Para variáveis de ambiente no container da aplicação, copie `.devcontainer/.env.example` para `.devcontainer/.env` e ajuste se precisar. Por padrão a URI do MongoDB já está alinhada com `application.properties` (`mongodb://mongo:27017/ecommerce-spring`).

4. Dentro do container, na pasta do projeto:

   ```bash
   ./mvnw spring-boot:run
   ```

5. Acesse no navegador (a porta **8081** é encaminhada pelo Dev Container):
   - API de produtos: `http://localhost:8081/api/products`
   - Loja (HTML): `http://localhost:8081/loja.html`

Na primeira abertura do container, o `postCreateCommand` resolve as dependências Maven automaticamente.
