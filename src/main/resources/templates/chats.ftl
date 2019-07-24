<#import "./parts/common.ftl" as c>
<@c.common "Chats">
    <body>
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
                           id="chat-${chat.id}" data-toggle="list"
                           href="#chat-${chat.id}" role="tab" aria-controls="chat-${chat.id}">${chat.name}</a>
                    </#list>
                <#else>
                </#if>
            </div>
        </div>
        <div class="col-md-9">
            <div class="tab-content" id="nav-tabContent">
                <#if chats??>
                    <#list chats as chat>
                        <div class="tab-pane fade show <#if chat_index = 0>active<#else></#if>" id="chat-${chat.id}"
                             role="tabpanel" aria-labelledby="chat-${chat.id}"
                             style="height: 600px; overflow-y: scroll">
                            <table id="conversation" class="table table-borderless">
                                <thead>
                                <tr>
                                    <th scope="row" colspan="4" style="text-align: center">${chat.name}</th>
                                </tr>
                                </thead>
                                <tbody id="greetings">
                                <tr>
                                    <th scope="row">admin</th>
                                    <td colspan="2">Hey guys</td>
                                    <td style="text-align: right; width: 180px">01-01-2019 14:44</td>
                                </tr>
                                <tr>
                                    <th scope="row">tom</th>
                                    <td colspan="2">Hey admin!</td>
                                    <td style="text-align: right; width: 180px">01-01-2019 14:45</td>
                                </tr>
                                <tr>
                                    <th scope="row">cat</th>
                                    <td colspan="2">Meow</td>
                                    <td style="text-align: right; width: 180px">01-01-2019 14:46</td>
                                </tr>
                                <tr>
                                    <th scope="row">dog</th>
                                    <td colspan="2">Haw-haw!</td>
                                    <td style="text-align: right; width: 180px">01-01-2019 14:47</td>
                                </tr>
                                <#--<#if messages??>
                                    <#list messages as message>
                                        <tr>
                                            <th scope="row" style="width: 80px">${message.sender}</th>
                                            <td colspan="2">${message.text}</td>
                                            <td style="text-align: right; width: 180px">${message.dateTime.format(formatter)}</td>
                                        </tr>
                                    </#list>
                                </#if>-->
                                </tbody>
                            </table>
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