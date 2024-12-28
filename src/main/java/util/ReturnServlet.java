package util;

import jakarta.servlet.annotation.*;
        import java.io.IOException;
import java.sql.*;
        import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DatabaseConnection;

@WebServlet("/borrowReturn")
public class ReturnServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();

             if ("return".equals(action)) {
                returnBook(request, response, conn);
            } else {
                response.getWriter().println("Invalid action.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    private void borrowBook(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
//        String username = request.getParameter("username");
//        int bookId = Integer.parseInt(request.getParameter("bookId"));
//
//        // Check if user has reached the borrow limit
//        String checkLimitSql = "SELECT COUNT(*) AS count FROM borrow_transactions WHERE username = ? AND return_date IS NULL";
//        PreparedStatement checkLimitStmt = conn.prepareStatement(checkLimitSql);
//        checkLimitStmt.setString(1, username);
//        ResultSet rs = checkLimitStmt.executeQuery();
//        rs.next();
//
//        if (rs.getInt("count") >= 3) {
//            response.getWriter().println("You have already borrowed the maximum number of books.");
//            return;
//        }
//
//        // Check if the book is available
//        String checkBookSql = "SELECT availability FROM books WHERE book_id = ?";
//        PreparedStatement checkBookStmt = conn.prepareStatement(checkBookSql);
//        checkBookStmt.setInt(1, bookId);
//        ResultSet bookRs = checkBookStmt.executeQuery();
//
//        if (bookRs.next() && bookRs.getBoolean("availability")) {
//            // Generate transaction ID
//            String transactionId = "BT" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
//            LocalDate borrowDate = LocalDate.now();
//            LocalDate dueDate = borrowDate.plusWeeks(2);
//
//            // Insert into borrow_transactions
//            String borrowSql = "INSERT INTO borrow_transactions (transaction_id, username, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?, ?)";
//            PreparedStatement borrowStmt = conn.prepareStatement(borrowSql);
//            borrowStmt.setString(1, transactionId);
//            borrowStmt.setString(2, username);
//            borrowStmt.setInt(3, bookId);
//            borrowStmt.setDate(4, Date.valueOf(borrowDate));
//            borrowStmt.setDate(5, Date.valueOf(dueDate));
//
//            borrowStmt.executeUpdate();
//
//            // Update book availability
//            String updateBookSql = "UPDATE books SET availability = FALSE WHERE book_id = ?";
//            PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
//            updateBookStmt.setInt(1, bookId);
//            updateBookStmt.executeUpdate();
//
//            response.getWriter().println("Book borrowed successfully. Transaction ID: " + transactionId);
//        } else {
//            response.getWriter().println("Book is not available.");
//        }
//    }

    private void returnBook(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        String transactionId = request.getParameter("transactionId");

        // Fetch transaction details
        String fetchTransactionSql = "SELECT book_id, due_date FROM borrow_transactions WHERE transaction_id = ?";
        PreparedStatement fetchStmt = conn.prepareStatement(fetchTransactionSql);
        fetchStmt.setString(1, transactionId);
        ResultSet rs = fetchStmt.executeQuery();

        if (rs.next()) {
            int bookId = rs.getInt("book_id");
            LocalDate dueDate = rs.getDate("due_date").toLocalDate();
            LocalDate returnDate = LocalDate.now();

            // Calculate fine
            long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
            double fine = overdueDays > 0 ? overdueDays * 1.0 : 0.0;

            // Update borrow_transactions
            String updateTransactionSql = "UPDATE borrow_transactions SET return_date = ?, fine = ? WHERE transaction_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateTransactionSql);
            updateStmt.setDate(1, Date.valueOf(returnDate));
            updateStmt.setDouble(2, fine);
            updateStmt.setString(3, transactionId);

            updateStmt.executeUpdate();

            // Update book availability
            String updateBookSql = "UPDATE books SET availability = TRUE WHERE book_id = ?";
            PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
            updateBookStmt.setInt(1, bookId);
            updateBookStmt.executeUpdate();

            response.getWriter().println("Book returned successfully. Fine: " + fine + " JD.");
        } else {
            response.getWriter().println("Invalid transaction ID.");
        }
    }
}
