<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="header.jsp" %><html>
<main role="main">
    <div class="container">
        <div class="row justify-content-md-center m-5">
            <div class="col-md-auto">
                <form method="post" action="signup">
                    <div class="mb-3">
                        <label for="username" class="form-label">Имя</label>
                        <input type="text" class="form-control" id="username" name="username">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Пароль</label>
                        <input type="password" class="form-control" id="password" name="password">
                    </div>
                    <input type="submit" class="btn btn-primary" value="Зарегистрироваться"/>
                </form>
            </div>
        </div>
    </div>
</main>
<%@include file="footer.jsp" %><html>