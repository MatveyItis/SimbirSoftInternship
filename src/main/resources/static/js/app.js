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
    parseCommand($("textarea").val());
    /*stompClient.send("/app/chat", {}, JSON.stringify({'sender': $('#sender').val(), 'text': $("#text").val()}));
    $("textarea").val('');
    let objDiv = $("#content");
    objDiv.scrollTop = objDiv.scrollHeight;*/
}

function showMessage(message) {
    let dateTime = new Date(Date.parse(message.dateTime));
    dateTime.setHours(dateTime.getHours() + 3);
    let dateTimeString = dateTime.toISOString();
    dateTimeString = dateTimeString.replace('T', ' ');
    dateTimeString = dateTimeString.substring(dateTimeString.length - 5, 0);
    $("#greetings").append('<tr>' +
        '<th scope="row" style="width: 80px">' + message.sender + '</th>' +
        '<td colspan="2">' + message.text + '</td>' +
        '<td style="text-align: right; width: 180px">' + dateTimeString + '</td>' +
        '</tr>'
    );
    let objDiv = document.getElementById("content");
    objDiv.scrollTop = objDiv.scrollHeight;
}

$(function () {
    $("#text").keyup(function (event) {
        if (event.keyCode === 13) {
            $("#send").click();
        }
    });
});
