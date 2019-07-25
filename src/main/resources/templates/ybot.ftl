<#import "parts/common.ftl" as c>

<@c.common "Home">
    <body>
    <@c.navbar/>
    <div class="container pt-5 col-md-6">
        <div class="container" align="center">
            <h4>YBot</h4>
            <img src="/img/youtube.svg" alt="youtube" width="300" height="300"><br>
            <small class="pt-2">For all information please type "ybot help"</small>
        </div>
        <br>
        <form action="/ybot_command" method="post">
            <div class="row">
                <div class="form-group col-md-10">
                    <label for="search_request">Youtube Bot</label>
                    <input type="text" class="form-control" id="search_request" name="search_request"
                           placeholder="Enter the command..">
                </div>
                <div class="form-group col-md-2 mt-4">
                    <button type="submit" class="btn btn-raised btn-dark">Enter</button>
                </div>
            </div>
        </form>
    </div>
    <div class="container col-md-6">
        <table class="table table-borderless">
            <tbody>
            <tr>
                <td>
                    <#if url??>
                        <a href="${url}">${url}</a>
                    <#else>
                    </#if>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <@c.scripts/>
    </body>
</@c.common>