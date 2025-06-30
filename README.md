# API de Produtos - Projeto de E-commerce

Esta é a segunda API do projeto de e-commerce, desenvolvida com Java 17 e Spring Boot. A API gerencia dados de produtos, incluindo criação, leitura, atualização e exclusão de registros, com recursos de buscas rápidas e avançadas, cache e integração com banco de dados PostgreSQL.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4.3**
- **Maven**
- **SpringBoot DevTools** para desenvolvimento mais ágil
- **Spring Data JPA** para integração com banco de dados
- **ElasticSearch** para consultas rápidas e avançadas
- **RabbitMQ** para adicionar e atualizar produtos no Estoque (próxima API) 
- **Redis** para cache (Write-through, validação após cada consulta e invalidação após cada alteração no banco ou TTL de 15 minutos)
- **PostgreSQL** como banco de dados relacional
- **Hibernate Validator** para validação de dados
- **MapStruct** para mapeamento de objetos
- **JUnit 5** para testes unitários
- **Mockito** para criação de mocks em testes

## Como Rodar a Aplicação

1. Clone o repositório:
   ```bash
   git clone https://github.com/ItaloAlisson/e-commerce-produto.git
   ```

2. Crie e configure as variáveis de ambiente no arquivo `env.properties`:
   ```properties
    SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/seubanco
    SPRING_DATASOURCE_USERNAME=seuusuario
    SPRING_DATASOURCE_PASSWORD=suasenha
   ```

3. Fazer download do ElasticSearch: 

    www.elastic.co/pt/downloads/elasticsearch


4. Para rodar o RabbitMQ com Docker, siga os passos abaixo:

    **Baixe e execute o RabbitMQ no Docker**:
       Execute o comando abaixo para rodar o RabbitMQ usando Docker. O comando configura o RabbitMQ com a interface de administração ativada.

   ```bash
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=123456 rabbitmq:management
   ```

    **Acesse a interface de administração**:
       Após iniciar o RabbitMQ, você pode acessar a interface de administração em [http://localhost:15672](http://localhost:15672). Faça login com o usuário `admin` e a senha `123456`.


5. Para rodar a aplicação, utilize o Maven:
   ```bash
   mvn spring-boot:run
   ```

6. A API estará rodando em `http://localhost:8080`.

## Endpoints

A API oferece os seguintes endpoints para interação com os dados dos produtos:

- `POST /produtos` - Registra um novo produto
- `GET /produtos` - Retorna a lista de produtos
- `GET /produtos/{nome}` - Retorna um produto específico
- `PUT /produtos/{id}` - Atualiza os dados de um produto
- `DELETE /produtos/{id}` - Deleta um produto

### Exemplos de Uso

#### Registrar Produto
```bash
POST /produtos
Content-Type: application/json
{
  "nome": "Moto G75",
  "marca": "Motorola",
  "preco": 1400.99,
  "quantidade": 600,
  "categoria": "1",
  "descricao": "Primeiro moto g com ultrarrêsistencia, O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, Som Dolby Atmos e tela Full HD+ de 6,8” com superbrilho e Smart Water Touch . Velocidade e eficiência do novo Snapdragon."
}
```

#### Obter Produtos
```bash
GET /produtos
```

#### Obter Produto
```bash
GET /produtos/Moto G
```

#### Atualizar Registro Produto
```bash
PUT /produtos/0bccffb3-e4f4-483c-a2db-71f464aaf1ca
Content-Type: application/json
{
  "nome": "Moto g75",
  "marca": "Motorola",
  "preco": 3000,
  "quantidade": 40,
  "categoria": "1",
   "descricao": "Primeiro moto g com ultrarrêsistencia, O poder da IA e a câmera Sony - LYTIA 600, 5 anos de atualização de Android, Som Dolby Atmos e tela Full HD+ de 6,8” com superbrilho e Smart Water Touch . Velocidade e eficiência do novo Snapdragon."


}
```

#### Deletar Produto
```bash
DELETE /produtos/0bccffb3-e4f4-483c-a2db-71f464aaf1ca
```




## Configuração de Cache

A API utiliza o Redis para caching. O mecanismo de caching está configurado para usar **write-through**, ou seja, todas as operações de gravação no banco de dados também atualizam o cache. O cache é invalidado quando ocorre uma alteração no banco de dados ou após 15 minutos de TTL (time-to-live).

## Testes

Para rodar os testes unitários, execute:

```bash
mvn test
```