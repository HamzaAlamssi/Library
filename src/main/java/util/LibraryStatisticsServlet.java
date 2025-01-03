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

@WebServlet("/libraryStats")
public class LibraryStatisticsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            // Query for active borrowers
            String activeBorrowersSql = "SELECT COUNT(DISTINCT username) AS activeBorrowers FROM borrow_transactions WHERE return_date IS NULL";
            PreparedStatement activeBorrowersStmt = conn.prepareStatement(activeBorrowersSql);
            ResultSet activeBorrowersRs = activeBorrowersStmt.executeQuery();
            int activeBorrowers = 0;
            if (activeBorrowersRs.next()) {
                activeBorrowers = activeBorrowersRs.getInt("activeBorrowers");
            }

            // Query for current reservations
            String currentReservationsSql = "SELECT COUNT(*) AS currentReservations FROM book_reservations";
            PreparedStatement currentReservationsStmt = conn.prepareStatement(currentReservationsSql);
            ResultSet currentReservationsRs = currentReservationsStmt.executeQuery();
            int currentReservations = 0;
            if (currentReservationsRs.next()) {
                currentReservations = currentReservationsRs.getInt("currentReservations");
            }

            // Output statistics
            response.setContentType("text/html");
            response.getWriter().println("<h1>Library Usage Statistics</h1>");
            response.getWriter().println("<p>Active Borrowers: " + activeBorrowers + "</p>");
            response.getWriter().println("<p>Current Reservations: " + currentReservations + "</p>");

            activeBorrowersRs.close();
            activeBorrowersStmt.close();
            currentReservationsRs.close();
            currentReservationsStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
