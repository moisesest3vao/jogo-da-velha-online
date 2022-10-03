package br.com.moisesestevao.jogodavelha.service;

import br.com.moisesestevao.jogodavelha.models.Jogada;
import br.com.moisesestevao.jogodavelha.models.Jogador;
import br.com.moisesestevao.jogodavelha.models.Jogo;
import br.com.moisesestevao.jogodavelha.models.enums.Marcador;
import br.com.moisesestevao.jogodavelha.models.enums.StatusJogo;
import br.com.moisesestevao.jogodavelha.models.enums.Turno;
import br.com.moisesestevao.jogodavelha.storage.JogoStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@Slf4j
public class JogoService {

    public Jogo criarJogo(Jogador jogador){
        Jogo jogo = new Jogo(jogador);
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

    public boolean isJogadaValida(Jogada jogada, Map<String, Jogo> jogos){
        if(!jogos.containsKey(jogada.getIdJogo())){
            log.error("O jogo com o id "+jogada.getIdJogo()+" não existe");
            return false;
        }

        Jogo jogo = jogos.get(jogada.getIdJogo());
        if(jogo.getStatus().equals(StatusJogo.TERMINADO)){
            log.error("O jogo já foi terminado");
            return false;
        }

        Turno turno = jogo.getTurno();
        Marcador marcador = jogada.getMarcador();

        if(turno == Turno.JOGADOR1){
            if(marcador != Marcador.X){
                log.error("Não é sua vez de jogar");
                return false;
            }
        } else if(turno == Turno.JOGADOR2){
            if(marcador != Marcador.O){
                log.error("Não é sua vez de jogar");
                return false;
            }
        }

        return true;
    }

    public Jogo executarJogada(Jogada jogada){
        Map<String, Jogo> jogos = JogoStorage.getInstancia().getJogos();
        boolean jogadaValida = isJogadaValida(jogada, jogos);
        if(!jogadaValida){
            return null;
        }
        Jogo jogo = jogos.get(jogada.getIdJogo());

        int[][] quadro = jogo.getQuadro();
        quadro[jogada.getCoordenadaX()][jogada.getCoordenadaY()] = jogada.getMarcador().getValor();

        boolean isOVencedor = verificarVencedor(jogo.getQuadro(), Marcador.O);
        boolean isXVencedor = verificarVencedor(jogo.getQuadro(), Marcador.X);
        boolean isVelha = verificarVelha(jogo.getQuadro());

        if(isXVencedor){
            jogo.setStatus(StatusJogo.TERMINADO);
            jogo.setVencedor(Marcador.X);
        } else if (isOVencedor){
            jogo.setStatus(StatusJogo.TERMINADO);
            jogo.setVencedor(Marcador.O);
        } else if (isVelha){
            jogo.setStatus(StatusJogo.TERMINADO);
            jogo.setVencedor(Marcador.VELHA);
        }
        jogo = alternarTurno(jogo);

        JogoStorage.getInstancia().addJogo(jogo);
        return jogo;
    }

    private Jogo alternarTurno(Jogo jogo) {
        if(jogo.getTurno() == Turno.JOGADOR1){
            jogo.setTurno(Turno.JOGADOR2);
        } else if(jogo.getTurno() == Turno.JOGADOR2){
            jogo.setTurno(Turno.JOGADOR1);
        }
        return jogo;
    }

    private boolean verificarVelha(int[][] quadro) {
        boolean isCamposPreenchidos = true;
        for (int i = 0; i < quadro.length; i++ ){
            for (int j = 0; j < quadro[i].length; j++ ){
                if(quadro[i][j] != 1 && quadro[i][j] != 2){
                    return false;
                }
            }
        }

        return true;
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
