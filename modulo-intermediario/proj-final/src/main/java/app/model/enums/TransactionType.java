package app.model.enums;

public enum TransactionType {
    INCOME("Entrada"),
    OUTCOME("Saída");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static TransactionType fromDescription(String text) {
        for (TransactionType type : TransactionType.values()) {
            if (type.description.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de transação inválido: " + text);
    }
}
