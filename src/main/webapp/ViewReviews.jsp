<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Book Reviews</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0fff0; /* Light green background */
            color: #006400; /* Dark green text */
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #228b22; /* Forest green */
            color: white;
            padding: 10px 20px;
            text-align: center;
        }

        .container {
            margin: 20px;
        }

        h1, h2 {
            color: #006400; /* Dark green */
        }

        form {
            background-color: #ffffff; /* White background */
            border: 1px solid #228b22; /* Forest green border */
            border-radius: 5px;
            padding: 15px;
            margin-bottom: 20px;
        }

        form label {
            display: block;
            margin-bottom: 8px;
        }

        form input, form button {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #228b22;
            border-radius: 5px;
            font-size: 14px;
        }

        form button {
            background-color: #228b22; /* Forest green */
            color: white;
            cursor: pointer;
        }

        form button:hover {
            background-color: #006400; /* Dark green */
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table, th, td {
            border: 1px solid #228b22;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #228b22; /* Forest green */
            color: white;
        }
    </style>
</head>
<body>
<header>
    <h1>View Book Reviews</h1>
</header>

<div class="container">

    <!-- Form to Enter Book ID -->
    <h2>Find Reviews for a Book</h2>
    <form action="viewReviews" method="GET">
        <label for="bookId">Enter Book ID:</label>
        <input type="number" id="bookId" name="bookId" required>
        <button type="submit">View Reviews</button>
    </form>

    <!-- Reviews Table -->
    <%
        // Get the reviews from the request if they exist
        Object reviews = request.getAttribute("reviews");
        if (reviews != null && reviews instanceof java.util.List) {
            java.util.List<java.util.Map<String, Object>> reviewList = (java.util.List<java.util.Map<String, Object>>) reviews;
    %>
    <h2>Reviews</h2>
    <table>
        <tr>
            <th>User</th>
            <th>Comment</th>
            <th>Rating</th>
            <th>Date</th>
        </tr>
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
    </table>
    <% } else if (reviews == null) { %>
    <p>No reviews found for this book.</p>
    <% } %>

</div>
</body>
</html>
