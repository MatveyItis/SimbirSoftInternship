/*function parseCommand(message) {
    if (message.includes("//room create ")) {
        let chatName = message.substring(message.includes(" -c ") ? 17 : 14, message.length);
        let chatType = message.includes(" -c ");
        if (chatName.length !== 0) {
            createRoom(chatName, chatType);
        }
    } else if (message.includes("//room connect ")) {
        let containsLogin = message.includes(" -l ");
        let chatName = message.substring(15, containsLogin ? message.indexOf(" -l ") : message.length);
        let userLogin;
        if (containsLogin) {
            userLogin = message.substring(message.indexOf(" -l ") + 4, message.length);
        }
        if (chatName.length !== 0) {
            connectRoom(chatName, userLogin);
        }
    } else if (message.includes("//room rename ")) {
        let newChatName = message.substring(14, message.length);
        if (newChatName.length !== 0) {
            renameRoom(newChatName);
        }
    } else if (message.includes("//room remove ")) {
        let chatName = message.substring(14, message.length);
        if (chatName.length !== 0) {
            removeRoom(chatName);
        }
    }
}*/

//todo add exception handling
function createRoom(name, chatType) {
    $.ajax({
        url: "/create_room",
        type: 'POST',
        dataType: "json",
        data: {
            chatName: name,
            chatType: chatType
        },
        success: function (result) {
            let chat = JSON.parse(JSON.stringify(result));
            $('#chats').append(
                '<a class="list-group-item list-group-item-action"\n' +
                'id="chat-' + chat.id + '-list" data-toggle="list"\n' +
                'href="#chat-' + chat.id + '">' + chat.name +
                '</a>\n' +
                '<input type="hidden" name="chatId" value="' + chat.id + '">'
            );
            $('#chat-messages').append(
                '<div class="tab-pane fade " id="chat-' + chat.id + '"\n' +
                '   role="tabpanel" ' +
                '   style="height: 550px; overflow-y: scroll">\n' +
                '   <table id="conversation" class="table table-borderless">\n' +
                '      <tbody id="greetings">\n' +
                '      </tbody>\n' +
                '   </table>\n' +
                '</div>'
            );
        },
        error: function (xhr, status, error) {
            console.log(xhr + ":" + status + ":" + error);
        }
    });
}

//todo add exception handling
function connectRoom(name, userLogin) {
    $.ajax({
        url: "/connect_room",
        type: 'POST',
        dataType: "json",
        data: {
            chatName: name,
            userLogin: userLogin
        },
        success: function (result) {
            let chat = JSON.parse(JSON.stringify(result));
            $('#chats').append(
                '<a class="list-group-item list-group-item-action"\n' +
                'id="chat-' + chat.id + '-list" data-toggle="list"\n' +
                'href="#chat-' + chat.id + '">' + chat.name +
                '</a>\n' +
                '<input type="hidden" name="chatId" value="' + chat.id + '">'
            );
            $('#chat-messages').append(
                '<div class="tab-pane fade " id="chat-' + chat.id + '"\n' +
                '   role="tabpanel" ' +
                '   style="height: 550px; overflow-y: scroll">\n' +
                '   <table id="conversation" class="table table-borderless">\n' +
                '      <tbody id="greetings">\n' +
                '      </tbody>\n' +
                '   </table>\n' +
                '</div>'
            );
            subscribeToChat(chat.id);
        },
        error: function (xhr, status, error) {
            console.log(xhr + ":" + status + ":" + error);
            let chatId = getCurrentChatId();
            let chats = document.getElementsByClassName("tab-pane fade");
            if (contains(chats, chatId)) {
                let chatDest = getChatElementByChatId(chats, chatId);
                $(chatDest).children('table').children('tbody').append('<tr class="alert alert-warning alert-dismissible fade show" role="alert">' +
                    '<th colspan="3">' +
                    '  <strong>Error.</strong> Cannot connect to the room!\n' +
                    '  <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                    '    <span aria-hidden="true">&times;</span>\n' +
                    '  </button>\n' +
                    '</th>' +
                    '</tr>'
                );
                let objDiv = $('#chat-' + chatId)[0];
                objDiv.scrollTop = objDiv.scrollHeight;
            }
        }
    });
}

function removeRoom(chatName) {
    $.ajax({
        url: "/remove_room",
        type: 'POST',
        dataType: "json",
        data: {
            chatName: chatName
        },
        success: function () {
            let chatId = getCurrentChatId();
            $("#chats").children("#chat-" + chatId + "-list")[0].remove();
        },
        error: function (xhr, status, error) {
            console.log(xhr + ":" + status + ":" + error);
        }
    });
}

function renameRoom(newChatName) {
    $.ajax({
        url: "/rename_room",
        type: 'POST',
        dataType: "json",
        data: {
            newChatName: newChatName,
            chatId: getCurrentChatId()
        },
        success: function (result) {
            let chat = JSON.parse(JSON.stringify(result));
            $('#chats').children('#chat-' + chat.id + '-list').html(chat.name);
        },
        error: function (xhr, status, error) {
            console.log(xhr + ":" + status + ":" + error);
        }
    });
}