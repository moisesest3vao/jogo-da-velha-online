package br.com.moisesestevao.jogodavelha.models;

import br.com.moisesestevao.jogodavelha.models.enums.Marcador;
import lombok.Data;

@Data
public class Jogada {
    private String idJogo;
    private Integer coordenadaX;
    private Integer coordenadaY;
    private Marcador marcador;
}
