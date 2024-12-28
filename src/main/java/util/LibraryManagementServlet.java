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
import util.DatabaseConnection;

@WebServlet("/libraryManagement")
public class LibraryManagementServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();

            switch (action) {
                case "addLibrary":
                    addLibrary(request, response, conn);
                    break;
                case "addBookToLibrary":
                    addBookToLibrary(request, response, conn);
                    break;
                default:
                    response.getWriter().println("Invalid action.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();

            if ("viewLibraries".equals(action)) {
                viewLibraries(request, response, conn);
            } else if ("viewLibraryBooks".equals(action)) {
                viewLibraryBooks(request, response, conn);
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

    private void addLibrary(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        String libraryName = request.getParameter("libraryName");
        String location = request.getParameter("location");

        String sql = "INSERT INTO libraries (library_name, location) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, libraryName);
        stmt.setString(2, location);

        int rows = stmt.executeUpdate();
        response.getWriter().println(rows > 0 ? "Library added successfully." : "Failed to add library.");

        stmt.close();
    }

    private void addBookToLibrary(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        int libraryId = Integer.parseInt(request.getParameter("libraryId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        String sql = "INSERT INTO library_books (library_id, book_id) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, libraryId);
        stmt.setInt(2, bookId);

        int rows = stmt.executeUpdate();
        response.getWriter().println(rows > 0 ? "Book added to library successfully." : "Failed to add book to library.");

        stmt.close();
    }

    private void viewLibraries(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        String sql = "SELECT * FROM libraries";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        response.setContentType("text/html");
        response.getWriter().println("<h2>All Libraries</h2>");
        response.getWriter().println("<table border='1'>");
        response.getWriter().println("<tr><th>Library ID</th><th>Library Name</th><th>Location</th></tr>");

        while (rs.next()) {
            response.getWriter().println("<tr>");
            response.getWriter().println("<td>" + rs.getInt("library_id") + "</td>");
            response.getWriter().println("<td>" + rs.getString("library_name") + "</td>");
            response.getWriter().println("<td>" + rs.getString("location") + "</td>");
            response.getWriter().println("</tr>");
        }
        response.getWriter().println("</table>");

        rs.close();
        stmt.close();
    }

    private void viewLibraryBooks(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, IOException {
        int libraryId = Integer.parseInt(request.getParameter("libraryId"));

        String sql = "SELECT b.book_id, b.title, b.author FROM books b " +
                "JOIN library_books lb ON b.book_id = lb.book_id " +
                "WHERE lb.library_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, libraryId);
        ResultSet rs = stmt.executeQuery();

        response.setContentType("text/html");
        response.getWriter().println("<h2>Books in Library</h2>");
        response.getWriter().println("<table border='1'>");
        response.getWriter().println("<tr><th>Book ID</th><th>Title</th><th>Author</th></tr>");

        while (rs.next()) {
            response.getWriter().println("<tr>");
            response.getWriter().println("<td>" + rs.getInt("book_id") + "</td>");
            response.getWriter().println("<td>" + rs.getString("title") + "</td>");
            response.getWriter().println("<td>" + rs.getString("author") + "</td>");
            response.getWriter().println("</tr>");
        }
        response.getWriter().println("</table>");

        rs.close();
        stmt.close();
    }
}
