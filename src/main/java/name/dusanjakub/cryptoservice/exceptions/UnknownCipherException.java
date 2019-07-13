package name.dusanjakub.cryptoservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such cipher")
public class UnknownCipherException extends RuntimeException {

    public UnknownCipherException() {
        super();
    }

    public UnknownCipherException(String message) {
        super(message);
    }
}
