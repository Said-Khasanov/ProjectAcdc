<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="header.jsp" %>
<main role="main">
    <div class="container">
        <div class="row justify-content-md-center m-5">
            <div class="col-md-auto">
                <h1>Добро пожаловать!</h1>
            </div>
        </div>

        <div class="row justify-content-md-center m-5">
            <div class="col-md-auto">
                <c:if test="${sessionScope.userId == null}">
                    <p>
                        Чтобы начать играть, <a href="/login">войдите</a> или <a href="/signup">зарегистрируйтесь</a>
                    </p>
                </c:if>
            </div>
        </div>
    </div>
</main>
<%@include file="footer.jsp" %>