package name.dusanjakub.cryptoservice.cyphers;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MorseCypherTest {

    private MorseCypher cypher = new MorseCypher();

    @Test
    public void encrypt() {
        assertEquals("Empty", "", cypher.encrypt(""));
        assertEquals("Null", "", cypher.encrypt(null));
        assertEquals("String 'bang' is encrypted",
                "-... .- -. --.", cypher.encrypt("bang"));
        assertEquals("Lower case alphabet",
                ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --..",
                cypher.encrypt("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("Upper case alphabet",
                ".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --..",
                cypher.encrypt("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("Numbers",
                "----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----.",
                cypher.encrypt("0123456789"));
        assertEquals("Invalid chars are skipped, case is irrelevant",
                "-... .- -. --. -... .- -. --.",
                cypher.encrypt("BaNg!!!Bang"));
        assertEquals("Spaces are kept",
                "--- -. .  - .-- ---  - .... .-. . .",
                cypher.encrypt("One, two, three"));
    }

    @Test
    public void decrypt() {
        assertEquals("Empty", "", cypher.decrypt(""));
        assertEquals("Null", "", cypher.decrypt(null));
        assertEquals("String 'bang' is encrypted",
                "BANG", cypher.decrypt("-... .- -. --."));
        assertEquals("Upper case alphabet",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ", cypher.decrypt(".- -... -.-. -.. . ..-. --. .... .. .--- -.- .-.. -- -. --- .--. --.- .-. ... - ..- ...- .-- -..- -.-- --.."));
        assertEquals("Numbers",
                "0123456789", cypher.decrypt("----- .---- ..--- ...-- ....- ..... -.... --... ---.. ----."));
        assertEquals("Invalid chars are skipped, case is always upper",
                "BANGBANG", cypher.decrypt("-... .- -. --. -... .- -. --."));
        assertEquals("Spaces are kept",
                "ONE TWO THREE", cypher.decrypt("--- -. .  - .-- ---  - .... .-. . ."));
        assertEquals("Invalid chars are delimiters",
                "ONE T", cypher.decrypt("---k-.x.rr-"));
        assertEquals("Illegal morse sequences are passed through", "ON.---.----", cypher.decrypt("--- -. .---.----"));
    }
}
