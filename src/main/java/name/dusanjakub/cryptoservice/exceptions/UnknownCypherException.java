package name.dusanjakub.cryptoservice.exceptions;

public class UnknownCypherException extends RuntimeException {
    public UnknownCypherException() {
        super();
    }

    public UnknownCypherException(String message) {
        super(message);
    }
}
