//package util;
//
//import jakarta.servlet.annotation.*;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.UUID;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import util.DatabaseConnection;
//
//@WebServlet("/borrowBook")
//public class BorrowBookServlet extends HttpServlet {
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String username = (String) request.getSession().getAttribute("username");
//        String bookId = request.getParameter("bookId");
//
//        if (username == null) {
//            response.getWriter().println("Please log in to borrow a book.");
//            return;
//        }
//
//        try {
//            Connection conn = DatabaseConnection.getInstance().getConnection();
//
//            // Check if the book is available
//            String checkAvailabilitySql = "SELECT availability FROM books WHERE book_id = ?";
//            PreparedStatement checkStmt = conn.prepareStatement(checkAvailabilitySql);
//            checkStmt.setString(1, bookId);
//            ResultSet rs = checkStmt.executeQuery();
//
//            if (rs.next() && rs.getBoolean("availability")) {
//                // Check if the user has borrowed less than 3 books
//                String countBorrowedBooksSql = "SELECT COUNT(*) AS count FROM borrow_transactions WHERE username = ? AND return_date IS NULL";
//                PreparedStatement countStmt = conn.prepareStatement(countBorrowedBooksSql);
//                countStmt.setString(1, username);
//                ResultSet countRs = countStmt.executeQuery();
//                countRs.next();
//
//                if (countRs.getInt("count") < 3) {
//                    // Borrow the book
//                    String transactionId = "BT" + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
//                    LocalDate borrowDate = LocalDate.now();
//                    LocalDate dueDate = borrowDate.plusWeeks(2);
//
//                    String borrowSql = "INSERT INTO borrow_transactions (transaction_id, username, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?, ?)";
//                    PreparedStatement borrowStmt = conn.prepareStatement(borrowSql);
//                    borrowStmt.setString(1, transactionId);
//                    borrowStmt.setString(2, username);
//                    borrowStmt.setString(3, bookId);
//                    borrowStmt.setString(4, borrowDate.toString());
//                    borrowStmt.setString(5, dueDate.toString());
//
//                    borrowStmt.executeUpdate();
//
//                    // Update book availability
//                    String updateBookSql = "UPDATE books SET availability = FALSE WHERE book_id = ?";
//                    PreparedStatement updateStmt = conn.prepareStatement(updateBookSql);
//                    updateStmt.setString(1, bookId);
//                    updateStmt.executeUpdate();
//
//                    response.getWriter().println("Book borrowed successfully. Transaction ID: " + transactionId);
//                } else {
//                    response.getWriter().println("You have reached the maximum borrowing limit of 3 books.");
//                }
//
//                countRs.close();
//                countStmt.close();
//            } else {
//                response.getWriter().println("Book is not available for borrowing.");
//            }
//
//            rs.close();
//            checkStmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.getWriter().println("Error: " + e.getMessage());
//        }
//    }
//}
