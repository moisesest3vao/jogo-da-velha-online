const url = 'http://localhost:8080';
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
            console.log(data);
            exibirResposta(data);
        })
    })
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
                alert("Parabéns, você criou um jogo. O id do jogo é: " + data.id);
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
                alert("Você entrou em uma sessão com: " + data.jogador1.login);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

function connectToSpecificGame() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Preencha seu login antes de entrar em um jogo!");
    } else {
        let idJogo = document.getElementById("game_id").value;
        if (idJogo == null || idJogo === '') {
            alert("Digite o id do Jogo");
        }
        $.ajax({
            url: url + "/game/connect",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "jogador": {
                    "login": login
                },
                "idJogo": idJogo
            }),
            success: function (data) {
                idJogo = data.id;
                marcador = 'O';
                reset();
                conectarSocket(idJogo);
                alert("Congrats you're playing with: " + data.jogador1.login);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}
