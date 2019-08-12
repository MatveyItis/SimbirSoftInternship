function renderConnectRoom(response) {
    let chatId = response.responseData.connectedChat.id;
    let connectedChatName = response.responseData.connectedChat.name;
    let connectedUserLogin = response.responseData.connectedUserLogin;
    if ($('#sender').val() === connectedUserLogin) {
        subscribeToChat(chatId);
        $('#chats').append(
            '<a class="list-group-item list-group-item-action"\n' +
            'id="chat-' + chatId + '-list" data-toggle="list"\n' +
            'href="#chat-' + chatId + '">' + connectedChatName +
            '</a>\n' +
            '<input type="hidden" name="chatId" value="' + chatId + '">'
        );
        $('#chat-messages').append(
            createRoomTemplate(chatId, connectedChatName, connectedUserLogin)
        );
    } else {

    }
}

function renderDisconnectRoom(response) {
    let chatId = response.responseData.chatId;
    unsubscribeFromChat(chatId);
    $("#chats").children("#chat-" + chatId + "-list")[0].remove();
    $('#chat-messages').children('#chat-' + chatId)[0].remove();
    let chatsId = document.getElementsByName('chatId');
    if (chatsId.length === 0) {
        createStartTemplate();
    } else {
        $("#chats").children('#chat-' + Number(chatsId[0].value) + "-list").click();
    }
}

function renderCreateRoom(response) {
    let chatId = response.responseData.chatId;
    let createdChatName = response.responseData.createdChatName;
    let username = response.responseData.username;
    if ($('#sender').val() === username) {
        $('#chats').append(
            '<a class="list-group-item list-group-item-action"\n' +
            'id="chat-' + chatId + '-list" data-toggle="list"\n' +
            'href="#chat-' + chatId + '">' + createdChatName +
            '</a>\n' +
            '<input type="hidden" name="chatId" value="' + chatId + '">'
        );
        $('#chat-messages').append(
            createRoomTemplate(chatId, createdChatName, username)
        );
        subscribeToChat(chatId);
        let chatList = $('.list-group-item');
        if (chatList.length === 1) {
            chatList[0].click();
        }
        $('#no_content_container').remove();
    }
}

function renderRemoveRoom(response) {
    let chatId = response.responseData.chatId;
    unsubscribeFromChat(chatId);
    $("#chats").children("#chat-" + chatId + "-list")[0].remove();
    let chatArray = document.getElementsByName("chatId");
    if (chatArray.length !== 0) {
        $("#chats").children('#chat-' + Number(chatArray[0].value) + "-list").click();
    }
    $('#chat-' + chatId).children().remove();
}

function renderRenameRoom(response) {
    let chatId = response.responseData.chatId;
    let renamedChatName = response.responseData.renamedChatName;
    $('#chats').children('#chat-' + chatId + '-list').html(renamedChatName);
    $('#chat-' + chatId).children('div').children('#chatName').html(renamedChatName);
}

function renderUserRename(response) {
    let renamedUserLogin = response.responseData.renamedUserLogin;
    $('#sender').val(renamedUserLogin);
    let chatId = getCurrentChatId();
    let dateTime = getDateTime(response.message.dateTime);
    let chats = document.getElementsByClassName("tab-pane fade");
    for (let i = 0; i < chats.length; i++) {
        let currentChatId = Number(chats[i].id.substring(5, chats[i].id.length));
        let chatDest = getChatElementByChatId(chats, currentChatId);
        if (currentChatId !== chatId) {
            $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
                '<th scope="row" style="width: 80px; color: forestgreen">Server: </th>' +
                '<td colspan="2">' + response.utilMessage + '</td>' +
                '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
                '</tr>'
            );
        }
    }

}

function renderUserBan(response) {
    let bannedUserLogin = response.responseData.bannedUserLogin;
    let bannedMinuteCount = response.responseData.bannedMinuteCount;
    if ($('#sender').val() === bannedUserLogin) {
        replaceTextAreaForBannedUser(bannedUserLogin, bannedMinuteCount);
    }
}

function renderUserModerator(response) {

}

function renderFiveLastVideos(response) {
    let channelName = response.responseData.channelName;
    let videoReferences = response.responseData.videoReferences;
    let chatId = response.message.chatId;
    let dateTime = getDateTime(response.message.dateTime);
    let chats = document.getElementsByClassName("tab-pane fade");
    let chatDest = getChatElementByChatId(chats, chatId);
    $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
        '<th scope="row" style="width: 80px; color: dodgerblue">yBot:</th>' +
        '<td colspan="2">' + channelName + '</td>' +
        '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
        '</tr>'
    );
    let hrefs = '';
    for (let i = 0; i < videoReferences.length; i++) {
        hrefs += '<a href="' + videoReferences[i] + '" target="_blank">' + videoReferences[i] + '</a><br>'
    }
    $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
        '<th scope="row" style="width: 80px; color: dodgerblue">yBot:</th>' +
        '<td colspan="2">' + hrefs + '</td>' +
        '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
        '</tr>'
    );
}

function renderCommentRandom(response) {
    let commentInfo = response.responseData.commentInfo;
    let commentAuthorName = commentInfo.substring(0, commentInfo.indexOf("::"));
    let commentName = commentInfo.substring(commentInfo.indexOf("::") + 2);
    let dateTime = getDateTime(response.message.dateTime);
    let chats = document.getElementsByClassName("tab-pane fade");
    let chatId = response.message.chatId;
    let chatDest = getChatElementByChatId(chats, chatId);
    $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
        '<th scope="row" style="width: 80px; color: dodgerblue">yBot:</th>' +
        '<td colspan="2">' + commentAuthorName + '</td>' +
        '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
        '</tr>'
    );
    $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
        '<th scope="row" style="width: 80px; color: dodgerblue">yBot:</th>' +
        '<td colspan="2">' + commentName + '</td>' +
        '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
        '</tr>'
    );
}

function renderChatBotHelp(response) {
    let chats = document.getElementsByClassName("tab-pane fade");
    let dateTime = getDateTime(response.message.dateTime);
    let chatId = response.message.chatId;
    let chatDest = getChatElementByChatId(chats, chatId);
    $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
        '<th scope="row" style="width: 80px; color: forestgreen">Server:</th>' +
        '<td colspan="2">' + helpInfo() + '</td>' +
        '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
        '</tr>'
    );
}

function renderYBotHelp(response) {
    let chats = document.getElementsByClassName("tab-pane fade");
    let dateTime = getDateTime(response.message.dateTime);
    let chatId = response.message.chatId;
    let chatDest = getChatElementByChatId(chats, chatId);
    $(chatDest).children('div').children('table').children('tbody').append('<tr>' +
        '<th scope="row" style="width: 80px; color: dodgerblue">yBot:</th>' +
        '<td colspan="2">' + helpYBotInfo() + '</td>' +
        '<td style="text-align: right; width: 180px">' + dateTime + '</td>' +
        '</tr>'
    );
}