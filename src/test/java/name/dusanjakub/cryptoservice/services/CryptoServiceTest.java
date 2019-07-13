package name.dusanjakub.cryptoservice.services;

import name.dusanjakub.cryptoservice.ciphers.Cipher;
import name.dusanjakub.cryptoservice.exceptions.UnknownCipherException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
public class CryptoServiceTest {

    @Autowired
    private CryptoService service;

    @Test
    public void whenValidCipher_thenEncrypt() {
        Assert.assertEquals("OSTRAVA !!!", service.encrypt("bang", "OSTRAVA"));
    }

    @Test(expected = UnknownCipherException.class)
    public void whenInvalidCipherInEncrypt_thenThrow() {
        service.encrypt("bla", "");
    }

    @Test
    public void whenValidCipher_thenDecrypt() {
        Assert.assertEquals("OSTRAVA", service.decrypt("bang", "OSTRAVA !!!"));
    }

    @Test(expected = UnknownCipherException.class)
    public void whenInvalidCipherInDecrypt_thenThrow() {
        service.decrypt("bla", "");
    }

    static class TestCipher implements Cipher {

        private static final String BANG = " !!!";
        private static final int BANG_LENGTH = BANG.length();

        @Override
        public String encrypt(String text) {
            return text + BANG;
        }

        @Override
        public String decrypt(String text) {
            return text.length() > BANG_LENGTH ? text.substring(0, text.length() - BANG_LENGTH) : "";
        }
    }

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public CryptoService cryptoService(Map<String, Cipher> ciphers) {
            return new CryptoService(ciphers);
        }

        @Bean("bang")
        public Cipher testCipher() {
            return new TestCipher();
        }
    }
}