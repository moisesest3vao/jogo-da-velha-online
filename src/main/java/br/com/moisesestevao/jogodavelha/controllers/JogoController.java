package br.com.moisesestevao.jogodavelha.controllers;

import br.com.moisesestevao.jogodavelha.service.JogoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class JogoController {

    private final JogoService jogoService;

    @PostMapping("/iniciar-jogo")
    public ResponseEntity<?> iniciarNovoJogo(){

    }

}
