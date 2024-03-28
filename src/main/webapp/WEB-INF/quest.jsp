<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="header.jsp" %>
<main role="main">
    <div class="container">
        <p><c:out value="${sessionScope.question.getText()}"/></p>
        <ul>
            <c:forEach items="${sessionScope.answers}" var="i">
                <li>
                    <button onclick="sendAnswer(${i.getId()})">
                        <c:out value="${i.getText()}"/>
                    </button>
                </li>
                <br/>
            </c:forEach>
        </ul>
        <div>
            <c:if test="${sessionScope.answers.isEmpty()}">
                <button class="restart" onclick="document.location='/restart'">Начать заново</button>
            </c:if>
        </div>
    </div>
</main>
<%@include file="footer.jsp" %>