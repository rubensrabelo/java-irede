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
    private final TransactionService service;
    private Transaction editingTransaction;

    public FormViewController(TransactionService service) {
        this.service = service;
    }

    @FXML
    public void initialize() {
        cmbType.setItems(FXCollections.observableArrayList("Entrada", "Saída"));
        txtDate.setValue(LocalDate.now());
    }

    public void setMainController(MainViewController mainController, Transaction transactionToEdit) {
        this.mainController = mainController;
        this.editingTransaction = transactionToEdit;

        if (transactionToEdit != null) {
            cmbType.setValue(transactionToEdit.getType().getDescription());
            txtCategory.setText(transactionToEdit.getCategory());
            txtAmount.setText(transactionToEdit.getAmount().toString().replace(".", ","));
            txtDate.setValue(transactionToEdit.getDate());
        }
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

            String normalizedAmount = amountStr.replace(",", ".");
            BigDecimal amount = new BigDecimal(normalizedAmount);

            if (editingTransaction != null) {
                editingTransaction.setDate(date);
                editingTransaction.setCategory(category);
                editingTransaction.setAmount(amount);
                editingTransaction.setType(type);
                
                service.update(editingTransaction);
            } else {
                Transaction transaction = new Transaction(date, type, category, amount);
                service.save(transaction);
            }
            
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
