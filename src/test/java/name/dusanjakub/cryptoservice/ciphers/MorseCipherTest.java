package name.dusanjakub.cryptoservice.ciphers;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MorseCipherTest {

    private MorseCipher cipher = new MorseCipher();

    @Test
    public void encode() {
        assertEquals("Empty", "", cipher.encode(""));
        assertEquals("Null", "", cipher.encode(null));
        assertEquals("String 'bang' is encoded",
                "-... .- -. --.", cipher.encode("bang"));
        assertEquals("Lower case alphabet",
                ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --..",
                cipher.encode("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("Upper case alphabet",
                ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --..",
                cipher.encode("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("Numbers",
                "----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----.",
                cipher.encode("0123456789"));
        assertEquals("Invalid chars are skipped, case is irrelevant",
                "-... .- -. --. -... .- -. --.",
                cipher.encode("BaNg!!!Bang"));
        assertEquals("Spaces are kept",
                "--- -. .  - .-- ---  - .... .-. . .",
                cipher.encode("One, two, three"));
    }

    @Test
    public void decode() {
        assertEquals("Empty", "", cipher.decode(""));
        assertEquals("Null", "", cipher.decode(null));
        assertEquals("String 'bang' is decoded",
                "BANG", cipher.decode("-... .- -. --."));
        assertEquals("Upper case alphabet",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ", cipher.decode(".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.."));
        assertEquals("Numbers",
                "0123456789", cipher.decode("----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----."));
        assertEquals("Invalid chars are skipped, case is always upper",
                "BANGBANG", cipher.decode("-... .- -. --. -... .- -. --."));
        assertEquals("Spaces are kept",
                "ONE TWO THREE", cipher.decode("--- -. .  - .-- ---  - .... .-. . ."));
        assertEquals("Invalid chars are delimiters",
                "ONE T", cipher.decode("---k-.x.rr-"));
        assertEquals("Illegal morse sequences are passed through", "ON.---.----", cipher.decode("--- -. .---.----"));
    }
}
