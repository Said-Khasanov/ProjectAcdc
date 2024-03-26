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
    <script src="static/jquery-3.6.0.min.js"></script>
    <script>
        function sendAnswer(id) {
            $.ajax({
                url: '/quest',
                method: 'post',
                dataType: 'html',
                headers: {'answerId': id},
                async: false,
                success: function () {
                    location.reload();
                }
            });
        }
    </script>
</body>
</html>