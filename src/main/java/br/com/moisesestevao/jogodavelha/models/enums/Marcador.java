package br.com.moisesestevao.jogodavelha.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Marcador {
    X(1), O(2), VELHA(9);

    private Integer valor;
}
