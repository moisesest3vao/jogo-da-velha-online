package br.com.moisesestevao.jogodavelha.models;

import br.com.moisesestevao.jogodavelha.models.enums.Marcador;
import br.com.moisesestevao.jogodavelha.models.enums.StatusJogo;
import br.com.moisesestevao.jogodavelha.models.enums.Turno;
import lombok.Data;

import java.util.UUID;

@Data
public class Jogo {
    private String id;
    Jogador jogador1;
    Jogador jogador2;
    StatusJogo status;
    private int [][] quadro;
    private Marcador vencedor;
    private Turno turno;

    public Jogo(Jogador jogador) {
        this.setId(UUID.randomUUID().toString());
        this.setJogador1(jogador);
        this.setQuadro(new int [3][3]);
        this.setStatus(StatusJogo.NOVO);
        this.setTurno(Turno.JOGADOR1);
    }
}
