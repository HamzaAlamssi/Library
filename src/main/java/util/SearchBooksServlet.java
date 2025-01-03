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



@WebServlet("/searchBooks")
public class SearchBooksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("query");
        String searchType = request.getParameter("type"); // "title" or "author"

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT * FROM books WHERE " + searchType + " LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchQuery + "%");
            ResultSet rs = stmt.executeQuery();

            response.setContentType("text/html");
            response.getWriter().println("<table border='1'><tr><th>Title</th><th>Author</th><th>Availability</th></tr>");
            while (rs.next()) {
                response.getWriter().println("<tr>");
                response.getWriter().println("<td>" + rs.getString("title") + "</td>");
                response.getWriter().println("<td>" + rs.getString("author") + "</td>");
                response.getWriter().println("<td>" + (rs.getBoolean("availability") ? "Available" : "Not Available") + "</td>");
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
