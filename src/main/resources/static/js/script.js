var quadro = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
var jogada = "";
var jogoIniciou = false;
var isCriadorDaPartida = false;
var alertouConexao = false;

function turnoDoJogador(turn, id) {
    if (jogoIniciou) {
        var spotTaken = $("#" + id).text();
        if (spotTaken != "O" && spotTaken != "X") {
            executarJogada(marcador, id.split("_")[0], id.split("_")[1]);
        }
    }
}

function executarJogada(marcador, coordenadaX, coordenadaY) {
    $.ajax({
        url: url + "/jogo/executar-jogada",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "marcador": marcador,
            "coordenadaX": coordenadaX,
            "coordenadaY": coordenadaY,
            "idJogo": idJogo
        }),
        success: function (data) {
            jogoIniciou = false;
            exibirResposta(data);
        },
        error: function (xhr, textStatus) {
            let statusCode = xhr.status;
            if(statusCode == 500){
                alert("Aguarde sua vez de jogar!");
            }            
        }
    })
}

function exibirResposta(data) {
    let quadro = data.quadro;
    for (let i = 0; i < quadro.length; i++) {
        for (let j = 0; j < quadro[i].length; j++) {
            if (quadro[i][j] === 1) {
                quadro[i][j] = 'X'
            } else if (quadro[i][j] === 2) {
                quadro[i][j] = 'O';
            } else if (quadro[i][j] === 0){
                quadro[i][j] = ' ';
            }
            let id = i + "_" + j;
            $("#" + id).text(quadro[i][j]);
        }
    }

    let vencedor = data.vencedor;

    if (vencedor != null) {
        if (vencedor == marcador) {
            alert("Você venceu!");
        } else if(vencedor == "VELHA" ) {
            alert("Deu velha!");
        } else {
            alert("O seu oponente venceu...");
        }

        window.location.reload();
    }

    if(isCriadorDaPartida == true && alertouConexao == false){
        let oponente = data.jogador2;

        if(oponente != null){
            alertarConexaoParaUsuario(oponente.login);
            alertouConexao = true;
        }
    }
    


    jogoIniciou = true;
}

function alertarConexaoParaUsuario(nomeDoOponente){
    alert("Você entrou em uma sessão com: " + nomeDoOponente);
    $("#oponentLogin").text(nomeDoOponente);
    $("footer").css('display', 'block');
}

$(".campo").click(function () {
    var coordenadas = $(this).attr('id');
    turnoDoJogador(jogada, coordenadas);
});

function reset() {
    quadro = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
    $(".campo").text("#");
}

$("#reset").click(function () {
    reset();
});
