<#import "parts/common.ftl" as c>
<@c.common "Chat">
    <body >
    <@c.navbar/>
    <noscript>
        <h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
            enabled. Please enable Javascript and reload this page!</h2></noscript>
    <div id="main-content" class="container">
        <div class="row">
            <div class="col-md-6">
            </div>
            <div class="col-md-6">
            </div>
        </div>
        <div class="row">
            <h5 class="p-2">Chat room</h5>
            <div class="col-md-12" id="content" style="height: 600px; overflow-y: scroll">
                <table id="conversation" class="table table-borderless">

                </table>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-md-11">
                <label for="text">Enter the text</label>
                <textarea id="text" class="form-control" placeholder="Your text here..."></textarea>
            </div>
            <div class="form-group col-md-1 mt-4">
                <button id="send" class="btn btn-primary btn-raised" type="button" onclick="sendMessage()">Send
                </button>
                <input type="hidden" readonly name="sender" id="sender" value="${username}">
            </div>
        </div>
    </div>
    <@c.scripts/>
    </body>
</@c.common>