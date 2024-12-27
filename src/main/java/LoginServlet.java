import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DatabaseConnection;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Database connection
            Connection conn = DatabaseConnection.getInstance().getConnection();

            // Validate credentials
            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");

                // Set session attribute for username and role
                request.getSession().setAttribute("username", username);
                request.getSession().setAttribute("role", role);

                // Redirect based on role
                if ("Admin".equalsIgnoreCase(role)) {
                    response.sendRedirect("AdminDashboard.jsp");
                } else if ("Librarian".equalsIgnoreCase(role)) {
                    response.sendRedirect("LibrarianDashboard.jsp");
                } else if ("Patron".equalsIgnoreCase(role)) {
                    response.sendRedirect("PatronDashboard.jsp");
                }
            } else {
                response.getWriter().println("Invalid username or password.");
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}

