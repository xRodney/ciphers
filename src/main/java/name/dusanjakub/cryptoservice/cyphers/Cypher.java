package name.dusanjakub.cryptoservice.cyphers;

/**
 * Cypher interface
 */
public interface Cypher {
    /**
     * Encrypt the text according to the cypher
     * @param text The text
     * @return Encrypted text
     */
    String encrypt(String text);

    /**
     * Decrypt the text according to the cypher
     * @param text Encrypted text
     * @return Decrypted text
     */
    String decrypt(String text);
}
