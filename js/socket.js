let stompClient;
let idJogo;
let marcador;

function conectarSocket(idJogo) {

    console.log("Conectando ao jogo...");
    let socket = new SockJS(url + "/executar-jogada");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("conectado ao frame: " + frame);
        stompClient.subscribe("/topic/progresso-jogo/" + idJogo, function (response) {
            let data = JSON.parse(response.body);
            idJogo = data.id;
            console.log(data);
            exibirResposta(data);
        })
    })
    jogoIniciou = true;
}

function criarJogo() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Preencha seu login antes de criar um jogo!");
    } else {
        $.ajax({
            url: url + "/jogo/iniciar-jogo",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login
            }),
            success: function (data) {
                idJogo = data.id;
                marcador = 'X';
                reset();
                conectarSocket(idJogo);
                desabilitarBotoes();
                alert("Parabéns, você criou um jogo. O id do jogo é: " + data.id);
                isCriadorDaPartida = true;
                jogoIniciou = true;
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}


function conectarAJogoAleatorio() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Digite seu login antes de conectar-se a um jogo");
    } else {
        $.ajax({
            url: url + "/jogo/conectar-jogo/aleatorio",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login
            }),
            success: function (data) {
                idJogo = data.id;
                marcador = 'O';
                reset();
                conectarSocket(idJogo);
                desabilitarBotoes();
                alertarConexaoParaUsuario(data.jogador1.login);
                
            },
            error: function (xhr, textStatus) {
                let statusCode = xhr.status;
                if(statusCode == 500){
                    alert("Não há nenhum jogo disponível no momento");
                }            
            }
        })
    }
}

function conectarAJogo() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Preencha seu login antes de entrar em um jogo!");
    } else {
        let idJogoField = document.getElementById("game_id").value;
        if (idJogoField == null || idJogoField === '') {
            alert("Digite o id do Jogo");
        }
        $.ajax({
            url: url + "/jogo/conectar-jogo",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "jogador": {
                    "login": login
                },
                "idJogo": idJogoField
            }),
            success: function (data) {
                idJogo = data.id;
                marcador = 'O';
                reset();
                desabilitarBotoes();
                conectarSocket(idJogo);
                alertarConexaoParaUsuario(data.jogador1.login);
            },
            error: function (xhr, textStatus) {
                let statusCode = xhr.status;
                if(statusCode == 500){
                    alert("Já há dois jogadores nessa partida!");
                }            
            }
        })
    }
}
