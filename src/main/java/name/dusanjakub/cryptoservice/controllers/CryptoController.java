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

    @GetMapping(path = "/")
    public String getIndex() {
        return "index";
    }

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
                case ENCRYPT:
                    result = service.encrypt(cipherName, text);
                    break;
                case DECRYPT:
                    result = service.decrypt(cipherName, text);
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

    @PostMapping(path = "{cipherName}/encrypt")
    @ResponseBody
    public String encryptRest(@PathVariable String cipherName, @RequestBody String text) {
        return service.encrypt(cipherName, text);
    }

    @PostMapping(path = "{cipherName}/decrypt")
    @ResponseBody
    public String decryptRest(@PathVariable String cipherName, @RequestBody String text) {
        return service.decrypt(cipherName, text);
    }

    enum Action {
        ENCRYPT, DECRYPT
    }
}
