package app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import app.factory.ControllerFactory;
import app.model.Transaction;
import app.model.enums.TransactionType;
import app.service.TransactionService;

public class MainViewController {

    @FXML private Label lblBalance;
    @FXML private TableView<Transaction> tblTransactions;
    @FXML private TableColumn<Transaction, LocalDate> colDate;
    @FXML private TableColumn<Transaction, String> colCategory;
    @FXML private TableColumn<Transaction, TransactionType> colType;
    @FXML private TableColumn<Transaction, BigDecimal> colAmount;
    @FXML private TableColumn<Transaction, Void> colActions;

    private final TransactionService service;
    private ObservableList<Transaction> observableList;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MainViewController(TransactionService service) {
        this.service = service;
    }

    @FXML
    public void initialize() {
        this.observableList = FXCollections.observableArrayList(service.findAll());

        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        setupDateColumnFormatting();
        setupTypeColumnTranslation();
        setupActionButtons();
        
        tblTransactions.setItems(observableList);
        updateUI();
    }

    public void updateUI() {
        observableList.setAll(service.findAll());
        BigDecimal balance = service.calculateTotalBalance();
        lblBalance.setText("Saldo: R$ " + balance);
    }

    private void setupDateColumnFormatting() {
        colDate.setCellFactory(column -> new TableCell<Transaction, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });
    }

    private void setupTypeColumnTranslation() {
        colType.setCellFactory(column -> new TableCell<Transaction, TransactionType>() {
            @Override
            protected void updateItem(TransactionType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().removeAll("type-income", "type-outcome");
                } else {
                    getStyleClass().removeAll("type-income", "type-outcome");
                    if (item == TransactionType.INCOME) {
                        setText("Entrada");
                        getStyleClass().add("type-income");
                    } else if (item == TransactionType.OUTCOME) {
                        setText("Saída");
                        getStyleClass().add("type-outcome");
                    }
                }
            }
        });
    }

    private void setupActionButtons() {
        colActions.setCellFactory(new Callback<TableColumn<Transaction, Void>, TableCell<Transaction, Void>>() {
            @Override
            public TableCell<Transaction, Void> call(final TableColumn<Transaction, Void> param) {
                return new TableCell<Transaction, Void>() {
                    private final Button btnEdit = new Button("EDITAR");
                    private final Button btnDelete = new Button("EXCLUIR");
                    private final HBox pane = new HBox(8, btnEdit, btnDelete);

                    {
                        pane.setAlignment(javafx.geometry.Pos.CENTER);
                        btnEdit.getStyleClass().add("btn-table-edit");
                        btnDelete.getStyleClass().add("btn-table-delete");

                        btnEdit.setOnAction(event -> {
                            Transaction transaction = getTableView().getItems().get(getIndex());
                            openFormWindow(transaction);
                        });

                        btnDelete.setOnAction(event -> {
                            Transaction transaction = getTableView().getItems().get(getIndex());
                            if (transaction != null && transaction.getId() != null) {
                                service.deleteById(transaction.getId());
                                updateUI();
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

    private void openFormWindow(Transaction transactionToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/form-view.fxml"));
            loader.setControllerFactory(new ControllerFactory(this.service));
            
            Parent root = loader.load();
            
            FormViewController formController = loader.getController();
            formController.setMainController(this, transactionToEdit);

            Stage stage = new Stage();
            stage.setTitle(transactionToEdit == null ? "Nova Transação" : "Editar Transação");
            stage.setScene(new Scene(root, 500, 400));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (Exception e) {
            System.out.println("Erro ao abrir a tela de formulário:");
            e.printStackTrace();
        }
    }
}
