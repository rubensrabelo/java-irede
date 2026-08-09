package app.application.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.time.LocalDate;

import app.application.service.TransactionService;
import app.domain.Transaction;
import app.shared.exceptions.InvalidInputException;
import app.shared.exceptions.TransactionPersistenceException;

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

            if (type == null || category.trim().isEmpty() || amountStr.trim().isEmpty() || date == null) {
                throw new InvalidInputException("Todos os campos do formulário são de preenchimento obrigatório.");
            }

            String normalizedAmount = amountStr.replace(",", ".");
            BigDecimal amount;
            try {
                amount = new BigDecimal(normalizedAmount);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("O valor numérico digitado é inválido. Utilize apenas números e vírgula.");
            }

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
            
        } catch (InvalidInputException e) {
            showValidationError(e.getMessage());
        } catch (Exception e) {
            System.err.println("[LOG CRÍTICO REPOSITÓRIO]: " + e.getMessage());
            e.printStackTrace();
            showDatabaseErrorAlert("Não foi possível salvar o registro devido a uma instabilidade na comunicação com o servidor de dados.");
            throw new TransactionPersistenceException("Falha de persistência ao tentar salvar o registro da transação.", e);
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

    private void showValidationError(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Aviso de Validação");
        alert.setHeaderText("Verifique os campos digitados");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDatabaseErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro de Armazenamento");
        alert.setHeaderText("Falha na gravação dos dados");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
