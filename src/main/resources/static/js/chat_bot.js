function parseCommand(message) {
    if (message.includes("//room create ")) {
        let chatName = message.substring(message.includes(" -c ") ? 17 : 14, message.length);
        let chatType = message.includes(" -c ");
        createRoom(chatName, chatType);
    }
}

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
                'id="chat-' + chat.id + '" data-toggle="list"\n' +
                'href="#chat-' + chat.id + '">' + chat.name +
                '</a>'
            );
            $('#chat-messages').append(
                '<div class="tab-pane fade " id="chat-' + chat.id + '"\n' +
                '   role="tabpanel" ' +
                '   style="height: 600px; overflow-y: scroll">\n' +
                '   <table id="conversation" class="table table-borderless">\n' +
                '      <thead>\n' +
                '      </thead>\n' +
                '      <tbody id="greetings">\n' +
                '      </tbody>\n' +
                '   </table>\n' +
                '</div>')
        },
        fail: function () {
            alert("failed to creating a room");
        }
    });
}

function connectRoom() {

}