<#import "parts/common.ftl" as c>
<@c.common "WebSocket">
    <noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript
            being
            enabled. Please enable
            Javascript and reload this page!</h2></noscript>
    <div id="main-content" class="container">
        <div class="row">
            <div class="col-md-6">
                <form class="form-inline">
                    <div class="form-group">
                        <label for="connect">WebSocket connection:</label>
                        <button id="connect" class="btn btn-default" type="submit">Connect</button>
                        <button id="disconnect" class="btn btn-default" type="submit" disabled="disabled">Disconnect
                        </button>
                    </div>
                </form>
            </div>
            <div class="col-md-6">
                <form class="form-inline">
                    <div class="form-group">
                        <label for="text" style="padding-right: 10px">Enter the text</label>
                        <input type="text" id="text" class="form-control" placeholder="Your text here...">
                    </div>
                    <button id="send" class="btn btn-default" type="submit">Send</button>
                </form>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table id="conversation" class="table table-striped">
                    <thead>
                    <tr>
                        <th>Greetings</th>
                    </tr>
                    </thead>
                    <tbody id="greetings">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</@c.common>