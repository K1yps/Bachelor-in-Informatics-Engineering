package business.subRobos;

public class RoboInvalidoException extends Exception {

    public RoboInvalidoException() {
    }

    public RoboInvalidoException(String message) {
        super(message);
    }

    public RoboInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoboInvalidoException(Throwable cause) {
        super(cause);
    }

    public RoboInvalidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
