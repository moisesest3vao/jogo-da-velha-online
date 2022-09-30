package br.com.moisesestevao.jogodavelha.models;

import br.com.moisesestevao.jogodavelha.models.enums.Marcador;
import br.com.moisesestevao.jogodavelha.models.enums.StatusJogo;
import lombok.Data;

@Data
public class Jogo {

    private String id;
    Jogador jogador1;
    Jogador jogador2;
    StatusJogo status;
    private int [][] quadro;
    private Marcador vencedor;
}
