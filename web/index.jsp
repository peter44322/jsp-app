<html>
<head>
    <title>$Title$</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="content">
    <div class="flex-center position-ref full-height">
        <div class="title">
            Students
        </div>
        <div class="form">
            <% if (session.getAttribute("error") != null) { %>
            <div class="error">
                <%= session.getAttribute("error")%>
                <% session.removeAttribute("error"); %>
            </div>
            <% }%>
            <form method="post" action="validate">
                <div class="form-group">
                    <input required type="number" name="id" placeholder="Student ID">
                </div>
                <div class="form-group">
                    <input required type="password" name="password" placeholder="Password">
                </div>
                <div class="form-group">
                    <input type="submit">
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
