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