package app.exceptions;

public class VisualRenderingException extends RuntimeException {
    public VisualRenderingException(String message) {
        super(message);
    }
    public VisualRenderingException(String message, Throwable cause) {
        super(message, cause);
    }
}
