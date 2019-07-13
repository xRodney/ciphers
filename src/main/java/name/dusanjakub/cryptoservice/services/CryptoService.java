package name.dusanjakub.cryptoservice.services;

import name.dusanjakub.cryptoservice.ciphers.Cipher;
import name.dusanjakub.cryptoservice.exceptions.UnknownCipherException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CryptoService {

    private final Map<String, Cipher> ciphers;

    public CryptoService(Map<String, Cipher> ciphers) {
        this.ciphers = ciphers;
    }

    public String encrypt(String cipher, String text) {
        return getCipherByName(cipher).encrypt(text);
    }

    public String decrypt(String cipher, String text) {
        return getCipherByName(cipher).decrypt(text);
    }

    private Cipher getCipherByName(String name) {
        return ciphers.computeIfAbsent(name, (n) -> {
            throw new UnknownCipherException(n);
        });
    }
}
