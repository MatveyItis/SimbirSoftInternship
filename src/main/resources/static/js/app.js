var stompClient = null;

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

function unsubscribeFromChat(chatId) {
    if (stompClient !== null) {
        stompClient.unsubscribe('/topic/messages' + Number(chatId), function () {
            console.log('Unsubscribed from chat')
        })
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

function getDateTime(time) {
    let dateTime = new Date(Date.parse(time));
    dateTime.setHours(dateTime.getHours() + 3);
    let dateTimeString = dateTime.toISOString();
    dateTimeString = dateTimeString.replace('T', ' ');
    dateTimeString = dateTimeString.substring(dateTimeString.length - 5, 0);
    return dateTimeString;
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
    let chats = document.getElementsByClassName("tab-pane fade");
    let dateTime = getDateTime(response.message.dateTime);
    let chatId = response.message.chatId;
    let chatDest = getChatElementByChatId(chats, chatId);
    $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
        '<th scope="row" style="width: 80px">' + response.message.sender + '</th>' +
        '<td colspan="2">' + response.message.text + '</td>' +
        '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
        '</tr>'
    );
    setTimeout(renderResponse, 300, response, chatDest);

    let objDiv = $('#scroll-chat-' + getCurrentChatId())[0];
    objDiv.scrollTop = objDiv.scrollHeight;
}

function renderResponse(response, chatDest) {
    let type = response.message.type;
    let dateTime = getDateTime(response.message.dateTime);
    if (type === 'MESSAGE') {

    } else if (type === 'COMMAND') {
        renderCommandAction(response);
        $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
            '<th scope="row" style="width: 80px; color: forestgreen">Server: </th>' +
            '<td colspan="2">' + response.utilMessage + '</td>' +
            '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
            '</tr>'
        );
    } else if (type === 'YBOT_COMMAND') {
        $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
            '<th scope="row" style="width: 80px; color: cornflowerblue">yBot: </th>' +
            '<td colspan="2"><a href="' + response.utilMessage + '" target="_blank">' + response.utilMessage + '</a></td>' +
            '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
            '</tr>'
        );
    } else if (type === 'ERROR') {
        $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
            '<th scope="row" style="width: 80px;"><strong style="color: red">Server: </strong></th>' +
            '<td colspan="2">' + response.utilMessage + '</td>' +
            '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
            '</tr>'
        );
    }
}

function renderCommandAction(response) {
    let commandType = response.responseData.commandType;
    switch (commandType) {
        case 'CONNECT_ROOM':
            renderConnectRoom(response);
            break;
        case 'DISCONNECT_ROOM':
            renderDisconnectRoom(response);
            break;
        case 'CREATE_ROOM':
            renderCreateRoom(response);
            break;
        case 'REMOVE_ROOM':
            renderRemoveRoom(response);
            break;
        case 'RENAME_ROOM':
            renderRenameRoom(response);
            break;
        case 'USER_RENAME':
            renderUserRename(response);
            break;
        case 'USER_BAN':
            renderUserBan(response);
            break;
        case 'USER_MODERATOR':
            renderUserModerator(response);
            break;
        default:
            console.log('Что-то пошло не так')
    }
}

$(function () {
    $("#text").keyup(function (event) {
        if (event.shiftKey && event.keyCode === 13) {

        } else if (event.keyCode === 13) {
            $("#send").click();
        }
    });
});
