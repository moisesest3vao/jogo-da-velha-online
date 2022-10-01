package br.com.moisesestevao.jogodavelha.service;

import br.com.moisesestevao.jogodavelha.models.Jogada;
import br.com.moisesestevao.jogodavelha.models.Jogador;
import br.com.moisesestevao.jogodavelha.models.Jogo;
import br.com.moisesestevao.jogodavelha.models.enums.Marcador;
import br.com.moisesestevao.jogodavelha.models.enums.StatusJogo;
import br.com.moisesestevao.jogodavelha.storage.JogoStorage;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class JogoService {

    public Jogo criarJogo(Jogador jogador){
        Jogo jogo = new Jogo();
        jogo.setId(UUID.randomUUID().toString());
        jogo.setJogador1(jogador);
        jogo.setQuadro(new int [3][3]);
        jogo.setStatus(StatusJogo.NOVO);

        JogoStorage.getInstancia().addJogo(jogo);

        return jogo;
    }

    public Jogo conectarAoJogo(Jogador jogador2, String id){
        Map<String, Jogo> jogosEmExecucao = JogoStorage.getInstancia().getJogos();
        if(!jogosEmExecucao.containsKey(id)){
            throw new RuntimeException("O jogo com esse id não existe");
        }

        Jogo jogo = jogosEmExecucao.get(id);
        if(jogo.getJogador2()!= null){
            throw new RuntimeException("O jogo com esse id já está cheio");
        }

        jogo.setJogador2(jogador2);
        jogo.setStatus(StatusJogo.EM_PROGRESSO);
        JogoStorage.getInstancia().addJogo(jogo);

        return jogo;
    }

    public Jogo conectarAJogoAleatorio(Jogador jogador2){
        Jogo jogoAleatorio = JogoStorage.getInstancia().getJogos().values().stream()
                .filter(jogo -> jogo.getStatus().equals(StatusJogo.NOVO))
                    .findAny().orElseThrow(() -> new RuntimeException("Não há nenhum jogo disponível no momento"));
        jogoAleatorio.setJogador2(jogador2);
        jogoAleatorio.setStatus(StatusJogo.EM_PROGRESSO);

        JogoStorage.getInstancia().addJogo(jogoAleatorio);

        return jogoAleatorio;
    }

    public Jogo executarJogada(Jogada jogada){
        Map<String, Jogo> jogos = JogoStorage.getInstancia().getJogos();
        if(!jogos.containsKey(jogada.getIdJogo())){
            throw new RuntimeException("O jogo com esse id não existe");
        }

        Jogo jogo = jogos.get(jogada.getIdJogo());
        if(jogo.getStatus().equals(StatusJogo.TERMINADO)){
            throw new RuntimeException("O jogo já foi terminado");
        }

        int[][] quadro = jogo.getQuadro();
        quadro[jogada.getCoordenadaX()][jogada.getCoordenadaY()] = jogada.getMarcador().getValor();

        boolean isOVencedor = verificarVencedor(jogo.getQuadro(), Marcador.O);
        boolean isXVencedor = verificarVencedor(jogo.getQuadro(), Marcador.X);
        boolean deuVelha = verificarSeDeuVelha();
        if(isXVencedor){
            jogo.setStatus(StatusJogo.TERMINADO);
            jogo.setVencedor(Marcador.X);
        } else if (isOVencedor){
            jogo.setStatus(StatusJogo.TERMINADO);
            jogo.setVencedor(Marcador.O);
        }

        JogoStorage.getInstancia().addJogo(jogo);
        return jogo;
    }

    private boolean verificarSeDeuVelha() {
        return false;
    }

    private boolean verificarVencedor(int[][] quadro, Marcador marcador) {
        int[] arrayQuadro = new int[9];
        int index = 0;
        for(int i = 0; i < quadro.length; i++){
            for(int j = 0; j < quadro[i].length; j++){
                arrayQuadro[index] = quadro[i][j];
                index++;
            }
        }

        int[][] combinacoes = {{0,1,2},{3,4,5},{6,7,8},{0,3,6}, {1,4,7}, {2,5,8},{0,4,8},{2,4,6}};

        for(int i = 0; i < combinacoes.length; i++){
            int counter = 0;
            for(int j = 0; j < combinacoes[i].length; j++){
                if(arrayQuadro[combinacoes[i][j]] == marcador.getValor()){
                    counter++;

                    if(counter == 3){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
