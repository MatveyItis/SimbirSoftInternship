var stompClient = null;

function connect() {
    let socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        let chatsId = document.getElementsByName('chatId');
        if (chatsId.length === 0) {
            subscribeToCommonChat();
        }
        for (let i = 0; i < chatsId.length; i++) {
            subscribeToChat(Number(chatsId[i].value));
        }
    });
    let chatCountInterval = setInterval(function () {
        let chatsId = document.getElementsByName("chatId");
        $.ajax({
            url: "/chat_count",
            dataType: "json",
            success: function (chatCount) {
                if (chatCount !== chatsId.length) {
                    location.reload();
                }
            },
            error: function (xhr, status, error) {
                clearInterval(chatCountInterval);
            }
        });
    }, 5000);
}

function subscribeToChat(chatId) {
    if (stompClient !== null) {
        stompClient.subscribe('/topic/messages/' + Number(chatId), function (response) {
            console.log("Received response " + response);
            handleResponse(JSON.parse(response.body));
        });
    }
}

function subscribeToCommonChat() {
    if (stompClient !== null) {
        stompClient.subscribe('/topic/messages', function (response) {
            console.log("Received response " + response);
            handleResponse(JSON.parse(response.body));
        });
    }
}

function unsubscribeFromChat(chatId) {
    if (stompClient !== null) {
        stompClient.unsubscribe('/topic/messages/' + Number(chatId), function () {
            console.log('Unsubscribed from chat')
        })
    }
}

function unsubscribeFromAllChats() {
    let chatsId = document.getElementsByName('chatId');
    for (let i = 0; i < chatsId.length; i++) {
        unsubscribeFromChat(Number(chatsId[i].value));
    }
}

function getCurrentChatId() {
    let activeChatLink = document.getElementsByClassName("list-group-item list-group-item-action active")[0];
    if (activeChatLink !== undefined) {
        let chatId = activeChatLink.id.substring(5, activeChatLink.id.length - 5);
        return Number(chatId);
    }
    return 0;
}

function sendMessage() {
    let message = $("textarea");
    let currentChatId = getCurrentChatId();
    if (currentChatId === 0) {
        stompClient.send("/app/chat", {}, JSON.stringify({
            'sender': $('#sender').val(),
            'text': message.val()
        }));
        message.val('');
    } else if (!(/^\s+$/.test(message.val()))) {
        stompClient.send("/app/chat/" + currentChatId, {}, JSON.stringify({
            'sender': $('#sender').val(),
            'text': message.val()
        }));
        message.val('');
        let objDiv = $("#scroll-chat-" + currentChatId);
        objDiv.scrollTop = objDiv.scrollHeight;
    }
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
    if (chatDest !== null && response.message !== undefined) {
        $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
            '<th scope="row" style="width: 80px">' + response.message.sender + '</th>' +
            '<td colspan="2">' + response.message.text + '</td>' +
            '<td style="text-align: right; width: 180px"><small>' + dateTime + '</small></td>' +
            '</tr>'
        );
    }
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
        if ((response.utilMessage !== null && response.utilMessage !== undefined) &&
            (response.responseData !== null && response.responseData !== undefined && response.responseData.commandType !== 'HELP')) {
            $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
                '<th scope="row" style="width: 80px; color: forestgreen">Server: </th>' +
                '<td colspan="2">' + response.utilMessage + '</td>' +
                '<td style="text-align: right; width: 180px"><small>' + dateTime + '</small></td>' +
                '</tr>'
            );
        }
    } else if (type === 'YBOT_COMMAND' || type === 'YBOT_FIND' ||
        type === 'YBOT_FIVE_LAST_VIDEOS' || type === 'YBOT_RANDOM_COMMENT') {
        renderCommandAction(response);
        let yBotResponse = response.utilMessage;
        if (yBotResponse !== null && yBotResponse !== undefined &&
            type !== 'YBOT_RANDOM_COMMENT' && type !== 'YBOT_FIVE_LAST_VIDEOS' &&
            response.responseData !== null && response.responseData.commandType !== 'YBOT_HELP') {
            let videoHref = yBotResponse;
            let otherInfo = ' ';
            if (yBotResponse.includes(" v=") || yBotResponse.includes(" l=")) {
                videoHref = yBotResponse.substring(0, yBotResponse.indexOf(" "));
                otherInfo = otherInfo + yBotResponse.substring(yBotResponse.indexOf(" ") + 1);
            }
            $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
                '<th scope="row" style="width: 80px; color: cornflowerblue">yBot: </th>' +
                '<td colspan="2"><a href="' + response.utilMessage + '" target="_blank">' + videoHref + '</a> ' + otherInfo + '</td>' +
                '<td style="text-align: right; width: 180px"><small>' + dateTime + '</small></td>' +
                '</tr>'
            );
        }
    } else if (type === 'ERROR') {
        $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
            '<th scope="row" style="width: 80px;"><strong style="color: red">Server: </strong></th>' +
            '<td colspan="2">' + response.utilMessage + '</td>' +
            '<td style="text-align: right; width: 180px"><small>' + dateTime + '</small></td>' +
            '</tr>'
        );
    }
    let objDiv = $('#scroll-chat-' + getCurrentChatId())[0];
    objDiv.scrollTop = objDiv.scrollHeight;
}

function renderCommandAction(response) {
    if (response.responseData === null || response.responseData === undefined) {
        return;
    }
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
        case 'YBOT_FIVE_LAST_VIDEOS':
            renderFiveLastVideos(response);
            break;
        case 'YBOT_RANDOM_COMMENT':
            renderCommentRandom(response);
            break;
        case 'HELP':
            renderChatBotHelp(response);
            break;
        case 'YBOT_HELP':
            renderYBotHelp(response);
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

    $('#send').click(function () {
        sendMessage();
    });

    $('button[name=message]').click(function (e) {
        let id = Number(e.target.value);
        $.ajax({
            url: '/delete_message',
            dataType: 'json',
            type: 'POST',
            data: {
                id: id
            },
            success: function (result) {
                if (result === true) {
                    $('button[name=message], button[value=' + id + ']').closest('tr')[0].remove();
                }
            },
            error: function (xhr, status, error) {
                alert(xhr + '<br>' + status + '<br>' + error);
            }
        });
    })
});