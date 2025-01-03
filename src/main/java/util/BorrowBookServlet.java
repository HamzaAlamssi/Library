package util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DatabaseConnection;

@WebServlet("/borrowBook")
public class BorrowBookServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();

        // Retrieve configuration values
        int maxBorrowLimit = ConfigUtil.getMaxBorrowLimit(context);
        int borrowingPeriod = ConfigUtil.getBorrowingPeriod(context);

        String username = (String) request.getSession().getAttribute("username");
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        if (username == null) {
            response.getWriter().println("Please log in to borrow a book.");
            return;
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Check borrowing count
            String countSql = "SELECT COUNT(*) AS borrowedCount FROM borrow_transactions WHERE username = ? AND return_date IS NULL";
            PreparedStatement countStmt = conn.prepareStatement(countSql);
            countStmt.setString(1, username);
            ResultSet rs = countStmt.executeQuery();
            rs.next();

            if (rs.getInt("borrowedCount") >= maxBorrowLimit) {
                response.getWriter().println("You have reached the maximum borrowing limit of " + maxBorrowLimit + " books.");
                return;
            }

            // Insert borrow transaction
            String transactionId = "BT" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(borrowingPeriod);
            String borrowSql = "INSERT INTO borrow_transactions (transaction_id, username, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement borrowStmt = conn.prepareStatement(borrowSql);

            borrowStmt.setString(1, transactionId);
            borrowStmt.setString(2, username);
            borrowStmt.setInt(3, bookId);
            borrowStmt.setDate(4, java.sql.Date.valueOf(borrowDate));
            borrowStmt.setDate(5, java.sql.Date.valueOf(dueDate));

            int rows = borrowStmt.executeUpdate();
            if (rows > 0) {
                response.getWriter().println("Book borrowed successfully. Due date: " + dueDate);
            } else {
                response.getWriter().println("Failed to borrow the book.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

}
