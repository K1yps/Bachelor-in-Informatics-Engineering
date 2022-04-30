package business.subStock.Exceptions;

public class ZonaInvalidaException extends Exception {
    public ZonaInvalidaException() {
    }

    public ZonaInvalidaException(String message) {
        super(message);
    }

    public ZonaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZonaInvalidaException(Throwable cause) {
        super(cause);
    }

    public ZonaInvalidaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
