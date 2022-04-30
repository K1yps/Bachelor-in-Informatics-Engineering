package FileServerProtocol.Exceptions;

public class InvalidChunkException extends RuntimeException {

    public InvalidChunkException() {
    }

    public InvalidChunkException(String message) {
        super(message);
    }

    public InvalidChunkException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidChunkException(Throwable cause) {
        super(cause);
    }

    public InvalidChunkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
