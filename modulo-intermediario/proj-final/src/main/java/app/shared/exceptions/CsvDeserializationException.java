package app.shared.exceptions;

public class CsvDeserializationException extends RuntimeException {
    public CsvDeserializationException(String message) {
        super(message);
    }

    public CsvDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
