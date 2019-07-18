var stompClient = null;

function setConnected(connected) {
     if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    //$("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.send('/topic/new_user', {}, JSON.stringify({sender: $('#sender').val()}));
        stompClient.subscribe('/topic/messages', function (message) {
            console.log("Received message " + message);
            showMessage(JSON.parse(message.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.send("/app/chat", {}, JSON.stringify({'sender': $('#sender').val(), 'text': $("#text").val()}));
    $("textarea").val('');
    let objDiv = document.getElementById("content");
    objDiv.scrollTop = objDiv.scrollHeight;
}

function showMessage(message) {
    $("#greetings").append("<tr><td>" + '<b>' + message.sender + '$: </b>' + message.text + "</td></tr>");
    let objDiv = document.getElementById("content");
    objDiv.scrollTop = objDiv.scrollHeight;
}

$(function () {

});
