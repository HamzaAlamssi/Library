package util;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/addReview")
public class AddReviewServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");

        if (username == null) {
            response.getWriter().println("Please log in to add a review.");
            return;
        }

        int bookId = Integer.parseInt(request.getParameter("book_Id"));
        String comment = request.getParameter("comment");
        int rating = Integer.parseInt(request.getParameter("rating"));

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Get user_id for the logged-in user
            String getUserSql = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement getUserStmt = conn.prepareStatement(getUserSql);
            getUserStmt.setString(1, username);
            var rs = getUserStmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");

                // Insert review into the database
                String sql = "INSERT INTO reviews (book_id, user_id, comment, rating) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, bookId);
                stmt.setInt(2, userId);
                stmt.setString(3, comment);
                stmt.setInt(4, rating);

                int rows = stmt.executeUpdate();
                response.getWriter().println(rows > 0 ? "Review added successfully." : "Failed to add review.");

                stmt.close();
            } else {
                response.getWriter().println("User not found.");
            }

            rs.close();
            getUserStmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
