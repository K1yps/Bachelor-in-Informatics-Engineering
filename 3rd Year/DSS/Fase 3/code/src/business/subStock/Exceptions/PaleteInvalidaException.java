package business.subStock.Exceptions;

public class PaleteInvalidaException extends Exception {

    public PaleteInvalidaException() {
    }

    public PaleteInvalidaException(String message) {
        super(message);
    }

    public PaleteInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaleteInvalidaException(Throwable cause) {
        super(cause);
    }

    public PaleteInvalidaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
