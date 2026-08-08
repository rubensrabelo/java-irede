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

Esta versao representa uma reformulacao estrutural completa em relacao ao projeto console inicial, introduzindo praticas modernas de engenharia de software:

* **Gerenciamento de Dependencias com Maven:** Migracao do gerenciamento manual de bibliotecas para o Apache Maven, centralizando dependencias modulares e plugins do compilador.
* **Interface Grafica Reativa com JavaFX:** Substituicao completa do fluxo de terminal via console por uma interface visual dinamica (FXML e CSS) com tabelas reativas e caixas de dialogo controladas.
* **Persistencia Relacional com MySQL no Docker:** Saida definitiva do armazenamento volatil em memoria RAM para adocao de persistencia robusta em banco de dados MySQL 26.7 rodando em container orquestrado.
* **Abstracao Dupla com DAO e Repository:** Separacao explicita de responsabilidades onde o `TransactionDAO` manipula scripts SQL puros e o `TransactionDbRepository` gerencia colecoes logicas com Generics.
* **Inversao de Dependencia e Factory Pattern:** Extincao do acoplamento forte provocado pelo operador `new` dentro de controladores, centralizando o ciclo de vida dos objetos no `ControllerFactory`.
* **Seguranca Transacional Manual (ACID):** Implementacao de blocos de controle de transacao explicitos (`commit` e `rollback`) com desativacao de `autoCommit` no JDBC para proteger a integridade dos dados.
* **Hierarquia de Excecoes Personalizadas:** Criacao de erros especificos para evitar vazamento de metadados e credenciais de infraestrutura para as telas do usuario.
* **Suite de Testes JUnit 5 Dupla:** Estruturacao de testes unitarios puros para regras de negocio e testes de integracao relacional utilizando banco de dados SQLite em memoria.

---

## Links de Documentacao

* [Modelagem de Dados do Banco de Dados](./docs/DATA_MODEL.md)
* [Arquitetura do Sistema (C4 Component Model)](./docs/ARCHITECTURE.md)
* [Padroes de Projeto Utilizados (DI e Factory)](./docs/PATTERNS.md)
* [Interface Grafica e Descricao das Telas](./docs/SCREENS.md)
* [Estrutura de Diretorios e Pastas](./docs/STRUCTURE.md)
* [Manual de Configuracao (.env) e Execucao](./docs/SCRIPTS.md)

