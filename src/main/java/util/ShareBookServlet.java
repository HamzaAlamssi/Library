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

@WebServlet("/shareBook")
public class ShareBookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");

        if (username == null) {
            response.getWriter().println("Please log in to share a book.");
            return;
        }

        int bookId = Integer.parseInt(request.getParameter("book-Id"));
        String sharedWithUsername = request.getParameter("sharedWith");
        String comment = request.getParameter("notes");

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Get user IDs
            String getUserSql = "SELECT user_id, role FROM users WHERE username = ?";
            PreparedStatement getUserStmt = conn.prepareStatement(getUserSql);

            // Get shared_by user ID
            getUserStmt.setString(1, username);
            ResultSet rs1 = getUserStmt.executeQuery();
            if (!rs1.next()) {
                response.getWriter().println("Error: Invalid sharing user.");
                return;
            }
            int sharedById = rs1.getInt("user_id");

            // Get shared_with user ID and role
            getUserStmt.setString(1, sharedWithUsername);
            ResultSet rs2 = getUserStmt.executeQuery();
            if (!rs2.next()) {
                response.getWriter().println("Error: Invalid receiving user.");
                return;
            }
            int sharedWithId = rs2.getInt("user_id");
            String sharedWithRole = rs2.getString("role");

            // Check if the shared_with user is a patron
            if (!"patron".equalsIgnoreCase(sharedWithRole)) {
                response.getWriter().println("Error: You can only share books with users who are patrons.");
                return;
            }

            // Insert into shared_books table
            String sql = "INSERT INTO shared_books (book_id, shared_by, shared_with, comment) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            stmt.setInt(2, sharedById);
            stmt.setInt(3, sharedWithId);
            stmt.setString(4, comment);

            int rows = stmt.executeUpdate();
            response.getWriter().println(rows > 0 ? "Book shared successfully." : "Failed to share the book.");

            rs1.close();
            rs2.close();
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
