function renderConnectRoom(response) {
    let chatId = response.responseData.connectedChat.id;
    subscribeToChat(chatId);
    let connectedChatName = response.responseData.connectedChat.name;
    let connectedUserLogin = response.responseData.connectedUserLogin;
    $('#chats').append(
        '<a class="list-group-item list-group-item-action"\n' +
        'id="chat-' + chatId + '-list" data-toggle="list"\n' +
        'href="#chat-' + chatId + '">' + connectedChatName +
        '</a>\n' +
        '<input type="hidden" name="chatId" value="' + chatId + '">'
    );
    if ($('#sender').val() === connectedUserLogin) {
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

}

function renderUserModerator(response) {

}