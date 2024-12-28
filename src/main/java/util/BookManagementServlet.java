package util;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/bookManagement")
public class BookManagementServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();

            switch (action) {
                case "add":
                    addBook(request, response, conn);
                    break;
                case "edit":
                    editBook(request, response, conn);
                    break;
                case "remove":
                    removeBook(request, response, conn);
                    break;
                default:
                    response.getWriter().println("Invalid action.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();

            if ("viewAll".equals(action)) {
                viewAllBooks(request, response, conn);
            } else {
                response.getWriter().println("Invalid action.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    private void addBook(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
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

        stmt.close();
    }

    private void editBook(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
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

        stmt.close();
    }

    private void removeBook(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        String sql = "DELETE FROM books WHERE book_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, bookId);

        int rows = stmt.executeUpdate();
        response.getWriter().println(rows > 0 ? "Book removed successfully." : "Failed to remove book.");

        stmt.close();
    }

    private void viewAllBooks(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
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
}