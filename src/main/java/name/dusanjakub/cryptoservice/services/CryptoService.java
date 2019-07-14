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

    /**
     * Encode the text using the specified cipher
     *
     * @param cipher The cipher to use
     * @param text   The plain text
     * @return Encoded text
     * @throws UnknownCipherException When the cipher name is not recognized
     */
    public String encode(String cipher, String text) {
        return getCipherByName(cipher).encode(text);
    }

    /**
     * Decode the text using the specified cipher
     *
     * @param cipher The cipher to use
     * @param text   The encoded text
     * @return Decoded text
     * @throws UnknownCipherException When the cipher name is not recognized
     */
    public String decode(String cipher, String text) {
        return getCipherByName(cipher).decode(text);
    }

    /**
     * Lookup the cipher implementation to use
     *
     * @param name Cipher name
     * @return Cipher implementation
     * @throws UnknownCipherException When the cipher name is not recognized
     */
    private Cipher getCipherByName(String name) {
        return ciphers.computeIfAbsent(name, (n) -> {
            throw new UnknownCipherException(n);
        });
    }
}
