<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quests</title>
</head>
<body>
<div class="container">
    <ul>
        <c:forEach var="quest" items="${sessionScope.questsList}">
            <li><a href="quest?id=${quest.getId()}">${quest.getTitle()}</a></li>
        </c:forEach>
    </ul>
</div>
</body>
</html>
