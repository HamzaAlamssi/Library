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

@WebServlet("/viewReviews")
public class ViewReviewsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        List<Map<String, Object>> reviewList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT u.username, r.comment, r.rating, r.review_date " +
                    "FROM reviews r JOIN users u ON r.user_id = u.user_id " +
                    "WHERE r.book_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> review = new HashMap<>();
                review.put("username", rs.getString("username"));
                review.put("comment", rs.getString("comment"));
                review.put("rating", rs.getInt("rating"));
                review.put("review_date", rs.getTimestamp("review_date"));
                reviewList.add(review);
            }

            rs.close();
            stmt.close();

            // Set reviews and bookId as request attributes
            request.setAttribute("reviews", reviewList);
            request.setAttribute("bookId", bookId);

            // Forward to JSP
            request.getRequestDispatcher("ViewReviews.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while fetching reviews.");
            request.getRequestDispatcher("ViewReviews.jsp").forward(request, response);
        }
    }
}
