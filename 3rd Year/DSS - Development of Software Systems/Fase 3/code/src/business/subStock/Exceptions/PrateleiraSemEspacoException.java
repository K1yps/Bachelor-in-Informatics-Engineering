package business.subStock.Exceptions;

public class PrateleiraSemEspacoException extends Exception {
    public PrateleiraSemEspacoException() {
    }

    public PrateleiraSemEspacoException(String message) {
        super(message);
    }

    public PrateleiraSemEspacoException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrateleiraSemEspacoException(Throwable cause) {
        super(cause);
    }

    public PrateleiraSemEspacoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
