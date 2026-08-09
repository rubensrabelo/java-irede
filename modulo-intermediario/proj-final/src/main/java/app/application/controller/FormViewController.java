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

import app.application.dto.TransactionRequestDTO;
import app.application.dto.TransactionResponseDTO;
import app.application.service.TransactionService;
import app.shared.exceptions.InvalidInputException;
import app.shared.exceptions.TransactionPersistenceException;

public class FormViewController {

    @FXML private ComboBox<String> cmbType;
    @FXML private TextField txtCategory;
    @FXML private TextField txtAmount;
    @FXML private DatePicker txtDate;

    private MainViewController mainController;
    private final TransactionService service;
    private TransactionResponseDTO editingTransaction;

    public FormViewController(TransactionService service) {
        this.service = service;
    }

    @FXML
    public void initialize() {
        cmbType.setItems(FXCollections.observableArrayList("Entrada", "Saída"));
        txtDate.setValue(LocalDate.now());
    }

    public void setMainController(MainViewController mainController, TransactionResponseDTO transactionToEdit) {
        this.mainController = mainController;
        this.editingTransaction = transactionToEdit;

        if (transactionToEdit != null) {
            cmbType.setValue(transactionToEdit.typeDescription());
            txtCategory.setText(transactionToEdit.category());
            txtAmount.setText(transactionToEdit.amount().toString().replace(".", ","));
            txtDate.setValue(transactionToEdit.date());
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

            TransactionRequestDTO requestDto = new TransactionRequestDTO(date, type, category, amount);

            if (editingTransaction != null) {
                service.update(editingTransaction.id(), requestDto);
            } else {
                service.save(requestDto);
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
