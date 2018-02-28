var Game = {};
Game.connect = (function () {
    console.log("connect");
    Game.socket = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/game");
    // Game.socket = new WebSocket("ws://" + "ananymous.herokuapp.com" + "/game");

    Game.socket.onopen = function () {
        console.log('Info: WebSocket connection opened.');
        var resp = {};
        resp.type = "java.lang.String";
        resp.data = "Hello!";
        Game.socket.send(JSON.stringify(resp));
    };

    Game.socket.onclose = function () {
        console.log('Info: WebSocket closed.');
    };

    Game.socket.onmessage = function (event) {
        var message = JSON.parse(event.data);

        console.log("Info: onmessage");

        if (message.type === "java.lang.String") {
            console.log(message);
            console.log(message.data === "Hello!");
        }
    };
});

Game.connect();