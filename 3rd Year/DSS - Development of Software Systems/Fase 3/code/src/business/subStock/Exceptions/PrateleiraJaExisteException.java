package business.subStock.Exceptions;

public class PrateleiraJaExisteException extends Exception {
    public PrateleiraJaExisteException() {
    }

    public PrateleiraJaExisteException(String message) {
        super(message);
    }

    public PrateleiraJaExisteException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrateleiraJaExisteException(Throwable cause) {
        super(cause);
    }

    public PrateleiraJaExisteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
