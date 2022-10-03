package br.com.moisesestevao.jogodavelha.controllers;

import br.com.moisesestevao.jogodavelha.controllers.dto.ConectarAoJogoRequest;
import br.com.moisesestevao.jogodavelha.models.Jogada;
import br.com.moisesestevao.jogodavelha.models.Jogador;
import br.com.moisesestevao.jogodavelha.models.Jogo;
import br.com.moisesestevao.jogodavelha.service.JogoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/jogo")
@Slf4j
public class JogoController {
    private final JogoService jogoService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/iniciar-jogo")
    public ResponseEntity<Jogo> iniciarNovoJogo(@RequestBody Jogador jogador){
        log.info("tentando iniciar um novo jogo para o jogador: " + jogador.getLogin());

        return ResponseEntity.ok(this.jogoService.criarJogo(jogador));
    }

    @PostMapping("/conectar-jogo")
    public ResponseEntity<Jogo> conectarAoJogo(@RequestBody ConectarAoJogoRequest request){
        log.info("tentando conectar o jogador '"
                + request.getJogador().getLogin()
                + "' ao jogo de id: '"
                + request.getIdJogo()+"'");

        return ResponseEntity.ok(this.jogoService.conectarAoJogo(request.getJogador(), request.getIdJogo()));
    }

    @PostMapping("/conectar-jogo/aleatorio")
    public ResponseEntity<Jogo> conectarAJogoAleatorio(@RequestBody Jogador jogador){
        log.info("tentando conectar o jogador '"
                + jogador.getLogin() + "' a um jogo aleatório");

        return ResponseEntity.ok(this.jogoService.conectarAJogoAleatorio(jogador));
    }

    @PostMapping("/executar-jogada")
    public ResponseEntity<Jogo> executarJogada(@RequestBody Jogada jogada){
        log.info("tentando executar jogada" + jogada.toString());
        Jogo jogo = this.jogoService.executarJogada(jogada);
        if(jogo == null){
            return ResponseEntity.internalServerError().build();
        }
        this.simpMessagingTemplate.convertAndSend("/topic/progresso-jogo/" + jogo.getId(), jogo);

        return ResponseEntity.ok(jogo);
    }
}
