package name.dusanjakub.cryptoservice.controllers;

import name.dusanjakub.cryptoservice.exceptions.UnknownCipherException;
import name.dusanjakub.cryptoservice.services.CryptoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CryptoController {
    private CryptoService service;

    public CryptoController(CryptoService service) {
        this.service = service;
    }

    /**
     * The initial page load
     */
    @GetMapping(path = "/")
    public String getIndex() {
        return "index";
    }

    /**
     * The main encoding and decoding action
     *
     * @param cipherName The cipher
     * @param text       The test to encode or decode
     * @param action     Encode / decode
     */
    @PostMapping(path = "/")
    public String postIndex(@RequestParam("cipher") String cipherName,
                            @RequestParam("text") String text,
                            @RequestParam("action") Action action,
                            Model model) {

        model.addAttribute("cipher", cipherName);
        model.addAttribute("text", text);

        try {
            String result;
            switch (action) {
                case ENCODE:
                    result = service.encode(cipherName, text);
                    break;
                case DECODE:
                    result = service.decode(cipherName, text);
                    break;
                default:
                    throw new IllegalArgumentException(action.toString());
            }
            model.addAttribute("text", result);
        } catch (UnknownCipherException ex) {
            model.addAttribute("cipherError", "Cipher is not supported: " + cipherName);
        }
        return "index";
    }

    /**
     * Encode the text via REST
     *
     * @param cipherName The cipher to use
     * @param text       The text
     * @return Encoded text
     */
    @PostMapping(path = "{cipherName}/encode")
    @ResponseBody
    public String encodeRest(@PathVariable String cipherName, @RequestBody String text) {
        return service.encode(cipherName, text);
    }

    /**
     * Decode the text via REST
     *
     * @param cipherName The cipher to use
     * @param text       The text
     * @return Decoded text
     */
    @PostMapping(path = "{cipherName}/decode")
    @ResponseBody
    public String decodeRest(@PathVariable String cipherName, @RequestBody String text) {
        return service.decode(cipherName, text);
    }

    enum Action {
        ENCODE, DECODE
    }
}
