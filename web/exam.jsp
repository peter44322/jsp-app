<%@ page import="DB.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.SQLException" %>
<%
    String studentId = (String) session.getAttribute("student_id");
    ResultSet question = new Connection().from("questions").orderBy("RAND()").limit("1").getResultSet();
    question.next();
    int question_id = question.getInt("id");
    String question_text = question.getString("text");
    Connection rightAnswer = new Connection()
            .from("answers")
            .where("question_id", "=", String.valueOf(question_id))
            .orderBy("RAND()").where("correct", "=", "1").limit("1");

    Connection wrongAnswers = new Connection()
            .from("answers")
            .where("question_id", "=", String.valueOf(question_id))
            .orderBy("RAND()").where("correct", "=", "0").limit("2");

    ResultSet allAnswers = new Connection().from(rightAnswer.union(wrongAnswers)).orderBy("RAND()").getResultSet();

    ArrayList<String> columns = new ArrayList<>();
    columns.add("student_id");
    columns.add("question_id");
    columns.add("answer_id");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="content">
    <div class="flex-center position-ref full-height">
        <div class="title">
            <%=  question_text %>
        </div>
        <div>
            <% while (allAnswers.next()) { %>
            <input type="checkbox">
            <label>
                <%= allAnswers.getString("text") %>
            </label>
            <%
                    try {
                        ArrayList<String> values =  new ArrayList<>();
                        values.add(studentId);
                        values.add(String.valueOf(question_id));
                        values.add(allAnswers.getString("id"));
                        new Connection().insert("student_question_answer", columns,values);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } %>
        </div>
        <button onclick="window.location.href = 'student.jsp'"> Back</button>

    </div>
</div>
</body>
</html>
