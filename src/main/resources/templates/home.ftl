<#import "parts/common.ftl" as c>

<@c.common "Login">
    <h2>Welcome to Chat!</h2>
    <div class="container pt-5 col-md-6">
        <form action="/search_video" method="post">
            <label for="video_name">Type name of video..</label>
            <input type="text" class="form-control" id="video_name" name="videoName">
            <button type="submit" class="btn btn-success">Search</button>
        </form>
    </div>
    <div class="container col-md-7">
        <#if url??>
            <a href="${url}">${url}</a>
        <#else>
            <p>No content</p>
        </#if>
    </div>
</@c.common>