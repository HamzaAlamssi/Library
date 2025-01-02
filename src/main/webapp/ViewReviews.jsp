<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Book Reviews</title>
    <link rel="stylesheet" href="styles/view-styles.css">
</head>
<body>
<header>
    <h1>View Book Reviews</h1>
</header>

<div class="container">
    <h2>Find Reviews for a Book</h2>
    <form action="viewReviews" method="GET" class="form-style">
        <label for="bookId">Enter Book ID:</label>
        <input type="number" id="bookId" name="bookId" required>
        <button type="submit">View Reviews</button>
    </form>

    <%-- Reviews Table --%>
    <%
        Object reviews = request.getAttribute("reviews");
        if (reviews != null && reviews instanceof java.util.List) {
            java.util.List<java.util.Map<String, Object>> reviewList = (java.util.List<java.util.Map<String, Object>>) reviews;
    %>
    <h2>Reviews</h2>
    <table class="styled-table">
        <thead>
        <tr>
            <th>User</th>
            <th>Comment</th>
            <th>Rating</th>
            <th>Date</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (java.util.Map<String, Object> review : reviewList) {
        %>
        <tr>
            <td><%= review.get("username") %></td>
            <td><%= review.get("comment") != null ? review.get("comment") : "No Comment" %></td>
            <td><%= review.get("rating") %></td>
            <td><%= review.get("review_date") %></td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <% } else { %>
    <p>No reviews found for this book.</p>
    <% } %>
</div>
</body>
</html>
