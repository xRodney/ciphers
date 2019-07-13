package name.dusanjakub.cryptoservice.ciphers;

/**
 * Cipher interface
 */
public interface Cipher {
    /**
     * Encrypt the text according to the cipher
     * @param text The text
     * @return Encrypted text
     */
    String encrypt(String text);

    /**
     * Decrypt the text according to the cipher
     * @param text Encrypted text
     * @return Decrypted text
     */
    String decrypt(String text);
}
