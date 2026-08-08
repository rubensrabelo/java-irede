package app.exceptions;

public class FactoryInstantiationException extends RuntimeException {
    public FactoryInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
