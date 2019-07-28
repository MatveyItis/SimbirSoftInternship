<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring>

<@c.common "Registration">
    <body>
    <@c.navbar/>
    <div class="container pt-5 col-md-5">
        <h3>Registration</h3>
        <form action="/registration" method="post">
            <@spring.bind "userForm"/>
            <div class="form-group">
                <label for="login">Login</label>
                <@spring.formInput "userForm.login" 'class="form-control" id="login" placeholder="Enter login"' />
                <small id="login" class="form-text text-muted">We'll never share your login with anyone else.</small>
                <@spring.showErrors "userForm.login"/>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <@spring.formPasswordInput "userForm.password" 'class="form-control" id="password" placeholder="Enter password"' />
                <@spring.showErrors "userForm.password"/>
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
            <a role="button" href="/login" class="btn btn-secondary">Login</a>
        </form>
    </div>
    <@c.scripts/>
    </body>
</@c.common>