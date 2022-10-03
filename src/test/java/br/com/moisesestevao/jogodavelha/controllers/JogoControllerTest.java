package br.com.moisesestevao.jogodavelha.controllers;

import br.com.moisesestevao.jogodavelha.controllers.dto.ConectarAoJogoRequest;
import br.com.moisesestevao.jogodavelha.models.Jogada;
import br.com.moisesestevao.jogodavelha.models.Jogador;
import br.com.moisesestevao.jogodavelha.models.Jogo;
import static org.junit.jupiter.api.Assertions.*;

import br.com.moisesestevao.jogodavelha.models.enums.Marcador;
import br.com.moisesestevao.jogodavelha.models.enums.Turno;
import br.com.moisesestevao.jogodavelha.service.JogoService;
import br.com.moisesestevao.jogodavelha.storage.JogoStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class JogoControllerTest {
    private JogoController jogoController;
    private final JogoService jogoService = new JogoService();
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;
    Jogador jogador = new Jogador("emoises");
    Jogador jogador2 = new Jogador("ejosue");


    @BeforeEach
    private void initialize(){
        MockitoAnnotations.initMocks(this);

        this.jogoController = new JogoController(this.jogoService, this.simpMessagingTemplate);
        JogoStorage.apagarJogos();
    }

    @Test
    void deveriaCriarUmNovoJogo(){
        Jogo jogo = this.jogoController.iniciarNovoJogo(jogador).getBody();
        assertNotEquals(null, jogo);
        assertEquals(jogo.getJogador1(), jogador);
        assertTrue(JogoStorage.getInstancia().getJogos().containsKey(jogo.getId()));
    }

    @Test
    void deveriaConectarAJogoAleatorio(){
        Jogo jogoCriado = this.jogoController.iniciarNovoJogo(jogador).getBody();
        Jogo jogoConectado = this.jogoController.conectarAJogoAleatorio(jogador2).getBody();

        assertNotEquals(null, jogoConectado);
        assertEquals(jogoConectado.getJogador2(), jogador2);
        assertEquals(jogoConectado, jogoCriado);
        assertTrue(JogoStorage.getInstancia().getJogos().containsKey(jogoConectado.getId()));
    }

    @Test
    void deveriaConectarAJogo(){
        Jogo jogoCriado = this.jogoController.iniciarNovoJogo(jogador).getBody();
        assertNotEquals(null, jogoCriado);

        Jogo jogoConectado = this.jogoController
                .conectarAoJogo(new ConectarAoJogoRequest(jogador2, jogoCriado.getId())).getBody();

        assertNotEquals(null, jogoConectado);
        assertEquals(jogoConectado.getJogador2(), jogador2);
        assertEquals(jogoConectado, jogoCriado);
        assertTrue(JogoStorage.getInstancia().getJogos().containsKey(jogoConectado.getId()));
    }

    @Test
    void deveriaExecutarPrimeiraJogadaEBloquearASegundaJogada(){
        Mockito.doNothing().when(simpMessagingTemplate).convertAndSend(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object.class));

        Jogo jogoCriado = this.jogoController.iniciarNovoJogo(jogador).getBody();
        assertNotEquals(null, jogoCriado);

        Jogada jogada = new Jogada(jogoCriado.getId(), 0,0, Marcador.X);
        Jogada jogada2 = new Jogada(jogoCriado.getId(), 1,1, Marcador.X);
        Jogo jogoResponseJogada = this.jogoController.executarJogada(jogada).getBody();
        Jogo jogoResponseJogada2 = this.jogoController.executarJogada(jogada2).getBody();

        assertNotNull(jogoCriado);
        assertNotNull(jogoResponseJogada);
        assertNull(jogoResponseJogada2);
        assertEquals(Turno.JOGADOR2, jogoResponseJogada.getTurno());
        assertEquals(1, jogoResponseJogada.getQuadro()[0][0]);
        assertEquals(0, jogoResponseJogada.getQuadro()[1][1]);
    }
}
