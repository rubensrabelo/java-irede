package app.application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.math.BigDecimal;
import java.time.LocalDate;

import app.shared.exceptions.TransactionPersistenceException;
import app.shared.exceptions.VisualRenderingException;
import app.shared.factory.ControllerFactory;
import app.shared.utils.Formatter;
import app.application.dto.TransactionResponseDTO;
import app.application.service.TransactionService;

public class MainViewController {

    @FXML private Label lblBalance;
    @FXML private TableView<TransactionResponseDTO> tblTransactions;
    @FXML private TableColumn<TransactionResponseDTO, LocalDate> colDate;
    @FXML private TableColumn<TransactionResponseDTO, String> colCategory;
    @FXML private TableColumn<TransactionResponseDTO, String> colType;
    @FXML private TableColumn<TransactionResponseDTO, BigDecimal> colAmount;
    @FXML private TableColumn<TransactionResponseDTO, Void> colActions;

    private final TransactionService service;
    private ObservableList<TransactionResponseDTO> observableList;

    public MainViewController(TransactionService service) {
        this.service = service;
    }

    @FXML
    public void initialize() {
        try {
            this.observableList = FXCollections.observableArrayList(service.findAll());

            colCategory.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().category()));
            colAmount.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().amount()));
            colType.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().typeDescription()));
            colDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().date()));

            setupDateColumnFormatting();
            setupAmountColumnFormatting();
            setupTypeColumnTranslation();
            setupActionButtons();
            
            tblTransactions.setItems(observableList);
            updateUI();
        } catch (Exception e) {
            System.err.println("[LOG CRÍTICO INFRA]: " + e.getMessage());
            e.printStackTrace();
            showDatabaseErrorAlert("Não foi possível carregar as informações devido a uma falha na conexão com o banco de dados.");
            throw new TransactionPersistenceException("Falha crítica ao carregar listagem inicial de dados no repositório.", e);
        }
    }

    public void updateUI() {
        try {
            observableList.setAll(service.findAll());
            BigDecimal balance = service.calculateTotalBalance();
            lblBalance.setText(Formatter.formatCurrency(balance));
        } catch (Exception e) {
            System.err.println("[LOG CRÍTICO INFRA]: " + e.getMessage());
            e.printStackTrace();
            showDatabaseErrorAlert("Não foi possível atualizar o saldo devido a uma falha na sincronização com o servidor de dados.");
            throw new TransactionPersistenceException("Falha crítica ao atualizar sincronização de dados e balanço de saldos.", e);
        }
    }

    private void setupDateColumnFormatting() {
        colDate.setCellFactory(column -> new TableCell<TransactionResponseDTO, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(Formatter.formatDate(item));
                }
            }
        });
    }

    private void setupAmountColumnFormatting() {
        colAmount.setCellFactory(column -> new TableCell<TransactionResponseDTO, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(Formatter.formatCurrency(item));
                }
            }
        });
    }

    private void setupTypeColumnTranslation() {
        colType.setCellFactory(column -> new TableCell<TransactionResponseDTO, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().removeAll("type-income", "type-outcome");
                } else {
                    getStyleClass().removeAll("type-income", "type-outcome");
                    setText(item);
                    if ("Entrada".equalsIgnoreCase(item)) {
                        getStyleClass().add("type-income");
                    } else if ("Saída".equalsIgnoreCase(item)) {
                        getStyleClass().add("type-outcome");
                    }
                }
            }
        });
    }

    private void setupActionButtons() {
        colActions.setCellFactory(new Callback<TableColumn<TransactionResponseDTO, Void>, TableCell<TransactionResponseDTO, Void>>() {
            @Override
            public TableCell<TransactionResponseDTO, Void> call(final TableColumn<TransactionResponseDTO, Void> param) {
                return new TableCell<TransactionResponseDTO, Void>() {
                    private final Button btnEdit = new Button("EDITAR");
                    private final Button btnDelete = new Button("EXCLUIR");
                    private final HBox pane = new HBox(8, btnEdit, btnDelete);

                    {
                        pane.setAlignment(javafx.geometry.Pos.CENTER);
                        btnEdit.getStyleClass().add("btn-table-edit");
                        btnDelete.getStyleClass().add("btn-table-delete");

                        btnEdit.setOnAction(event -> {
                            TransactionResponseDTO transaction = getTableView().getItems().get(getIndex());
                            openFormWindow(transaction);
                        });

                        btnDelete.setOnAction(event -> {
                            try {
                                TransactionResponseDTO transaction = getTableView().getItems().get(getIndex());
                                if (transaction != null && transaction.id() != null) {
                                    service.deleteById(transaction.id());
                                    updateUI();
                                }
                            } catch (Exception e) {
                                System.err.println("[LOG CRÍTICO INFRA]: " + e.getMessage());
                                e.printStackTrace();
                                showDatabaseErrorAlert("Não foi possível excluir o item selecionado devido a uma instabilidade com o banco de dados.");
                                throw new TransactionPersistenceException("Falha crítica ao executar deleção física por id.", e);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
            }
        });
    }

    @FXML
    private void handleNewTransaction() {
        openFormWindow(null);
    }

    private void openFormWindow(TransactionResponseDTO transactionToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/form-view.fxml"));
            loader.setControllerFactory(new ControllerFactory(this.service));
            Parent root = loader.load();

            FormViewController formController = loader.getController();
            formController.setMainController(this, transactionToEdit);

            Stage stage = new Stage();
            stage.setTitle(transactionToEdit == null ? "Nova Transação" : "Editar Transação");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            System.err.println("[LOG CRÍTICO INTERFACE]: " + e.getMessage());
            e.printStackTrace();
            showInterfaceErrorAlert("Não foi possível inicializar os componentes gráficos da janela solicitada.");
            throw new VisualRenderingException("Falha crítica de carregamento estrutural do arquivo FXML de formulário.", e);
        }
    }

    private void showDatabaseErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro de Comunicação");
        alert.setHeaderText("Instabilidade no Servidor");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInterfaceErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro de Renderização");
        alert.setHeaderText("Falha ao abrir janela");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
