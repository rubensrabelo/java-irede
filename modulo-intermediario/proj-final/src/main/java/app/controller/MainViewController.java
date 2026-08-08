package app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.time.LocalDate;
import app.model.Transaction;
import app.repository.DataRepository;
import app.service.TransactionService;

public class MainViewController {

    @FXML private Label lblBalance;
    @FXML private TableView<Transaction> tblTransactions;
    @FXML private TableColumn<Transaction, LocalDate> colDate;
    @FXML private TableColumn<Transaction, String> colCategory;
    @FXML private TableColumn<Transaction, String> colType;
    @FXML private TableColumn<Transaction, BigDecimal> colAmount;

    private TransactionService service;
    private ObservableList<Transaction> observableList;

    @FXML
    public void initialize() {
        this.service = new TransactionService(new DataRepository<>());
        this.observableList = FXCollections.observableArrayList(service.findAll());

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tblTransactions.setItems(observableList);
        updateUI();
    }

    public void updateUI() {
        observableList.setAll(service.findAll());
        BigDecimal balance = service.calculateTotalBalance();
        lblBalance.setText("Saldo: R$ " + balance);
    }

    @FXML
    private void handleNewTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/form-view.fxml"));
            Parent root = loader.load();
            
            FormViewController formController = loader.getController();
            formController.setMainController(this, service);

            Stage stage = new Stage();
            stage.setTitle("Nova Transação");
            stage.setScene(new Scene(root, 500, 400));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (Exception e) {
            System.out.println("Erro ao abrir a tela de formulário:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteTransaction() {
        Transaction selected = tblTransactions.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.findAll().remove(selected); 
            updateUI();
        }
    }
}
