package app.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.time.LocalDate;
import app.model.Transaction;

public class FormViewController {

    @FXML private ComboBox<String> cmbType;
    @FXML private TextField txtCategory;
    @FXML private TextField txtAmount;
    @FXML private DatePicker txtDate;

    private MainViewController mainController;
    private FinTracker finTracker;

    @FXML
    public void initialize() {
        cmbType.setItems(FXCollections.observableArrayList("Entrada", "Saída"));
        txtDate.setValue(LocalDate.now()); // Deixa a data atual pré-selecionada
    }

    public void setMainController(MainViewController mainController, FinTracker finTracker) {
        this.mainController = mainController;
        this.finTracker = finTracker;
    }

    @FXML
    private void handleSave() {
        try {
            String type = cmbType.getValue();
            String category = txtCategory.getText();
            String amountStr = txtAmount.getText();
            LocalDate date = txtDate.getValue();

            if (type == null || category.isEmpty() || amountStr.isEmpty() || date == null) {
                System.out.println("Erro: Preencha todos os campos da interface gráfica.");
                return;
            }

            BigDecimal amount = new BigDecimal(amountStr);
            
            // Cria a transação usando os dados que você digitou na tela
            Transaction transaction = new Transaction(date, type, category, amount);
            
            // Grava o registro através do FinTracker
            finTracker.addTransaction(transaction);
            
            // Força a tela de trás (Main) a recarregar a tabela e recalcular o saldo imediatamente
            mainController.updateUI();
            
            closeStage();
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite um valor numérico válido.");
        }
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) txtCategory.getScene().getWindow();
        stage.close();
    }
}
