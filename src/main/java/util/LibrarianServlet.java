package util;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/librarian")
public class LibrarianServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            switch (action) {
                case "addBook":
                    addBook(request, response, conn);
                    break;
                case "editBook":
                    editBook(request, response, conn);
                    break;
                case "removeBook":
                    removeBook(request, response, conn);
                    break;
                case "returnBook":
                    returnBook(request, response, conn);
                    break;
                case "updateFineStatus":
                    updateFineStatus(request, response, conn);
                    break;
                default:
                    response.getWriter().println("Invalid action.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            switch (action) {
                case "viewAllBooks":
                    viewAllBooks(request, response, conn);
                    break;
                case "viewPatronHistory":
                    viewPatronHistory(request, response, conn);
                    break;
                case "viewFineReports":
                    viewFineReports(request, response, conn);
                    break;
                default:
                    response.getWriter().println("Invalid action.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    // Add Book
    private void addBook(HttpServletRequest request, HttpServletResponse response, Connection conn) throws Exception {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String genre = request.getParameter("genre");
        String isbn = request.getParameter("isbn");
        int year = Integer.parseInt(request.getParameter("year"));

        String sql = "INSERT INTO books (title, author, genre, isbn, year_of_publication, availability) VALUES (?, ?, ?, ?, ?, TRUE)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, title);
        stmt.setString(2, author);
        stmt.setString(3, genre);
        stmt.setString(4, isbn);
        stmt.setInt(5, year);

        int rows = stmt.executeUpdate();
        response.getWriter().println(rows > 0 ? "Book added successfully." : "Failed to add book.");
    }

    // Edit Book
    private void editBook(HttpServletRequest request, HttpServletResponse response, Connection conn) throws Exception {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String genre = request.getParameter("genre");
        String isbn = request.getParameter("isbn");
        int year = Integer.parseInt(request.getParameter("year"));
        boolean availability = Boolean.parseBoolean(request.getParameter("availability"));

        String sql = "UPDATE books SET title = ?, author = ?, genre = ?, isbn = ?, year_of_publication = ?, availability = ? WHERE book_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, title);
        stmt.setString(2, author);
        stmt.setString(3, genre);
        stmt.setString(4, isbn);
        stmt.setInt(5, year);
        stmt.setBoolean(6, availability);
        stmt.setInt(7, bookId);

        int rows = stmt.executeUpdate();
        response.getWriter().println(rows > 0 ? "Book updated successfully." : "Failed to update book.");
    }

    // Remove Book
    private void removeBook(HttpServletRequest request, HttpServletResponse response, Connection conn) throws Exception {
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        String sql = "DELETE FROM books WHERE book_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, bookId);

        int rows = stmt.executeUpdate();
        response.getWriter().println(rows > 0 ? "Book removed successfully." : "Failed to remove book.");
    }

    // Return Book
    private void returnBook(HttpServletRequest request, HttpServletResponse response, Connection conn) throws Exception {
        String transactionId = request.getParameter("transactionId");

        String fetchTransactionSql = "SELECT book_id, due_date FROM borrow_transactions WHERE transaction_id = ?";
        PreparedStatement fetchStmt = conn.prepareStatement(fetchTransactionSql);
        fetchStmt.setString(1, transactionId);
        ResultSet rs = fetchStmt.executeQuery();

        if (rs.next()) {
            int bookId = rs.getInt("book_id");
            LocalDate dueDate = rs.getDate("due_date").toLocalDate();
            LocalDate returnDate = LocalDate.now();

            long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
            double fine = overdueDays > 0 ? overdueDays * 1.0 : 0.0;

            String updateTransactionSql = "UPDATE borrow_transactions SET return_date = ?, fine = ? WHERE transaction_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateTransactionSql);
            updateStmt.setDate(1, Date.valueOf(returnDate));
            updateStmt.setDouble(2, fine);
            updateStmt.setString(3, transactionId);

            updateStmt.executeUpdate();

            String updateBookSql = "UPDATE books SET availability = TRUE WHERE book_id = ?";
            PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
            updateBookStmt.setInt(1, bookId);
            updateBookStmt.executeUpdate();

            response.getWriter().println("Book returned successfully. Fine: " + fine + " JD.");
        } else {
            response.getWriter().println("Invalid transaction ID.");
        }
    }

    // Update Fine Status
    private void updateFineStatus(HttpServletRequest request, HttpServletResponse response, Connection conn) throws Exception {
        int fineId = Integer.parseInt(request.getParameter("fineId"));

        String sql = "UPDATE fines SET status = 'Paid' WHERE fine_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, fineId);

        int rows = stmt.executeUpdate();
        response.getWriter().println(rows > 0 ? "Fine marked as paid." : "Failed to update fine status.");
    }

    // View All Books
    private void viewAllBooks(HttpServletRequest request, HttpServletResponse response, Connection conn) throws Exception {
        String sql = "SELECT * FROM books";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        response.setContentType("text/html");
        response.getWriter().println("<table border='1'>");
        response.getWriter().println("<tr><th>ID</th><th>Title</th><th>Author</th><th>Genre</th><th>ISBN</th><th>Year</th><th>Availability</th></tr>");

        while (rs.next()) {
            response.getWriter().println("<tr>");
            response.getWriter().println("<td>" + rs.getInt("book_id") + "</td>");
            response.getWriter().println("<td>" + rs.getString("title") + "</td>");
            response.getWriter().println("<td>" + rs.getString("author") + "</td>");
            response.getWriter().println("<td>" + rs.getString("genre") + "</td>");
            response.getWriter().println("<td>" + rs.getString("isbn") + "</td>");
            response.getWriter().println("<td>" + rs.getInt("year_of_publication") + "</td>");
            response.getWriter().println("<td>" + (rs.getBoolean("availability") ? "Available" : "Not Available") + "</td>");
            response.getWriter().println("</tr>");
        }
        response.getWriter().println("</table>");

        rs.close();
        stmt.close();
    }

    // View Patron History
    private void viewPatronHistory(HttpServletRequest request, HttpServletResponse response, Connection conn) throws Exception {
        String patronId = request.getParameter("patronId");

        String sql = "SELECT * FROM borrow_transactions WHERE patron_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, patronId);
        ResultSet rs = stmt.executeQuery();

        response.setContentType("text/html");
        response.getWriter().println("<table border='1'>");
        response.getWriter().println("<tr><th>Transaction ID</th><th>Book ID</th><th>Borrow Date</th><th>Due Date</th><th>Return Date</th><th>Fine</th></tr>");

        while (rs.next()) {
            response.getWriter().println("<tr>");
            response.getWriter().println("<td>" + rs.getString("transaction_id") + "</td>");
            response.getWriter().println("<td>" + rs.getInt("book_id") + "</td>");
            response.getWriter().println("<td>" + rs.getDate("borrow_date") + "</td>");
            response.getWriter().println("<td>" + rs.getDate("due_date") + "</td>");
            response.getWriter().println("<td>" + rs.getDate("return_date") + "</td>");
            response.getWriter().println("<td>" + rs.getDouble("fine") + "</td>");
            response.getWriter().println("</tr>");
        }
        response.getWriter().println("</table>");

        rs.close();
        stmt.close();
    }

    // View Fine Reports
    private void viewFineReports(HttpServletRequest request, HttpServletResponse response, Connection conn) throws Exception {
        String sql = "SELECT * FROM fines";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        response.setContentType("text/html");
        response.getWriter().println("<table border='1'>");
        response.getWriter().println("<tr><th>Fine ID</th><th>Patron ID</th><th>Amount</th><th>Status</th></tr>");

        while (rs.next()) {
            response.getWriter().println("<tr>");
            response.getWriter().println("<td>" + rs.getInt("fine_id") + "</td>");
            response.getWriter().println("<td>" + rs.getString("patron_id") + "</td>");
            response.getWriter().println("<td>" + rs.getDouble("amount") + "</td>");
            response.getWriter().println("<td>" + rs.getString("status") + "</td>");
            response.getWriter().println("</tr>");
        }
        response.getWriter().println("</table>");

        rs.close();
        stmt.close();
    }
}
