<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="header.jsp" %>
<main role="main">
    <div class="container">
        <form method="post" action="create-quest">
            <div class="row justify-content-md-center mt-5">
                <div class="col-md-5">
                    <div class="mb-4">
                        <label for="example" class="form-label">Пример</label>
                        <textarea class="form-control" id="example" rows="20" disabled style="resize: none">
t: Название квеста

q1: Текст вопроса 1
a>q2: Текст ответа (Ответ приведёт к вопросу q2)
a>e1: Текст ответа (Ответ приведёт к концовке e1)
e1: Текст концовки 1 (Концовки пишутся после ответов на вопрос)

q2: Текст вопроса 2
a>e2: Текст ответа
a>e3: Текст ответа
e2: Текст концовки 2
e3: Текст концовки 3
                    </textarea>
                    </div>
                </div>

                <div class="col-md-7">
                    <div class="mb-3">
                        <label for="questText" class="form-label">Новый квест</label>
                        <textarea class="form-control" id="questText" name="questText" rows="20" required></textarea>
                    </div>
                </div>
            </div>

            <div class="row justify-content-md-end mb-5">
                <div class="col-md-auto">
                    <input type="submit" class="btn btn-success" value="Создать"/>
                </div>
            </div>
        </form>
    </div>
</main>
<%@include file="footer.jsp" %>