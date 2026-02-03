# Vehicle Rental System — INF008

Disciplina: INF008 – Programação Orientada a Objetos  
Curso: Análise e Desenvolvimento de Sistemas (ADS)  
Instituição: IFBA  
Professor: Sandro Santos Andrade  

## Autores

- Angelito Chagas de Almeida Alves
- Jônatas Alfa Torquato de Souza

---
# Visão Geral

Este projeto implementa um sistema de gerenciamento de uma locadora de veículos,
desenvolvido em Java, utilizando JavaFX para a interface gráfica e uma arquitetura
baseada em microkernel com suporte a plug-ins.

O sistema foi desenvolvido conforme a especificação do trabalho prático da
disciplina INF008, priorizando modularidade, extensibilidade, uso de interfaces,
polimorfismo e integração real com banco de dados.

---
# Arquitetura

A arquitetura segue o padrão Microkernel, organizada da seguinte forma:

- Kernel:
  Contém as interfaces centrais do sistema e a infraestrutura compartilhada,
  incluindo a conexão com o banco de dados via JDBC.

- App:
  Responsável apenas pela inicialização da aplicação e da interface gráfica
  em JavaFX.

- Plug-ins:
  Funcionalidades adicionais são implementadas como plug-ins independentes,
  carregados dinamicamente em tempo de execução, sem necessidade de recompilar
  o sistema principal.

Essa abordagem permite adicionar ou remover funcionalidades de forma simples
e organizada.

---

# Plug-ins Implementados

1. Plug-ins de Tipos de Veículos

- Economy
- Compact
- SUV
- Luxury
- VAN
- Electric

Cada tipo de veículo implementa seu próprio comportamento, utilizando
interfaces e polimorfismo para o cálculo do valor da locação.

2. Plug-in de Locação

Permite realizar uma nova locação de veículo, contemplando:

- Seleção de cliente
- Seleção de tipo de veículo
- Exibição de veículos disponíveis
- Cálculo polimórfico do valor total da locação
- Inserção do registro na tabela de locações
- Atualização do status do veículo para RENTED

A listagem de veículos considera apenas aqueles com status AVAILABLE. Caso não haja veículos disponíveis para determinado tipo, a tabela permanecerá vazia.

3. Relatório 1 — Distribuição por Tipo de Combustível

- Implementado como plug-in independente
- Apresenta um gráfico de pizza (JavaFX PieChart)
- Dados obtidos diretamente do banco de dados via JDBC
- Utiliza a query fornecida no arquivo report1.sql

4. Relatório 2 — Relatório Geral de Locações
- Implementado como plug-in independente
- Apresenta uma tabela JavaFX (TableView)
- Dados obtidos diretamente do banco de dados via JDBC
- Utiliza a query fornecida no arquivo report2.sql

---
# Banco de Dados

O banco de dados utilizado é o MariaDB, executado em container Docker conforme
ambiente fornecido pelo professor.

O acesso ao banco é realizado exclusivamente via JDBC, utilizando o driver
oficial do MariaDB.

---
# Tecnologias Utilizadas

- Java 25.0.1
- JavaFX
- Maven
- JDBC
- MariaDB
- Docker e Docker Compose

---
# Como Executar o Projeto

1. Pré-requisitos

- Java JDK 25 instalado
- Maven instalado
- Docker e Docker Compose instalados

2. Subir o Banco de Dados

Acesse o diretório:

```bash
docs/docker
```

Execute:

```bash
docker-compose up -d
```

Verifique se o container está em execução:

```bash
docker ps
```

3. Compilar o Projeto

Na raiz do projeto, execute:

```bash
mvn clean install
```

4. Executar a Aplicação

Após a compilação, execute a aplicação com:

```bash
mvn -pl app exec:java
```

A interface gráfica será aberta em JavaFX, contendo:

- Aba de locação
- Relatório por tipo de combustível
- Relatório geral de locações

---
# Vídeo de Apresentação

Link do vídeo explicativo (até 5 minutos):

(INSERIR LINK AQUI)

---
# Observações Finais

- Os relatórios utilizam dados reais do banco de dados, conforme exigido.
- As queries SQL fornecidas pelo professor foram utilizadas sem alterações lógicas.
- O projeto respeita os princípios de interfaces, polimorfismo e extensibilidade.
- O código entregue não contém arquivos .class, apenas arquivos .java.

---