var stompClient = null;

function setConnected(connected) {
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
}

function connect() {
    let socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        let chatsId = document.getElementsByName('chatId');
        for (let i = 0; i < chatsId.length; i++) {
            stompClient.subscribe('/topic/messages/' + Number(chatsId[i].value), function (message) {
                console.log("Received message " + message);
                showMessage(JSON.parse(message.body));
            });
        }
    });
}

function getCurrentChatId() {
    let activeChatLink = document.getElementsByClassName("list-group-item list-group-item-action active")[0];
    let chatId = activeChatLink.id.substring(5, activeChatLink.id.length - 5);
    return Number(chatId);
}

function sendMessage() {
    let textFromTextArea = $("textarea");
    parseCommand(textFromTextArea.val());
    let currentChatId = getCurrentChatId();
    stompClient.send("/app/chat/" + currentChatId, {}, JSON.stringify({
        'sender': $('#sender').val(),
        'text': $("#text").val()
    }));
    textFromTextArea.val('');
    let objDiv = $("#content");
    objDiv.scrollTop = objDiv.scrollHeight;
}

function contains(arr, elem) {
    let newArr = [];
    for (let i = 0; i < arr.length; i++) {
        newArr.push(arr[i].id.substring(5, arr[i].id.length));
    }
    return (newArr + "").indexOf(elem) !== -1;
}

function getChatElementByChatId(chats, chatId) {
    for (let i = 0; i < chats.length; i++) {
        let currentChatId = Number(chats[i].id.substring(5, chats[i].id.length));
        if (currentChatId === chatId) {
            return chats[i];
        }
    }
    return null;
}

function showMessage(message) {
    let dateTime = new Date(Date.parse(message.dateTime));
    dateTime.setHours(dateTime.getHours() + 3);
    let dateTimeString = dateTime.toISOString();
    dateTimeString = dateTimeString.replace('T', ' ');
    dateTimeString = dateTimeString.substring(dateTimeString.length - 5, 0);
    let chatId = message.chat.id;
    let chats = document.getElementsByClassName("tab-pane fade");
    if (contains(chats, chatId)) {
        let chatDest = getChatElementByChatId(chats, chatId);
        $(chatDest).children('table').children('tbody').append('<tr>' +
            '<th scope="row" style="width: 80px">' + message.sender + '</th>' +
            '<td colspan="2">' + message.text + '</td>' +
            '<td style="text-align: right; width: 180px">' + dateTimeString + '</td>' +
            '</tr>'
        );
        let objDiv = $(chatDest);
        objDiv.scrollTop = objDiv.scrollHeight;
    }
}

$(function () {
    $("#text").keyup(function (event) {
        if (event.keyCode === 13) {
            $("#send").click();
        }
    });
    $('#chats a').on('click', function (e) {
        e.preventDefault();
        $(this).tab('show');
    });
});
