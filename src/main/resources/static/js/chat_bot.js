function parseCommand(message) {
    if (message.includes("//room create ")) {
        let chatName = message.substring(14, message.length);
        createRoom(chatName);
    }
}

function createRoom(name) {
    $.ajax({
        url: "/create_room",
        type: 'POST',
        dataType: "json",
        data: {
            chatName: name,
            username: $('#sender').val()
        },
        success: function (result) {
            let chat = JSON.parse(JSON.stringify(result));
            console.log(result);
            $('#chats').append(
                '<a class="list-group-item list-group-item-action"\n' +
                'id="chat-' + chat.id + '" data-toggle="list"\n' +
                'href="#chat-' + chat.id + '" role="tab" aria-controls="chat-' + chat.id + '">' + chat.name +
                '</a>'
            );
        },
        fail: function () {
            alert("fail");
        }
    });
}