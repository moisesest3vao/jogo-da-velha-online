package br.com.moisesestevao.jogodavelha.controllers.dto;

import br.com.moisesestevao.jogodavelha.models.Jogador;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConectarAoJogoRequest {
    private Jogador jogador;
    private String idJogo;
}
