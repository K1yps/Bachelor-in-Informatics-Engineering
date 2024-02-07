package business.subStaff;

public class GestorInvalidoException extends Exception {

    public GestorInvalidoException() {
    }

    public GestorInvalidoException(String message) {
        super(message);
    }

    public GestorInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public GestorInvalidoException(Throwable cause) {
        super(cause);
    }

    public GestorInvalidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
