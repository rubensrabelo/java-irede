# Projeto: FinTrack

## Descricao do Projeto (Modulo Intermediario)
O FinTrack e um sistema de gestao de financas pessoais estruturado com Java 21, interface grafica JavaFX, persistencia relacional MySQL via Docker e arquitetura robusta baseada em padroes de projeto corporativos.

O sistema permite ao usuario:
* Cadastrar e editar transacoes (Entradas e Saidas) com suporte a decimais nacionais (virgula).
* Listar movimentacoes de forma reativa em tabelas com formatacao brasileira de data e moeda (R$).
* Exibir balanco de saldo total sincronizado em tempo real.
* Remover registros de forma fisica utilizando chaves primarias diretas do banco de dados.

---

## O que eu fiz de diferente do projeto anterior

Esta versão representa uma reformulação estrutural completa em relação ao projeto console inicial, introduzindo práticas modernas de engenharia de software:

* **Gerenciamento de Dependências com Maven:** Migração do gerenciamento manual de bibliotecas para o Apache Maven, centralizando dependências modulares e plugins do compilador.
* **Interface Gráfica Reativa com JavaFX:** Substituição completa do fluxo de terminal via console por uma interface visual dinâmica (FXML e CSS) com tabelas reativas e caixas de dialogo controladas.
* **Persistência Relacional com MySQL no Docker:** Saída definitiva do armazenamento volátil em memória RAM para adoção de persistência robusta em banco de dados MySQL rodando em container orquestrado.
* **Abstração por Interface no Repository via DI:** Introdução de contratos via interfaces genéricas no ecossistema de persistência, desacoplando totalmente a infraestrutura e injetando a dependência diretamente no ciclo de vida do `TransactionService`.
* **Isolamento de Domínio com DTOs e Mappers:** Implementação de Java Records (DTOs) e conversores dedicados (Mappers) para trafegar dados de forma segura entre os controladores JavaFX e a camada de serviços, impedindo o vazamento de entidades de domínio para a interface visual.
* **Inversão de Dependência e Factory Pattern:** Extinção do acoplamento forte provocado pelo operador `new` dentro de controladores, centralizando o ciclo de vida dos objetos no `ControllerFactory`.
* **Segurança Transacional Manual (ACID):** Implementação de blocos de controle de transação explicitos (`commit` e `rollback`) com desativação de `autoCommit` no JDBC para proteger a integridade dos dados.
* **Hierarquia de Exceções Personalizadas:** Criação de erros específicos para evitar vazamento de metadados e credenciais de infraestrutura para as telas do usuário.
* **Suíte de Testes JUnit 5 Dupla:** Estruturação de testes unitários puros para regras de negócio e testes de integração relacional utilizando banco de dados SQLite em memória.

---

## Links de Documentacao

* [Modelagem de Dados do Banco de Dados](./docs/DATA_MODEL.md)
* [Arquitetura do Sistema (C4 Component Model)](./docs/ARCHITECTURE.md)
* [Padroes de Projeto Utilizados (DI e Factory)](./docs/PATTERNS.md)
* [Interface Grafica e Descricao das Telas](./docs/SCREENS.md)
* [Estrutura de Diretorios e Pastas](./docs/STRUCTURE.md)
* [Manual de Configuracao (.env) e Execucao](./docs/SCRIPTS.md)

