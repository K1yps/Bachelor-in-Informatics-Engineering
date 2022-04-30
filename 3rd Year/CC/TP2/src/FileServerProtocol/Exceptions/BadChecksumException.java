package FileServerProtocol.Exceptions;

public class BadChecksumException extends RuntimeException {


    public BadChecksumException() {
    }

    public BadChecksumException(String message) {
        super(message);
    }

    public BadChecksumException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadChecksumException(Throwable cause) {
        super(cause);
    }

    public BadChecksumException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
