package name.dusanjakub.cryptoservice.controllers;

import name.dusanjakub.cryptoservice.services.CryptoService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;

public class CryptoControllerTest {

    @InjectMocks
    private CryptoController controller;

    @Mock
    private CryptoService serviceMock;

    @Mock
    private Model modelMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(serviceMock.encrypt("x", "text")).thenReturn("encrypted");
        Mockito.when(serviceMock.decrypt("x", "text")).thenReturn("decrypted");
    }

    @Test
    public void getIndex() {
        assertEquals("index", controller.getIndex());
    }

    @Test
    public void postIndex_encrypt() {
        assertEquals("index",
                controller.postIndex("x", "text", CryptoController.Action.ENCRYPT, modelMock));
        Mockito.verify(serviceMock).encrypt("x", "text");
        Mockito.verify(modelMock).addAttribute("text", "encrypted");
    }

    @Test
    public void postIndex_decrypt() {
        assertEquals("index",
                controller.postIndex("x", "text", CryptoController.Action.DECRYPT, modelMock));
        Mockito.verify(serviceMock).decrypt("x", "text");
        Mockito.verify(modelMock).addAttribute("text", "decrypted");
    }

    @Test
    public void encryptRest() {
        assertEquals("encrypted", controller.encryptRest("x", "text"));
        Mockito.verify(serviceMock).encrypt("x", "text");
        Mockito.verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void decryptRest() {
        assertEquals("decrypted", controller.decryptRest("x", "text"));
        Mockito.verify(serviceMock).decrypt("x", "text");
        Mockito.verifyNoMoreInteractions(serviceMock);
    }
}