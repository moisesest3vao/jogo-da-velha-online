package br.com.moisesestevao.jogodavelha.storage;

import br.com.moisesestevao.jogodavelha.models.Jogo;

import java.util.HashMap;
import java.util.Map;

public class JogoStorage {

    private static Map<String, Jogo> jogos;
    private static JogoStorage instancia;
    private JogoStorage(){
        jogos = new HashMap<>();
    }

    public static synchronized JogoStorage getInstancia(){
        if(instancia == null){
            instancia = new JogoStorage();
        }
        return instancia;
    }

    public Map<String, Jogo> getJogos(){
        return jogos;
    }

    public void addJogo(Jogo jogo){
        jogos.put(jogo.getId(), jogo);
    }
}
