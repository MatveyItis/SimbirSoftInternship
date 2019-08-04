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
                <#else>
                </#if>
            </div>
        </div>
        <div class="col-md-9">
            <div class="tab-content" id="chat-messages">
                <#if chats??>
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
                                                        <div class="row justify-content-between"><b>${member.login}</b>
                                                            <#if chat.admin?? && chat.admin.login = member.login>
                                                                <small>ADMIN</small></#if>
                                                            <#if chat.owner.login = member.login>
                                                                <small>OWNER</small></#if>
                                                        </div>
                                                    </#list>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">
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
                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">
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
                                        <#--todo check types of response-->
                                            <tr>
                                                <th scope="row"
                                                    style="width: 80px"><#if message.sender??>${message.sender}<#else>
                                                        <strong style="color: red">Server: </strong></#if></th>
                                                <td colspan="2">${message.text}</td>
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
                </#if>
            </div>
            <div class="row">
                <div class="form-group col-md-11">
                    <label for="text">Enter the text</label>
                    <textarea id="text" class="form-control" placeholder="Type your text here..."></textarea>
                </div>
                <div class="form-group col-md-1 mt-4">
                    <button id="send" class="btn btn-primary btn-raised" type="button" onclick="sendMessage()">Send
                    </button>
                    <input type="hidden" readonly name="sender" id="sender" value="${username}">
                </div>
            </div>
        </div>
    </div>
    <@c.scripts/>
    </body>
</@c.common>