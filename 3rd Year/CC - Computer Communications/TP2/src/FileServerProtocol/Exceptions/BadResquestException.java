package FileServerProtocol.Exceptions;

public class BadResquestException extends RuntimeException {

    public BadResquestException() {
    }

    public BadResquestException(String message) {
        super(message);
    }

    public BadResquestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadResquestException(Throwable cause) {
        super(cause);
    }

    public BadResquestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
