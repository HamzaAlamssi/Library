package util;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/fineStatus")
public class FineStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");

        if (username == null) {
            response.getWriter().println("Please log in to view your fine status.");
            return;
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT f.amount, f.status " +
                    "FROM fines f JOIN borrow_transactions bt ON f.transaction_id = bt.transaction_id " +
                    "WHERE bt.username = (SELECT username FROM users WHERE username = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            response.setContentType("text/html");
            response.getWriter().println("<table border='1'><tr><th>Amount</th><th>Status</th></tr>");
            while (rs.next()) {
                response.getWriter().println("<tr>");
                response.getWriter().println("<td>" + rs.getDouble("amount") + "</td>");
                response.getWriter().println("<td>" + rs.getString("status") + "</td>");
                response.getWriter().println("</tr>");
            }
            response.getWriter().println("</table>");

            rs.close();
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
