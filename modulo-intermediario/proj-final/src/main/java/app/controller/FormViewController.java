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
import app.service.TransactionService;

public class FormViewController {

    @FXML private ComboBox<String> cmbType;
    @FXML private TextField txtCategory;
    @FXML private TextField txtAmount;
    @FXML private DatePicker txtDate;

    private MainViewController mainController;
    private TransactionService service;

    @FXML
    public void initialize() {
        cmbType.setItems(FXCollections.observableArrayList("Entrada", "Saída"));
        txtDate.setValue(LocalDate.now());
    }

    public void setMainController(MainViewController mainController, TransactionService service) {
        this.mainController = mainController;
        this.service = service;
    }

    @FXML
    private void handleSave() {
        try {
            String type = cmbType.getValue();
            String category = txtCategory.getText();
            String amountStr = txtAmount.getText();
            LocalDate date = txtDate.getValue();

            if (type == null || category.isEmpty() || amountStr.isEmpty() || date == null) {
                System.out.println("Erro: Preencha todos os campos.");
                return;
            }

            BigDecimal amount = new BigDecimal(amountStr);
            Transaction transaction = new Transaction(date, type, category, amount);
            
            service.save(transaction);
            
            mainController.updateUI();
            closeStage();
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
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
