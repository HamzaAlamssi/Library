package util;

import jakarta.servlet.ServletContext;

public class ConfigUtil {
    public static int getFineRate(ServletContext context) {
        // Check for runtime attribute first
        Object runtimeValue = context.getAttribute("fineRate");
        if (runtimeValue != null) {
            return (int) runtimeValue; // Return the dynamically set value
        }

        // Fallback to web.xml default
        return Integer.parseInt(context.getInitParameter("fineRate"));
    }

    public static int getMaxBorrowLimit(ServletContext context) {
        Object runtimeValue = context.getAttribute("maxBorrowLimit");
        if (runtimeValue != null) {
            return (int) runtimeValue;
        }
        return Integer.parseInt(context.getInitParameter("maxBorrowLimit"));
    }

    public static int getBorrowingPeriod(ServletContext context) {
        Object runtimeValue = context.getAttribute("borrowingPeriod");
        if (runtimeValue != null) {
            return (int) runtimeValue;
        }
        return Integer.parseInt(context.getInitParameter("borrowingPeriod"));
    }
}
