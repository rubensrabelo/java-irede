# Interfaces e Funcionalidades do Aplicativo

Este documento descreve as telas que compoem a interface grafica do FinTrack, detalhando os componentes visuais do JavaFX e o fluxo de comunicacao com as camadas de negocio e persistencia.

---

## 1. Tela Principal (Dashboard Financeiro)

A tela principal centraliza a visualizacao das movimentacoes e o balanco consolidado do usuario, operando de forma reativa com o banco de dados.

![Tela Principal](img/main-screen.png)

### Componentes e Comportamentos Visuais
* **Painel de Saldo Geral:** Exibe o saldo total consolidado. O valor e calculado de forma polimorfica pelo `TransactionService` somando as entradas e subtraindo as saidas, sendo renderizado com a mascara de moeda nacional (`R$`) fornecida pelo utilitario `Formatter`.
* **Tabela de Transacoes (TableView):** Lista os registros recuperados do MySQL por meio do `TransactionDbRepository`. As colunas de Data e Valor aplicam fabricas de celulas customizadas (`setCellFactory`) para exibir os dados nos padroes brasileiros (`dd/MM/yyyy` e `R$`).
* **Coluna de Acoes:** Cada linha da tabela possui dois botoes especificos:
  * **EDITAR:** Captura o objeto da linha selecionada e invoca a abertura da janela de formulario repassando a referencia para alteracao.
  * **EXCLUIR:** Dispara a remocao fisica do registro diretamente no banco de dados utilizando a chave primaria (`Long id`). O fluxo e protegido por controle transacional ACID, atualizando o saldo e a tabela instantaneamente apos a confirmacao.
* **Botao Nova Transacao:** Posicionado no topo da interface para abrir o formulario limpo para criacao de novos registros.

---

## 2. Tela de Formulario (Nova Transacao e Edicao)

Esta janela e aberta de forma modal (`APPLICATION_MODAL`), bloqueando a interacao com a tela de fundo ate que a operacao atual seja concluida ou cancelada.

![Tela de Formulario](img/form-screen.png)

### Componentes e Fluxo de Validacao
* **Tipo de Transacao (ComboBox):** Permite selecionar estritamente as opcoes "Entrada" ou "Saida", mapeadas internamente para o enumerador `TransactionType`.
* **Categoria e Data (TextField e DatePicker):** Campos de preenchimento obrigatorio. Ao carregar um registro para edicao, o `DatePicker` e os campos de texto sao populados automaticamente com os dados antigos recuperados por ID.
* **Valor da Movimentacao (TextField):** Suporta a digitacao com virgula (padrao nacional). O metodo `handleSave()` intercepta o clique em salvar, normaliza o caractere substituindo a virgula por ponto e encapsula o valor em um tipo `BigDecimal`.
* **Mecanismo de Intercepcao de Erros:**
  * Se houver campos em branco ou se o valor numrico for invalido/negativo, o controlador interrompe o processo, lanca a excecao customizada `InvalidInputException` e abre um alerta visual (`Alert`) do tipo `WARNING` na tela do usuario sem vazar metadados do sistema.
  * Se os dados estiverem corretos, o sistema identifica se e um novo registro (`service.save()`) ou uma edicao (`service.update()`), confirmando as alteracoes com `conn.commit()` antes de recarregar a listagem e fechar a janela.
