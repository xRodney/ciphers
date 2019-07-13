package name.dusanjakub.cryptoservice.ciphers;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MorseCipherTest {

    private MorseCipher cipher = new MorseCipher();

    @Test
    public void encrypt() {
        assertEquals("Empty", "", cipher.encrypt(""));
        assertEquals("Null", "", cipher.encrypt(null));
        assertEquals("String 'bang' is encrypted",
                "-... .- -. --.", cipher.encrypt("bang"));
        assertEquals("Lower case alphabet",
                ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --..",
                cipher.encrypt("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("Upper case alphabet",
                ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --..",
                cipher.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("Numbers",
                "----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----.",
                cipher.encrypt("0123456789"));
        assertEquals("Invalid chars are skipped, case is irrelevant",
                "-... .- -. --. -... .- -. --.",
                cipher.encrypt("BaNg!!!Bang"));
        assertEquals("Spaces are kept",
                "--- -. .  - .-- ---  - .... .-. . .",
                cipher.encrypt("One, two, three"));
    }

    @Test
    public void decrypt() {
        assertEquals("Empty", "", cipher.decrypt(""));
        assertEquals("Null", "", cipher.decrypt(null));
        assertEquals("String 'bang' is encrypted",
                "BANG", cipher.decrypt("-... .- -. --."));
        assertEquals("Upper case alphabet",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ", cipher.decrypt(".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.."));
        assertEquals("Numbers",
                "0123456789", cipher.decrypt("----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----."));
        assertEquals("Invalid chars are skipped, case is always upper",
                "BANGBANG", cipher.decrypt("-... .- -. --. -... .- -. --."));
        assertEquals("Spaces are kept",
                "ONE TWO THREE", cipher.decrypt("--- -. .  - .-- ---  - .... .-. . ."));
        assertEquals("Invalid chars are delimiters",
                "ONE T", cipher.decrypt("---k-.x.rr-"));
        assertEquals("Illegal morse sequences are passed through", "ON.---.----", cipher.decrypt("--- -. .---.----"));
    }
}
