package util;

import jakarta.servlet.annotation.*;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    private void returnBook(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        ServletContext context = getServletContext();
        int fineRate = Integer.parseInt(context.getInitParameter("fineRate")); // Retrieve fine rate from configuration

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

            // Calculate fine using the configured fine rate
            long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
            double fine = overdueDays > 0 ? overdueDays * fineRate : 0.0;

            // Update borrow_transactions
            String updateTransactionSql = "UPDATE borrow_transactions SET return_date = ?, fine = ? WHERE transaction_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateTransactionSql);
            updateStmt.setDate(1, Date.valueOf(returnDate));
            updateStmt.setDouble(2, fine);
            updateStmt.setString(3, transactionId);
            updateStmt.executeUpdate();

            // Insert fine record if applicable
            if (fine > 0) {
                String insertFineSql = "INSERT INTO fines (transaction_id, amount, status) VALUES (?, ?, 'Unpaid')";
                PreparedStatement insertFineStmt = conn.prepareStatement(insertFineSql);
                insertFineStmt.setString(1, transactionId);
                insertFineStmt.setDouble(2, fine);
                insertFineStmt.executeUpdate();
                insertFineStmt.close();
            }

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
