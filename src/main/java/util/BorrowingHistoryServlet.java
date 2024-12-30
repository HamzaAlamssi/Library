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

@WebServlet("/borrowingHistory")
public class BorrowingHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");

        if (username == null) {
            response.getWriter().println("Please log in to view your borrowing history.");
            return;
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Corrected SQL query
            String sql = "SELECT b.title, bt.borrow_date, bt.due_date, bt.return_date, bt.fine " +
                    "FROM borrow_transactions bt " +
                    "JOIN books b ON bt.book_id = b.book_id " +
                    "WHERE bt.username = (SELECT username FROM users WHERE username = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            response.setContentType("text/html");
            response.getWriter().println("<table border='1'><tr><th>Title</th><th>Borrow Date</th><th>Due Date</th><th>Return Date</th><th>Fine</th></tr>");

            // Check if there are any results
            boolean hasRows = false;

            while (rs.next()) {
                hasRows = true; // Mark that rows exist
                response.getWriter().println("<tr>");
                response.getWriter().println("<td>" + rs.getString("title") + "</td>");
                response.getWriter().println("<td>" + rs.getDate("borrow_date") + "</td>");
                response.getWriter().println("<td>" + rs.getDate("due_date") + "</td>");
                response.getWriter().println("<td>" + (rs.getDate("return_date") != null ? rs.getDate("return_date") : "Not Returned") + "</td>");
                response.getWriter().println("<td>" + rs.getDouble("fine") + "</td>");
                response.getWriter().println("</tr>");
            }

            if (!hasRows) {
                response.getWriter().println("<tr><td colspan='5'>No borrowing history found.</td></tr>");
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
