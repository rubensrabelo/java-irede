package app.shared.exceptions;

public class BatchPersistenceException extends RuntimeException {
    public BatchPersistenceException(String message) {
        super(message);
    }

    public BatchPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
