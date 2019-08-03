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
            subscribeToChat(Number(chatsId[i].value));
        }
    });
}

function subscribeToChat(chatId) {
    if (stompClient !== null) {
        stompClient.subscribe('/topic/messages/' + Number(chatId), function (response) {
            console.log("Received response " + response);
            handleResponse(JSON.parse(response.body));
        });
    }
}

function getCurrentChatId() {
    let activeChatLink = document.getElementsByClassName("list-group-item list-group-item-action active")[0];
    let chatId = activeChatLink.id.substring(5, activeChatLink.id.length - 5);
    return Number(chatId);
}

function sendMessage() {
    let message = $("textarea");
    let currentChatId = getCurrentChatId();
    if (!(/^\s+$/.test(message.val()))) {
        stompClient.send("/app/chat/" + currentChatId, {}, JSON.stringify({
            'sender': $('#sender').val(),
            'text': message.val()
        }));
        message.val('');
        let objDiv = $("#scroll-chat-" + currentChatId);
        objDiv.scrollTop = objDiv.scrollHeight;
    }
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

function handleResponse(response) {
    let type = response.message.type;

    let chats = document.getElementsByClassName("tab-pane fade");
    let dateTime = new Date(Date.parse(response.message.dateTime));
    dateTime.setHours(dateTime.getHours() + 3);
    let dateTimeString = dateTime.toISOString();
    dateTimeString = dateTimeString.replace('T', ' ');
    dateTimeString = dateTimeString.substring(dateTimeString.length - 5, 0);
    let chatId = response.message.chatId;
    let chatDest = getChatElementByChatId(chats, chatId);
    $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
        '<th scope="row" style="width: 80px">' + response.message.sender + '</th>' +
        '<td colspan="2">' + response.message.text + '</td>' +
        '<td style="text-align: right; width: 180px">' + dateTimeString + '</td>' +
        '</tr>'
    );
    let objDiv = $('#scroll-chat-' + chatId)[0];
    objDiv.scrollTop = objDiv.scrollHeight;

    if (type === 'MESSAGE') {
        return;
    } else if (type === 'COMMAND') {
        $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
            '<th scope="row" style="width: 80px; background-color: forestgreen">Success: </th>' +
            '<td colspan="3">' + response.utilMessage + '</td>' +
            '</tr>'
        );
    } else if (type === 'YBOT_COMMAND') {
        $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
            '<th scope="row" style="width: 80px; background-color: #ff5a58">Success to find video!</th>' +
            '<td colspan="3">' + response.utilMessage + '</td>' +
            '</tr>'
        );
    } else if (type === 'ERROR') {
        $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
            '<th scope="row" style="width: 80px;"><strong style="color: red">Server: </strong></th>' +
            '<td colspan="3">' + response.utilMessage + '</td>' +
            '</tr>'
        );
    } else if (type === 'COMMAND_ERROR') {
        $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
            '<th scope="row" style="width: 80px; background-color: red">Ops. We have a problem with parsing command: </th>' +
            '<td colspan="3">' + response.utilMessage + '</td>' +
            '</tr>'
        );
    } else if (type === 'JOIN') {

    } else if (type === 'LEFT') {

    }
    objDiv.scrollTop = objDiv.scrollHeight;
}

$(function () {
    $("#text").keyup(function (event) {
        if (event.shiftKey && event.keyCode === 13) {

        } else if (event.keyCode === 13) {
            $("#send").click();
        }
    });
});
