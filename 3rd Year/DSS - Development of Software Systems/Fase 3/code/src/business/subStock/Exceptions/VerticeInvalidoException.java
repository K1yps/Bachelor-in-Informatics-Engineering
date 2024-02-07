package business.subStock.Exceptions;

public class VerticeInvalidoException extends Exception {
    public VerticeInvalidoException() {
    }

    public VerticeInvalidoException(String message) {
        super(message);
    }

    public VerticeInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerticeInvalidoException(Throwable cause) {
        super(cause);
    }

    public VerticeInvalidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
