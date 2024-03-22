<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quest</title>
</head>
<body>
    <div class="game">
        <p><c:out value="${sessionScope.question.getText()}"/></p>
        <ul>
            <c:forEach items="${sessionScope.answers}" var="i">
                <li><button><c:out value="${i.getText()}"/></button></li><br/>
            </c:forEach>
        </ul>
    </div>
</body>
</html>