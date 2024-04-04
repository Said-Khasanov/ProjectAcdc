<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="header.jsp" %>
<main role="main">
    <div class="container">
        <div class="row justify-content-md-center m-5">
            <div class="col-md-auto">
                <c:if test="${!sessionScope.answers.isEmpty()}">
                    <img src="static/quest_image.jpg" alt="question image" height="200px"/>
                </c:if>
            </div>
        </div>
        <div class="row justify-content-md-center m-5">
            <div class="col-md-auto">
                <h2><c:out value="${sessionScope.question.getText()}"/></h2>
            </div>
        </div>

        <div class="row justify-content-md-center m-5">
            <c:forEach items="${sessionScope.answers}" var="i">
                <div class="col-md-2">
                    <button type="button" class="btn btn-light" onclick="sendAnswer(${i.getId()})">
                        <c:out value="${i.getText()}"/>
                    </button>
                </div>
            </c:forEach>
        </div>

        <div class="row justify-content-md-center m-5">
            <c:if test="${sessionScope.answers.isEmpty()}">
                <div class="col-md-auto">
                    <button type="button" class="btn btn-primary" onclick="document.location='/restart'">Начать заново</button>
                </div>
                <div class="col-md-auto">
                    <button type="button" class="btn btn-primary" onclick="document.location='/quests-list'">Квесты</button>
                </div>
            </c:if>
        </div>
    </div>
</main>
<%@include file="footer.jsp" %>