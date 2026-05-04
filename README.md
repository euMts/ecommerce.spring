# Ecommerce Spring

Trabalho desenvolvido para a disciplina **Arquitetura de Sistemas Distribuídos** da **UTFPR**, **abril de 2026**. Aluno: **Matheus Eduardo Tem Pass**.

Aplicação de exemplo de **e-commerce** em **Spring Boot** com **Java 17**, catálogo de produtos persistido em **MongoDB**, página web estática da loja e APIs REST para simular uma arquitetura distribuída com orquestração de serviços.

O projeto continua sendo uma única aplicação para facilitar a demonstração em sala, mas cada etapa do fluxo de compra foi exposta como um endpoint próprio. A comunicação entre as etapas acontece via HTTP, usando o `PurchaseOrchestrationService` como orquestrador.

## Objetivo

O objetivo da implementação é representar, dentro de um único projeto, um cenário que normalmente seria dividido em vários serviços independentes.

Em vez de o orquestrador chamar métodos Java diretamente de outros arquivos, ele chama endpoints REST internos. Assim, cada controller representa um serviço simulado da arquitetura distribuída.

## Fluxo implementado

1. Loja Web exibe catálogo de produtos
2. Usuário escolhe produtos para comprar
3. Usuário informa dados de pagamento e endereço de entrega
4. Usuário informa CEP para preenchimento automático do endereço
5. Usuário confirma a compra
6. Sistema envia e-mail de confirmação da compra
7. Sistema realiza a transação de pagamento
8. Sistema envia e-mail com resultado do pagamento
9. Sistema gera nota fiscal e baixa o estoque
10. Sistema envia e-mail com a nota fiscal
11. Sistema disponibiliza produtos para entrega
12. Sistema envia e-mail com dados da entrega

## Orquestração

O endpoint público de confirmação da compra é:

```text
POST /api/checkout/confirm
```

Esse endpoint recebe o carrinho, os dados de pagamento e o endereço de entrega. Ele delega o fluxo para o `PurchaseOrchestrationService`, que chama os endpoints internos na ordem necessária.

O orquestrador usa `RestClient` para chamar os serviços simulados por HTTP:

```text
http://localhost:8081/api/distributed/checkout
```

Durante a execução, os logs mostram em português o que está acontecendo em cada etapa, por exemplo:

```text
[Orquestrador] Chamando endpoint /payments para processar pagamento
[Serviço de Pagamento] Pedido ... valor ... cartão ... titular ...
[Serviço de Nota Fiscal] ... emitida para o pedido ...
```

## Serviços simulados

Cada serviço foi separado em um controller próprio:

| Serviço simulado | Controller | Endpoint |
|---|---|---|
| Validação do checkout | `CheckoutValidationController` | `POST /api/distributed/checkout/validate-input` |
| Cálculo do carrinho | `CartCalculationController` | `POST /api/distributed/checkout/calculate-total` |
| Estoque | `StockController` | `POST /api/distributed/checkout/validate-stock` |
| Pedido | `OrderController` | `POST /api/distributed/checkout/orders` |
| E-mail | `EmailController` | `POST /api/distributed/checkout/emails/...` |
| Pagamento | `PaymentController` | `POST /api/distributed/checkout/payments` |
| Nota fiscal | `InvoiceController` | `POST /api/distributed/checkout/invoices` |
| Entrega | `ShippingController` | `POST /api/distributed/checkout/shipments` |

Também existe o `DistributedCheckoutExceptionHandler`, responsável por padronizar respostas de erro dos endpoints distribuídos.

## Endpoints principais

```text
GET  /
GET  /api/products
POST /api/addresses/postal-code
POST /api/checkout/confirm
```

## Endpoints distribuídos

```text
POST /api/distributed/checkout/validate-input
POST /api/distributed/checkout/calculate-total
POST /api/distributed/checkout/validate-stock
POST /api/distributed/checkout/orders
POST /api/distributed/checkout/emails/order-confirmation
POST /api/distributed/checkout/payments
POST /api/distributed/checkout/emails/payment-result
POST /api/distributed/checkout/invoices
POST /api/distributed/checkout/emails/invoice
POST /api/distributed/checkout/shipments
POST /api/distributed/checkout/emails/shipping
```

## Postman

A collection está em:

```text
postman/ecommerce-spring.postman_collection.json
```

Ela possui três grupos:

| Grupo | Conteúdo |
|---|---|
| `General` | Health check, catálogo de produtos e busca de CEP |
| `Orchestrated checkout` | Fluxo completo chamando `POST /api/checkout/confirm` |
| `Distributed checkout steps` | Cada endpoint distribuído separado, numerado em ordem |

Antes de executar as chamadas do checkout, use o endpoint de catálogo para obter um `productId` válido e atualize a variável `productId` na collection.

## Loja Web

A loja está disponível em:

```text
http://localhost:8081/loja.html
```

Ela permite visualizar produtos, montar o carrinho, buscar endereço por CEP e confirmar a compra.

## Como rodar com Dev Containers

1. Instale o [Docker](https://docs.docker.com/get-docker/) e a extensão **Dev Containers** no VS Code ou use o **Cursor** com suporte equivalente.

2. Abra a pasta deste repositório e escolha **Reopen in Container** ou **Dev Containers: Reopen in Container**.

3. O ambiente sobe dois serviços via Docker Compose:

| Serviço | Descrição |
|---|---|
| `app` | Imagem oficial Dev Containers Java 17 |
| `mongo` | MongoDB 7, com porta 27017 exposta no host |

4. Dentro do container, na pasta do projeto, execute:

```bash
./mvnw spring-boot:run
```

5. Acesse:

```text
http://localhost:8081/api/products
http://localhost:8081/loja.html
```

## Configuração

O servidor HTTP usa a porta:

```text
8081
```

A URI padrão do MongoDB está em `src/main/resources/application.properties`:

```text
mongodb://mongo:27017/ecommerce-spring
```

Se necessário, ela pode ser sobrescrita pela variável de ambiente `SPRING_MONGODB_URI`.
