<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.javarush.khasanov.entity.GameState" %>
<%@include file="header.jsp" %>
<main role="main">
    <div class="container">
        <div class="row justify-content-md-center m-5">
            <div class="col-md-auto">
                <div class="col-md-auto text-center"><h1>Статистика по пользователям</h1></div>
            </div>
        </div>

        <div class="row justify-content-md-center m-5">
            <div class="col-md-auto">
                <table class="table table-light">
                    <thead>
                    <tr>
                        <th scope="col">Пользователь</th>
                        <th scope="col">Играет</th>
                        <th scope="col">Сыграл</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="statsMapEntry" items="${sessionScope.statsMap.entrySet()}">
                        <tr>
                            <th scope="row">${statsMapEntry.getKey()}</th>
                            <td class="table-info">${statsMapEntry.getValue().get(GameState.PLAYING)}</td>
                            <td class="table-success">${statsMapEntry.getValue().get(GameState.FINISHED)}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>
<%@include file="footer.jsp" %>