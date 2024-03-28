<%@ page contentType="text/html;charset=UTF-8" %>
<footer class="bg-body-tertiary text-center">
    <div class="text-center p-3" style="background-color: rgba(0, 0, 0, 0.05);">
        © 2024 Said Khasanov
    </div>
</footer>
<script src="static/jquery-3.6.0.min.js"></script>
<script>
    function sendAnswer(id) {
        $.ajax({
            method: 'POST',
            url: '/quest',
            dataType: 'html',
            headers: {'answerId': id},
            async: false,
            success: function () {
                location.reload();
            }
        });
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous">
</script>
<svg xmlns="http://www.w3.org/2000/svg" width="348" height="225" viewBox="0 0 348 225" preserveAspectRatio="none"
     style="display: none; visibility: hidden; position: absolute; top: -100%; left: -100%;">
    <defs>
        <style type="text/css"></style>
    </defs>
    <text x="0" y="17" style="font-weight:bold;font-size:17pt;font-family:Arial, Helvetica, Open Sans, sans-serif">
        Thumbnail
    </text>
</svg>
</body>
</html>
