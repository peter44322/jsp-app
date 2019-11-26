<%@ page import="DB.Connection" %>
<html>
<head>
    <title>$Title$</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="content">
    <div class="flex-center position-ref full-height">
        <div class="title">
            <%= session.getAttribute("student_id") %>
        </div>
        <button
                <%=
                new Connection().from("students")
                        .join("student_question_answer", "student_question_answer.student_id", "students.id")
                        .where("student_id", "=", (String) session.getAttribute("student_id"))
                        .count() > 0 ? "disabled" : ""
                %>
                onclick="window.location.href = 'exam.jsp'">
            Start Exam
        </button>
    </div>
</div>
</body>
</html>
