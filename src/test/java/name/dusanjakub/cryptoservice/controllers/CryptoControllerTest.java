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
        Mockito.when(serviceMock.encode("x", "text")).thenReturn("encoded");
        Mockito.when(serviceMock.decode("x", "text")).thenReturn("decoded");
    }

    @Test
    public void getIndex() {
        assertEquals("index", controller.getIndex());
    }

    @Test
    public void postIndex_encode() {
        assertEquals("index",
                controller.postIndex("x", "text", CryptoController.Action.ENCODE, modelMock));
        Mockito.verify(serviceMock).encode("x", "text");
        Mockito.verify(modelMock).addAttribute("text", "encoded");
    }

    @Test
    public void postIndex_decode() {
        assertEquals("index",
                controller.postIndex("x", "text", CryptoController.Action.DECODE, modelMock));
        Mockito.verify(serviceMock).decode("x", "text");
        Mockito.verify(modelMock).addAttribute("text", "decoded");
    }

    @Test
    public void encodeRest() {
        assertEquals("encoded", controller.encodeRest("x", "text"));
        Mockito.verify(serviceMock).encode("x", "text");
        Mockito.verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void decodeRest() {
        assertEquals("decoded", controller.decodeRest("x", "text"));
        Mockito.verify(serviceMock).decode("x", "text");
        Mockito.verifyNoMoreInteractions(serviceMock);
    }
}