<#import "parts/common.ftl" as c>

<@c.common "Login">
    <body>
    <@c.navbar/>
    <div class="container pt-5 col-md-5">
        <h3>Log in</h3>
        <form action="/login" method="post">
            <div class="form-group">
                <label for="login">Login</label>
                <input type="text" class="form-control" name="login" id="login" placeholder="Enter login">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" class="form-control" name="password" id="password" placeholder="Enter password">
            </div>
            <div class="checkbox">
                <label>
                    <input type="checkbox" id="remember-me" name="remember-me"> Remember me
                </label>
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
            <a role="button" href="/registration" class="btn btn-secondary">Registration</a>
        </form>
    </div>
    <@c.scripts/>
    </body>
</@c.common>