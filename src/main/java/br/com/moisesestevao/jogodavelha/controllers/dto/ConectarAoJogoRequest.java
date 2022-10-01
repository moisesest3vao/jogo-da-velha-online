package br.com.moisesestevao.jogodavelha.controllers.dto;

import br.com.moisesestevao.jogodavelha.models.Jogador;
import lombok.Data;

@Data
public class ConectarAoJogoRequest {
    private Jogador jogador;
    private String idJogo;
}
