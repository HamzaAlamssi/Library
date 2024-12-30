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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/viewSharedBooks")
public class ViewSharedBooksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");

        if (username == null) {
            response.getWriter().println("Please log in to view shared books.");
            return;
        }

        List<Map<String, Object>> sharedBooks = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Get user ID of the logged-in patron
            String getUserSql = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement getUserStmt = conn.prepareStatement(getUserSql);
            getUserStmt.setString(1, username);
            ResultSet rs1 = getUserStmt.executeQuery();
            if (!rs1.next()) {
                response.getWriter().println("Error: Invalid user.");
                return;
            }
            int userId = rs1.getInt("user_id");

            // Fetch shared books
            String sql = "SELECT b.title, b.author, sb.comment, u.username AS shared_by, sb.share_date " +
                    "FROM shared_books sb " +
                    "JOIN books b ON sb.book_id = b.book_id " +
                    "JOIN users u ON sb.shared_by = u.user_id " +
                    "WHERE sb.shared_with = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs2 = stmt.executeQuery();

            while (rs2.next()) {
                Map<String, Object> sharedBook = new HashMap<>();
                sharedBook.put("title", rs2.getString("title"));
                sharedBook.put("author", rs2.getString("author"));
                sharedBook.put("comment", rs2.getString("comment"));
                sharedBook.put("shared_by", rs2.getString("shared_by"));
                sharedBook.put("share_date", rs2.getTimestamp("share_date"));
                sharedBooks.add(sharedBook);
            }

            // Set sharedBooks as a request attribute
            request.setAttribute("sharedBooks", sharedBooks);

            // Forward to JSP
            request.getRequestDispatcher("ViewSharedBooks.jsp").forward(request, response);

            rs1.close();
            rs2.close();
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while fetching shared books.");
            request.getRequestDispatcher("ViewSharedBooks.jsp").forward(request, response);
        }
    }
}
