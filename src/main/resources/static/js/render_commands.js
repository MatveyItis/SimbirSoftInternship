function renderConnectRoom(response) {
    let chatId = response.responseData.connectedChat.id;
    subscribeToChat(chatId);
    let connectedChatName = response.responseData.connectedChat.name;
    let connectedUserLogin = response.responseData.connectedUserLogin;
    if ($('#sender').val() === connectedUserLogin) {
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
}

function renderRenameRoom(response) {
    let chatId = response.responseData.chatId;
    let renamedChatName = response.responseData.renamedChatName;
    $('#chats').children('#chat-' + chatId + '-list').html(renamedChatName);
    $('#chat-' + chatId).children('div').children('#chatName').html(renamedChatName);
}

function renderUserRename(response) {

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