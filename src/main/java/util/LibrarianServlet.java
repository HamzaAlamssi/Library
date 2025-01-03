package util;

import jakarta.servlet.ServletContext;
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

        // Dynamically build SQL query based on provided parameters
        StringBuilder sql = new StringBuilder("UPDATE books SET ");
        boolean hasUpdates = false;

        if (request.getParameter("title") != null && !request.getParameter("title").trim().isEmpty()) {
            sql.append("title = ?, ");
            hasUpdates = true;
        }
        if (request.getParameter("author") != null && !request.getParameter("author").trim().isEmpty()) {
            sql.append("author = ?, ");
            hasUpdates = true;
        }
        if (request.getParameter("genre") != null && !request.getParameter("genre").trim().isEmpty()) {
            sql.append("genre = ?, ");
            hasUpdates = true;
        }
        if (request.getParameter("isbn") != null && !request.getParameter("isbn").trim().isEmpty()) {
            sql.append("isbn = ?, ");
            hasUpdates = true;
        }
        if (request.getParameter("year") != null && !request.getParameter("year").trim().isEmpty()) {
            sql.append("year_of_publication = ?, ");
            hasUpdates = true;
        }
        if (request.getParameter("availability") != null && !request.getParameter("availability").trim().isEmpty()) {
            sql.append("availability = ?, ");
            hasUpdates = true;
        }

        // Remove trailing comma and space, add WHERE clause
        if (!hasUpdates) {
            response.getWriter().println("No fields to update.");
            return;
        }

        sql.setLength(sql.length() - 2); // Remove last ", "
        sql.append(" WHERE book_id = ?");

        PreparedStatement stmt = conn.prepareStatement(sql.toString());

        // Set parameters for non-empty fields
        int paramIndex = 1;
        if (request.getParameter("title") != null && !request.getParameter("title").trim().isEmpty()) {
            stmt.setString(paramIndex++, request.getParameter("title"));
        }
        if (request.getParameter("author") != null && !request.getParameter("author").trim().isEmpty()) {
            stmt.setString(paramIndex++, request.getParameter("author"));
        }
        if (request.getParameter("genre") != null && !request.getParameter("genre").trim().isEmpty()) {
            stmt.setString(paramIndex++, request.getParameter("genre"));
        }
        if (request.getParameter("isbn") != null && !request.getParameter("isbn").trim().isEmpty()) {
            stmt.setString(paramIndex++, request.getParameter("isbn"));
        }
        if (request.getParameter("year") != null && !request.getParameter("year").trim().isEmpty()) {
            stmt.setInt(paramIndex++, Integer.parseInt(request.getParameter("year")));
        }
        if (request.getParameter("availability") != null && !request.getParameter("availability").trim().isEmpty()) {
            stmt.setBoolean(paramIndex++, Boolean.parseBoolean(request.getParameter("availability")));
        }

        // Set book ID
        stmt.setInt(paramIndex, bookId);

        // Execute the update
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
        // Retrieve the fine rate from web.xml via ServletContext
        ServletContext context = request.getServletContext();
        int fineRate = Integer.parseInt(context.getInitParameter("fineRate")); // Fine rate defined in web.xml

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

            // Update the borrow transaction with return details and calculated fine
            String updateTransactionSql = "UPDATE borrow_transactions SET return_date = ?, fine = ? WHERE transaction_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateTransactionSql);
            updateStmt.setDate(1, Date.valueOf(returnDate));
            updateStmt.setDouble(2, fine);
            updateStmt.setString(3, transactionId);
            updateStmt.executeUpdate();

            // Update the book availability to true
            String updateBookSql = "UPDATE books SET availability = TRUE WHERE book_id = ?";
            PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
            updateBookStmt.setInt(1, bookId);
            updateBookStmt.executeUpdate();

            // Send success response to the client
            response.getWriter().println("Book returned successfully. Fine: " + fine + " JD.");
        } else {
            // Handle case where the transaction ID is invalid
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
        response.getWriter().println("<tr><th>Fine ID</th><th>Transaction ID</th><th>Amount</th><th>Status</th></tr>");

        while (rs.next()) {
            response.getWriter().println("<tr>");
            response.getWriter().println("<td>" + rs.getInt("fine_id") + "</td>");
            response.getWriter().println("<td>" + rs.getString("transaction_id") + "</td>");
            response.getWriter().println("<td>" + rs.getDouble("amount") + "</td>");
            response.getWriter().println("<td>" + rs.getString("status") + "</td>");
            response.getWriter().println("</tr>");
        }
        response.getWriter().println("</table>");

        rs.close();
        stmt.close();
    }
}
