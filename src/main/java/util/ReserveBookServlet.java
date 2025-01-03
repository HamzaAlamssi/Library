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
import java.time.LocalDate;

@WebServlet("/reserveBook")
public class ReserveBookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        if (username == null) {
            response.getWriter().println("Please log in to reserve a book.");
            return;
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Check if the book is available
            String bookSql = "SELECT availability FROM books WHERE book_id = ?";
            PreparedStatement bookStmt = conn.prepareStatement(bookSql);
            bookStmt.setInt(1, bookId);
            ResultSet bookRs = bookStmt.executeQuery();

            if (bookRs.next()) {
                if (!bookRs.getBoolean("availability")) {
                    response.getWriter().println("Book is not available for reservation.");
                    return;
                }
            } else {
                response.getWriter().println("Book not found.");
                return;
            }

            // Fetch the user_id of the logged-in user
            String userSql = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                int userId = userRs.getInt("user_id");
                LocalDate reservationDate = LocalDate.now();

                // Add reservation
                String reserveSql = "INSERT INTO book_reservations (book_id, user_id, reservation_date) VALUES (?, ?, ?)";
                PreparedStatement reserveStmt = conn.prepareStatement(reserveSql);
                reserveStmt.setInt(1, bookId);
                reserveStmt.setInt(2, userId);
                reserveStmt.setDate(3, java.sql.Date.valueOf(reservationDate));

                int rows = reserveStmt.executeUpdate();
                if (rows > 0) {
                    // Update book availability
                    String updateBookSql = "UPDATE books SET availability = FALSE WHERE book_id = ?";
                    PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
                    updateBookStmt.setInt(1, bookId);
                    updateBookStmt.executeUpdate();

                    response.getWriter().println("Book reserved successfully.");
                } else {
                    response.getWriter().println("Failed to reserve book.");
                }

                reserveStmt.close();
            } else {
                response.getWriter().println("User not found.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
