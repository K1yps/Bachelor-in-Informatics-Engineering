package business.subStock.Exceptions;

public class EstadoPaleteInvalidoException extends Exception {
    public EstadoPaleteInvalidoException() {
    }

    public EstadoPaleteInvalidoException(String message) {
        super(message);
    }

    public EstadoPaleteInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public EstadoPaleteInvalidoException(Throwable cause) {
        super(cause);
    }

    public EstadoPaleteInvalidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
