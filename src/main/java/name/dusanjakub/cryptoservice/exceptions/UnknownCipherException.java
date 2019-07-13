package name.dusanjakub.cryptoservice.exceptions;

public class UnknownCipherException extends RuntimeException {
    public UnknownCipherException() {
        super();
    }

    public UnknownCipherException(String message) {
        super(message);
    }
}
