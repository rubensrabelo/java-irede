package app.shared.exceptions;

public class CsvSerializationException extends RuntimeException {
    public CsvSerializationException(String message) {
        super(message);
    }

    public CsvSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
