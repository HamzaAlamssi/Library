package util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/updateConfig")
public class UpdateConfigServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();

        // Retrieve new configuration values from the form
        int fineRate = Integer.parseInt(request.getParameter("fineRate"));
        int maxBorrowLimit = Integer.parseInt(request.getParameter("maxBorrowLimit"));
        int borrowingPeriod = Integer.parseInt(request.getParameter("borrowingPeriod"));

        // Set the values dynamically
        context.setAttribute("fineRate", fineRate);
        context.setAttribute("maxBorrowLimit", maxBorrowLimit);
        context.setAttribute("borrowingPeriod", borrowingPeriod);

        // Print updated values to confirm the change
        response.getWriter().println("Configuration updated successfully.");
        response.getWriter().println("Fine Rate: " + ConfigUtil.getFineRate(context));
        response.getWriter().println("Max Borrow Limit: " + ConfigUtil.getMaxBorrowLimit(context));
        response.getWriter().println("Borrowing Period: " + ConfigUtil.getBorrowingPeriod(context));
    }

}
