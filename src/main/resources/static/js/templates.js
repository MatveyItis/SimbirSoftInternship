function createRoomTemplate(chatId, chatName, username) {
    return '<div class="tab-pane fade " id="chat-' + chatId + '"\n' +
        '   role="tabpanel" aria-labelledby="chat-' + chatId + '">\n' +
        '<div class="row justify-content-between">\ ' +
        '<h6 class="font-weight-light pl-4 mt-2" id="chatName">' + chatName + '</h6>\n' +
        '    <div class="btn-group-sm">\n' +
        '        <button type="button" class="btn btn-sm btn-primary dropdown-toggle"\n' +
        '         data-toggle="dropdown"\n' +
        '         aria-haspopup="true" aria-expanded="false">\n' +
        '              Settings\n' +
        '         </button>\n' +
        '             <div class="dropdown-menu">\n' +
        '                 <button class="dropdown-item" data-toggle="modal"\n' +
        '                         data-target="#chat-members-' + chatId + '">Members\n' +
        '                 </button>\n' +
        '                 <button class="dropdown-item" data-toggle="modal"\n' +
        '                         data-target="#chat-info-' + chatId + '">Info\n' +
        '                 </button>\n' +
        '                 <div class="dropdown-divider"></div>\n' +
        '                 <button class="dropdown-item" href="#">Exit</button>\n' +
        '             </div>\n' +
        '         </div>\n' +
        '         <!-- Modal for chat-members-' + chatId + ' -->\n' +
        '         <div class="modal fade" id="chat-members-' + chatId + '" tabindex="-1" role="dialog"\n' +
        '              aria-labelledby="exampleModalCenterTitle" aria-hidden="true">\n' +
        '             <div class="modal-dialog modal-dialog-centered" role="document">\n' +
        '                 <div class="modal-content">\n' +
        '                     <div class="modal-header">\n' +
        '                         <h5 class="modal-title" id="exampleModalLongTitle">Chat members</h5>\n' +
        '                         <button type="button" class="close" data-dismiss="modal"\n' +
        '                                 aria-label="Close">\n' +
        '                             <span aria-hidden="true">&times;</span>\n' +
        '                         </button>\n' +
        '                     </div>\n' +
        '                     <div class="modal-body">\n' +
        '                         <div class="container">\n' +
        '                                 <div class="row justify-content-between"><b>' + username + '</b>\n' +
        '                                         <small>OWNER</small>\n' +
        '                                 </div>\n' +
        '                         </div>\n' +
        '                     </div>\n' +
        '                     <div class="modal-footer">\n' +
        '                         <button type="button" class="btn btn-secondary" data-dismiss="modal">\n' +
        '                             Close\n' +
        '                         </button>\n' +
        '                     </div>\n' +
        '                 </div>\n' +
        '             </div>\n' +
        '         </div>\n' +
        '         <!-- Modal for chat-info-' + chatId + ' -->\n' +
        '           <div class="modal fade" id="chat-info-' + chatId + '" tabindex="-1" role="dialog"\n' +
        '                aria-labelledby="exampleModalCenterTitle" aria-hidden="true">\n' +
        '               <div class="modal-dialog modal-dialog-centered" role="document">\n' +
        '                   <div class="modal-content">\n' +
        '                       <div class="modal-header">\n' +
        '                           <h5 class="modal-title" id="exampleModalLongTitle">Chat info</h5>\n' +
        '               <button type="button" class="close" data-dismiss="modal"\n' +
        '                       aria-label="Close">\n' +
        '                   <span aria-hidden="true">&times;</span>\n' +
        '               </button>\n' +
        '            </div>\n' +
        '            <div class="modal-body">\n' +
        '                <div class="container">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '            <div class="modal-footer">\n' +
        '                       <button type="button" class="btn btn-secondary" data-dismiss="modal">\n' +
        '                            Close\n' +
        '                       </button>\n' +
        '            </div>\n' +
        '                 </div>\n' +
        '            </div>\n' +
        '       </div>\n' +
        '   </div>' +
        '   <div class="container" style="height: 550px; overflow-y: scroll" id="scroll-chat-' + chatId + '">' +
        '       <table id="conversation" class="table table-borderless">\n' +
        '           <tbody id="greetings">\n' +
        '       </tbody>\n' +
        '       </table>\n' +
        '   </div>' +
        '</div>';
}

function createStartTemplate() {
    return "";
}

function replaceTextAreaForBannedUser(bannedUserLogin, bannedMinuteCount) {
    if ($('#sender').val() === bannedUserLogin) {
        $('#text-area').html(
            '<div class="form-group col-md-11 bmd-form-group">\n' +
            '<textarea readonly class="form-control" placeholder="You are banned for ' + bannedMinuteCount + ' minutes :(">' +
            '</textarea>\n' +
            '</div>\n' +
            '<div class="form-group col-md-1 mt-4">\n' +
            '   <button class="btn btn-primary btn-raised" disabled type="button">Send\n' +
            '   </button>\n' +
            '</div>'
        );
    }
}

function helpInfo() {
    return "Info for rooms: <br>" +
        "//room create {name of the room} - create the room <br>" +
        "\t -c - private room <br>" +
        "//room remove {name of the room} - remove the room <br>" +
        "//room rename {new name of the room} - rename the room <br>" +
        "//room connect {name of the room} - connect the room <br>" +
        "\t -l - user login <br>" +
        "//room disconnect - exit from the current room <br>" +
        "\t -l - user login <br>" +
        "\t -m - minute count <br>" +
        "//room disconnect {name of the room} - exit from the room <br>" +
        "For users: \n" +
        "//user rename {user login} - rename login of the user <br>" +
        "//user ban - ban user -l {login user} -m {minute count} <br>" +
        "\t -l - user login, deports user from all rooms <br>" +
        "\t -m - minute count, time for which the user will not be able to enter the room <br>" +
        "//user moderator {user login} <br>" +
        "\t -n - nominate to moderator <br>" +
        "\t -d - downgrade to user <br>" +
        "//help - help info";
}

function helpYBotInfo() {
    return "Info for yBot: <br>" +
        "//yBot find -k {name of the channel} -w {name of the video} <br>" +
        "\t -v - view count <br>" +
        "\t -l - like count <br>" +
        "//yBot changelInfo {name of the chanel} - return 5 last videos from the channel <br>" +
        "//yBot videoCommentRandom -k {name of the channel} -w {name of the video} - return 1 random comment from video <br>" +
        "//yBot help - help info";
}