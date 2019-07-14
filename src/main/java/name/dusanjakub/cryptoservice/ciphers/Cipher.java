package name.dusanjakub.cryptoservice.ciphers;

/**
 * Cipher interface
 */
public interface Cipher {
    /**
     * Encode the text according to the cipher
     * @param text The text
     * @return Encoded text
     */
    String encode(String text);

    /**
     * Decode the text according to the cipher
     * @param text Encoded text
     * @return Decoded text
     */
    String decode(String text);
}
