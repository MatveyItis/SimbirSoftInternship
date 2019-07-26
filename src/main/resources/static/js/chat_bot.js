function parseCommand(message) {
    if (message.includes("//room create ")) {
        let chatName = message.substring(message.includes(" -c ") ? 17 : 14, message.length);
        let chatType = message.includes(" -c ");
        if (chatName.length !== 0) {
            createRoom(chatName, chatType);
        }
    } else if (message.includes("//room connect ")) {
        let chatName = message.substring(15, message.length);
        if (chatName.length !== 0) {
            connectRoom(chatName);
        }
    }
}

//todo add exception handling
function createRoom(name, chatType) {
    $.ajax({
        url: "/create_room",
        type: 'POST',
        dataType: "json",
        data: {
            chatName: name,
            username: $('#sender').val(),
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
                '   style="height: 600px; overflow-y: scroll">\n' +
                '   <table id="conversation" class="table table-borderless">\n' +
                '      <tbody id="greetings">\n' +
                '      </tbody>\n' +
                '   </table>\n' +
                '</div>'
            );
        },
        error: function (xhr, status, error) {
            console.log(xhr + ":" + status + ":" + error);
            alert("failed to creating a room");
        }
    });
}

//todo add exception handling
function connectRoom(name) {
    $.ajax({
        url: "/connect_room",
        type: 'POST',
        dataType: "json",
        data: {
            chatName: name,
            username: $('#sender').val()
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
                '   style="height: 600px; overflow-y: scroll">\n' +
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
                $(chatDest).children('table').children('tbody').append('<tr>' +
                    '<th colspan="4">' +
                    '<div class="alert alert-warning alert-dismissible fade show" role="alert">\n' +
                    '  <strong>Error.</strong> Cannot connect to the room!\n' +
                    '  <button type="button" class="close" data-dismiss="alert" aria-label="Close">\n' +
                    '    <span aria-hidden="true">&times;</span>\n' +
                    '  </button>\n' +
                    '</div>' +
                    '</th>' +
                    '</tr>'
                );
                let objDiv = $('#chat-' + chatId)[0];
                objDiv.scrollTop = objDiv.scrollHeight;
            }
        }
    });
}

function deleteChat(chatName) {
    $.ajax({
        url: "/connect_room",
        type: 'POST',
        dataType: "json",
        data: {
            chatName: name,
            username: $('#sender').val()
        },
        success: function (result) {

        },
        error: function (xhr, status, error) {
            console.log(xhr + ":" + status + ":" + error);
        }
    });
}