package business.subStock.Exceptions;

public class PrateleiraInvalidaException extends Exception {

    public PrateleiraInvalidaException() {
    }

    public PrateleiraInvalidaException(String message) {
        super(message);
    }

    public PrateleiraInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrateleiraInvalidaException(Throwable cause) {
        super(cause);
    }

    public PrateleiraInvalidaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
