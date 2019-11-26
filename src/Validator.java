import DB.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/validate")
public class Validator extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public Validator() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        Connection student = new Connection().from("students")
                .where("id", "=", id)
                .where("password", "=", password);

        HttpSession session = request.getSession();
        if (student.count() > 0) {
            session.setAttribute("student_id", id);
            session.setAttribute("has_exam",
                    new Connection().from("students")
                            .join("student_question_answer", "student_question_answer.student_id", "students.id")
                            .where("student_id","=",id)
                            .count() > 0
            );
            response.sendRedirect("student.jsp");
        } else {
            session.setAttribute("error", "Wrong Credential");
            response.sendRedirect("index.jsp");
        }

    }

}