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

@WebServlet("/patronManagement")
public class PatronManagementServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addPatron(request, response);
                break;
            case "edit":
                editPatron(request, response);
                break;
            case "remove":
                removePatron(request, response);
                break;
            case "viewHistory":
                viewPatronHistory(request, response);
                break;
            default:
                response.getWriter().println("Invalid action.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//
//        if (action.equals("viewHistory")) {
//            viewPatronHistory(request, response);
//        } else {
//            response.getWriter().println("Invalid action.");
//        }
    }

    private void addPatron(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String dob = request.getParameter("dob");

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            String sql = "INSERT INTO users (username, password, role, dob) VALUES (?, ?, 'Patron', ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, dob);

            int rows = stmt.executeUpdate();
            response.getWriter().println(rows > 0 ? "Patron added successfully." : "Failed to add patron.");

            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    private void editPatron(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = Integer.parseInt(request.getParameter("userId")); // Always required
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String dob = request.getParameter("dob");

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Start building the SQL query
            StringBuilder sql = new StringBuilder("UPDATE users SET ");
            boolean hasUpdates = false;

            // Append each field to be updated if it's not null or empty
            if (username != null && !username.trim().isEmpty()) {
                sql.append("username = ?, ");
                hasUpdates = true;
            }
            if (dob != null && !dob.trim().isEmpty()) {
                sql.append("dob = ?, ");
                hasUpdates = true;
            }
            if (password != null && !password.trim().isEmpty()) {
                sql.append("password = ?, ");
                hasUpdates = true;
            }

            // Remove the last comma and space if updates exist
            if (hasUpdates) {
                sql.setLength(sql.length() - 2); // Remove trailing ", "
                sql.append(" WHERE user_id = ? AND role = 'Patron'");
            } else {
                response.getWriter().println("No fields to update.");
                return;
            }

            PreparedStatement stmt = conn.prepareStatement(sql.toString());

            // Set parameters for the PreparedStatement
            int paramIndex = 1;
            if (username != null && !username.trim().isEmpty()) {
                stmt.setString(paramIndex++, username);
            }
            if (dob != null && !dob.trim().isEmpty()) {
                stmt.setString(paramIndex++, dob);
            }
            if (password != null && !password.trim().isEmpty()) {
                stmt.setString(paramIndex++, password);
            }
            stmt.setInt(paramIndex, userId); // Always set user_id

            int rows = stmt.executeUpdate();
            response.getWriter().println(rows > 0 ? "Patron updated successfully." : "Failed to update patron.");

            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }


    private void removePatron(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            String sql = "DELETE FROM users WHERE username = ? AND role = 'Patron'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            int rows = stmt.executeUpdate();
            response.getWriter().println(rows > 0 ? "Patron removed successfully." : "Failed to remove patron.");

            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    private void viewPatronHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT b.title, bt.borrow_date, bt.due_date, bt.return_date, bt.fine " +
                    "FROM borrow_transactions bt " +
                    "JOIN books b ON bt.book_id = b.book_id " +
                    "WHERE bt.username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            response.setContentType("text/html");
            response.getWriter().println("<h2>Borrowing History for Username: " + username + "</h2>");
            response.getWriter().println("<table border='1'><tr><th>Title</th><th>Borrow Date</th><th>Due Date</th><th>Return Date</th><th>Fine</th></tr>");

            while (rs.next()) {
                response.getWriter().println("<tr>");
                response.getWriter().println("<td>" + rs.getString("title") + "</td>");
                response.getWriter().println("<td>" + rs.getDate("borrow_date") + "</td>");
                response.getWriter().println("<td>" + rs.getDate("due_date") + "</td>");
                response.getWriter().println("<td>" + (rs.getDate("return_date") != null ? rs.getDate("return_date") : "Not Returned") + "</td>");
                response.getWriter().println("<td>" + rs.getDouble("fine") + "</td>");
                response.getWriter().println("</tr>");
            }

            response.getWriter().println("</table>");

            rs.close();
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

}
