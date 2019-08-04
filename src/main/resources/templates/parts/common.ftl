<#macro common title>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <link rel="stylesheet"
              href="https://unpkg.com/bootstrap-material-design@4.1.1/dist/css/bootstrap-material-design.min.css"
              integrity="sha384-wXznGJNEXNG1NFsbm0ugrLFMQPWswR3lds2VeinahP8N0zJw9VWSopbjv2x7WCvX"
              crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="/css/app.css">
        <script src="/webjars/jquery/jquery.min.js"></script>
        <script src="/webjars/sockjs-client/sockjs.min.js"></script>
        <script src="/webjars/stomp-websocket/stomp.min.js"></script>
        <script src="/js/app.js"></script>
        <script src="/js/chat_bot.js"></script>
        <script src="/js/render_commands.js"></script>
        <script src="/js/templates.js"></script>
        <title>${title}</title>
    </head>
    <#nested>
    </html>
</#macro>

<#macro navbar>
    <#assign known=Session.SPRING_SECURITY_CONTEXT??>
    <#if known>
        <#assign user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name = user.getUsername()
        isAdmin = user.isAdmin()
        currentUserId = user.getId()>
    <#else>
        <#assign name = "unknown"
        isAdmin = false
        currentUserId = -1>
    </#if>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <a class="navbar-brand" href="#"><img src="/img/chat.svg" alt="chat" width="35" height="35">
            <div class="ripple-container"></div>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarColor02"
                aria-controls="navbarColor02" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarColor02">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/chat">Home <span class="sr-only">(current)</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/chats">Chats</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/about">About</a>
                </li>

            </ul>
            <form class="form-inline my-2 my-lg-0" action="/logout" method="post">
                <pre class="m-2" style="color: white">${name}</pre>
                <button class="btn btn-dark btn-raised btn-xs"
                        type="submit"><#if user??>Sign Out<#else>Log in</#if></button>
            </form>
        </div>
    </nav>
</#macro>

<#macro scripts>
    <script src="https://code.jquery.com/jquery-3.4.1.min.js" type="text/javascript"></script>
    <script src="https://unpkg.com/popper.js@1.12.6/dist/umd/popper.js"
            integrity="sha384-fA23ZRQ3G/J53mElWqVJEGJzU0sTs+SvzG8fXVWP+kJQ1lwFAOkcUOysnlKJC33U"
            crossorigin="anonymous"></script>
    <script src="https://unpkg.com/bootstrap-material-design@4.1.1/dist/js/bootstrap-material-design.js"
            integrity="sha384-CauSuKpEqAFajSpkdjv3z9t8E7RlpJ1UP0lKM/+NdtSarroVKu069AlsRPKkFBz9"
            crossorigin="anonymous"></script>
    <script>
        $(document).ready(function () {
            $('body').bootstrapMaterialDesign();
        });
    </script>
</#macro>