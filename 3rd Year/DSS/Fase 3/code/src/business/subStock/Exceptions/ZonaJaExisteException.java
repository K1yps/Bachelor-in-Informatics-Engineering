package business.subStock.Exceptions;

public class ZonaJaExisteException extends Exception {

    public ZonaJaExisteException() {
    }

    public ZonaJaExisteException(String message) {
        super(message);
    }

    public ZonaJaExisteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZonaJaExisteException(Throwable cause) {
        super(cause);
    }

    public ZonaJaExisteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
