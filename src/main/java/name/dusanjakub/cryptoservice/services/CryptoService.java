package name.dusanjakub.cryptoservice.services;

import name.dusanjakub.cryptoservice.cyphers.Cypher;
import name.dusanjakub.cryptoservice.exceptions.UnknownCypherException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CryptoService {

    private final Map<String, Cypher> cyphers;

    public CryptoService(Map<String, Cypher> cyphers) {
        this.cyphers = cyphers;
    }

    public String encrypt(String cypher, String text) {
        return getCypherByName(cypher).encrypt(text);
    }

    public String decrypt(String cypher, String text) {
        return getCypherByName(cypher).decrypt(text);
    }

    private Cypher getCypherByName(String name) {
        return cyphers.computeIfAbsent(name, (n) -> { throw new UnknownCypherException(n); });
    }
}
