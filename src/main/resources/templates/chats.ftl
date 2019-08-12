<#import "./parts/common.ftl" as c>

<@c.common "Chats">
    <body onload="connect()">
    <@c.navbar/>
    <div class="row container-fluid pt-2" style="justify-content: center">
        <h4>Chats</h4>
    </div>
    <div class="row container-fluid">
        <div class="col-md-3">
            <div class="list-group" id="chats" role="tablist" style="overflow-y: scroll">
                <#if chats??>
                    <#list chats as chat>
                        <a class="list-group-item list-group-item-action <#if chat_index = 0>active<#else></#if>"
                           id="chat-${chat.id}-list" data-toggle="list"
                           href="#chat-${chat.id}" role="tab" aria-controls="chat-${chat.id}">${chat.name}</a>
                        <input type="hidden" name="chatId" value="${chat.id}">
                    </#list>
                </#if>
            </div>
        </div>
        <div class="col-md-9">
            <div class="tab-content" id="chat-messages">
                <#if chats?? && chats?size != 0>
                    <#list chats as chat>
                        <div class="tab-pane fade <#if chat_index = 0>show active<#else></#if>" id="chat-${chat.id}"
                             role="tabpanel" aria-labelledby="chat-${chat.id}">
                            <div class="row justify-content-between">
                                <h6 class="font-weight-light pl-4 mt-2" id="chatName">${chat.name}</h6>
                                <div class="btn-group-sm">
                                    <button type="button" class="btn btn-sm btn-primary dropdown-toggle"
                                            data-toggle="dropdown"
                                            aria-haspopup="true" aria-expanded="false">
                                        Settings
                                    </button>
                                    <div class="dropdown-menu">
                                        <button class="dropdown-item" data-toggle="modal"
                                                data-target="#chat-members-${chat.id}">Members
                                        </button>
                                        <button class="dropdown-item" data-toggle="modal"
                                                data-target="#chat-info-${chat.id}">Info
                                        </button>
                                        <div class="dropdown-divider"></div>
                                        <button class="dropdown-item" href="#">Exit</button>
                                    </div>
                                </div>
                                <!-- Modal for chat-members-${chat.id} -->
                                <div class="modal fade" id="chat-members-${chat.id}" tabindex="-1" role="dialog"
                                     aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="exampleModalLongTitle">Chat members</h5>
                                                <button type="button" class="close" data-dismiss="modal"
                                                        aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <div class="container">
                                                    <#list chat.members as member>
                                                        <div class="row justify-content-between">
                                                            <b>${member.login}</b>
                                                            <#if chat.admin?? && chat.admin.login = member.login>
                                                                <small>ADMIN</small></#if>
                                                            <#if chat.owner.login = member.login>
                                                                <small>OWNER</small></#if>
                                                        </div>
                                                    </#list>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"
                                                        data-dismiss="modal">
                                                    Close
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- Modal for chat-info-${chat.id} -->
                                <div class="modal fade" id="chat-info-${chat.id}" tabindex="-1" role="dialog"
                                     aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="exampleModalLongTitle">Chat info</h5>
                                                <button type="button" class="close" data-dismiss="modal"
                                                        aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <div class="container">

                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"
                                                        data-dismiss="modal">
                                                    Close
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="container" style="height: 550px; overflow-y: scroll"
                                 id="scroll-chat-${chat.id}">
                                <table id="conversation" class="table table-borderless">
                                    <tbody id="greetings">
                                    <#if chat.messages??>
                                        <#list chat.messages as message>
                                            <tr>
                                                <th scope="row" style="width: 80px">
                                                    <#if message.sender??>
                                                        ${message.sender}
                                                    <#elseif message.type.name() == 'COMMAND'>
                                                        <strong style="color: forestgreen">Server: </strong>
                                                    <#elseif message.type.name() == 'YBOT_COMMAND'>
                                                        <strong style="color: dodgerblue">yBot: </strong>
                                                    <#elseif message.type.name() == 'ERROR'>
                                                        <strong style="color: red">Server: </strong>
                                                    </#if>
                                                </th>
                                                <td colspan="2">
                                                    <#if message.type.name() == 'YBOT_COMMAND' && !(message.sender??)>
                                                        <#if message.text?contains(' ')>
                                                            <a href="${message.text?substring(0, message.text?index_of(' '))}"
                                                               target="_blank">${message.text?substring(0, message.text?index_of(' '))}</a>
                                                            ${message.text?substring(message.text?index_of(' '))}
                                                        <#else >
                                                            <a href="${message.text}"
                                                               target="_blank">${message.text}</a>
                                                        </#if>
                                                    <#else>
                                                        ${message.text}
                                                    </#if>
                                                </td>
                                                <td style="text-align: right; width: 180px">${message.dateTime.format(formatter)}</td>
                                            </tr>
                                        </#list>
                                    </#if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </#list>
                <#else>
                    <div class="container" align="center" id="no_content_container">
                        <h4>No available chats</h4><br>
                        <img src="/img/feedback.svg" width="450" height="450" alt="no available chat">
                        <br>
                        <small>Please enter command '//room create {-c}(if you want private chat) {your_chat_name}' or
                            '//room
                            connect {public_chat_name}'</small>
                    </div>
                </#if>
            </div>
            <div class="row" id="text-area">
                <#if banned>
                    <div class="form-group col-md-11">
                        <label for="text">Enter the text</label>
                        <textarea id="text" readonly class="form-control" placeholder="You are banned:("></textarea>
                    </div>
                    <div class="form-group col-md-1 mt-4">
                        <button class="btn btn-primary btn-raised" disabled type="button">Send
                        </button>
                    </div>
                <#else>
                    <div class="form-group col-md-11">
                        <label for="text">Enter the text</label>
                        <textarea id="text" class="form-control" placeholder="Type your text here..."></textarea>
                    </div>
                    <div class="form-group col-md-1 mt-4">
                        <button id="send" class="btn btn-primary btn-raised" type="button">Send
                        </button>
                        <input type="hidden" readonly name="sender" id="sender" value="${username}">
                    </div>
                </#if>
            </div>
        </div>
    </div>
    <@c.scripts/>
    </body>
</@c.common>