package name.dusanjakub.cryptoservice.ciphers;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Map.Entry;
import static java.util.stream.Collectors.toMap;

/**
 * Implements the Morse code cipher
 */
@Service("morse")
public class MorseCipher implements Cipher {
    private static final Map<Character, String> toMorse = new HashMap<>();
    private static final Map<String, String> fromMorse;

    static {
        toMorse.put('A', ".-");
        toMorse.put('B', "-...");
        toMorse.put('C', "-.-.");
        toMorse.put('D', "-..");
        toMorse.put('E', ".");
        toMorse.put('F', "..-.");
        toMorse.put('G', "--.");
        toMorse.put('H', "....");
        toMorse.put('I', "..");
        toMorse.put('J', ".---");
        toMorse.put('K', "-.-");
        toMorse.put('L', ".-..");
        toMorse.put('M', "--");
        toMorse.put('N', "-.");
        toMorse.put('O', "---");
        toMorse.put('P', ".--.");
        toMorse.put('Q', "--.-");
        toMorse.put('R', ".-.");
        toMorse.put('S', "...");
        toMorse.put('T', "-");
        toMorse.put('U', "..-");
        toMorse.put('V', "...-");
        toMorse.put('W', ".--");
        toMorse.put('X', "-..-");
        toMorse.put('Y', "-.--");
        toMorse.put('Z', "--..");

        toMorse.put('1', ".----");
        toMorse.put('2', "..---");
        toMorse.put('3', "...--");
        toMorse.put('4', "....-");
        toMorse.put('5', ".....");
        toMorse.put('6', "-....");
        toMorse.put('7', "--...");
        toMorse.put('8', "---..");
        toMorse.put('9', "----.");
        toMorse.put('0', "-----");

        toMorse.put('.', ".-.-.-");
        toMorse.put('-', "-....-");

        toMorse.put(' ', "");

        fromMorse = toMorse.entrySet().stream().collect(toMap(Entry::getValue, v -> v.getKey().toString()));
    }

    @Override
    public String encode(String text) {
        return (text == null ? "" : text).chars()
                .mapToObj(c -> toMorse.get(Character.toUpperCase((char) c)))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

    @Override
    public String decode(String text) {
        return Pattern.compile("[^.-]")
                .splitAsStream(text == null ? "" : text)
                .map(x -> fromMorse.getOrDefault(x, x))
                .collect(Collectors.joining()).trim();
    }
}
