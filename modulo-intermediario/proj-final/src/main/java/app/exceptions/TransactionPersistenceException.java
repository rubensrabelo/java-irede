package app.exceptions;

public class TransactionPersistenceException extends RuntimeException {
    public TransactionPersistenceException(String message) {
        super(message);
    }
    public TransactionPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
