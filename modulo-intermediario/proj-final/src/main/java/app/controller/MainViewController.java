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

    private FinTracker finTracker;
    private ObservableList<Transaction> observableList;

    @FXML
    public void initialize() {
        // Inicializa as dependências usando a inversão via construtor
        this.finTracker = new FinTracker(new TransactionService(new DataRepository<>()));
        this.observableList = FXCollections.observableArrayList(finTracker.listTransactions());

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tblTransactions.setItems(observableList);
        updateUI();
    }

    public void updateUI() {
        // Atualiza a tabela com os dados atualizados do controlador
        observableList.setAll(finTracker.listTransactions());
        
        // Atualiza o saldo visual na tela principal
        BigDecimal balance = finTracker.displayTotalBalance();
        lblBalance.setText("Saldo: R$ " + balance);
    }

    @FXML
    private void handleNewTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/view/form-view.fxml"));
            Parent root = loader.load();
            
            // Passa a referência deste controlador para a segunda tela conseguir atualizar a tabela
            FormViewController formController = loader.getController();
            formController.setMainController(this, finTracker);

            Stage stage = new Stage();
            stage.setTitle("Nova Transação");
            
            // CORREÇÃO: Define o tamanho ideal diretamente na Scene para ela abrir grande (Largura: 500, Altura: 400)
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
            // Como em memória usamos o ID simulado sequencial, precisamos buscar o índice ou mapear
            // Para simplificar a remoção na lista em memória usando a linha selecionada da tabela:
            finTracker.listTransactions().remove(selected); 
            updateUI();
        }
    }
}
