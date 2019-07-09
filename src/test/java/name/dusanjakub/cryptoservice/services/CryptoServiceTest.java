package name.dusanjakub.cryptoservice.services;

import name.dusanjakub.cryptoservice.cyphers.Cypher;
import name.dusanjakub.cryptoservice.exceptions.UnknownCypherException;
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
    public void whenValidCypher_thenEncrypt() {
        Assert.assertEquals("OSTRAVA !!!", service.encrypt("bang", "OSTRAVA"));
    }

    @Test(expected = UnknownCypherException.class)
    public void whenInvalidCypherInEncrypt_thenThrow() {
        service.encrypt("bla", "");
    }

    @Test
    public void whenValidCypher_thenDecrypt() {
        Assert.assertEquals("OSTRAVA", service.decrypt("bang", "OSTRAVA !!!"));
    }

    @Test(expected = UnknownCypherException.class)
    public void whenInvalidCypherInDecrypt_thenThrow() {
        service.decrypt("bla", "");
    }

    static class TestCypher implements Cypher {

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
        public CryptoService cryptoService(Map<String, Cypher> cyphers) {
            return new CryptoService(cyphers);
        }

        @Bean("bang")
        public Cypher testCypher() {
            return new TestCypher();
        }
    }
}