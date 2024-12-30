<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Patron Dashboard</title>
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

        form input, form textarea, form select, form button {
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

        .link-button {
            display: inline-block;
            padding: 10px 15px;
            background-color: #228b22;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-bottom: 10px;
        }

        .link-button:hover {
            background-color: #006400;
        }
    </style>
</head>
<body>
<header>
    <h1>Welcome to the Patron Dashboard</h1>
    <p>You are logged in as: Patron</p>
</header>

<div class="container">

    <!-- Search for Books -->
    <h2>Search for Books</h2>
    <form action="searchBooks" method="GET">
        <label for="query">Search Query:</label>
        <input type="text" id="query" name="query" required>
        <label for="type">Search By:</label>
        <select id="type" name="type">
            <option value="title">Title</option>
            <option value="author">Author</option>
        </select>
        <button type="submit">Search</button>
    </form>

    <!-- Borrow a Book -->
    <h2>Borrow a Book</h2>
    <form action="borrowBook" method="POST">
        <label for="bookId">Enter Book ID:</label>
        <input type="text" id="bookId" name="bookId" required>
        <button type="submit">Borrow Book</button>
    </form>

    <!-- View Borrowing History -->
    <h2>Your Borrowing History</h2>
    <a class="link-button" href="borrowingHistory">View Borrowing History</a>

    <!-- View Fine Status -->
    <h2>Your Fine Status</h2>
    <a class="link-button" href="fineStatus">View Fine Status</a>

    <!-- Add a Review -->
    <h2>Add a Review</h2>
    <form action="addReview" method="POST">
        <label for="bookId">Book ID:</label>
        <input type="number" id="book_Id" name="book_Id" required>
        <label for="rating">Rating (1-5):</label>
        <input type="number" id="rating" name="rating" min="1" max="5" required>
        <label for="comment">Comment:</label>
        <textarea id="comment" name="comment"></textarea>
        <button type="submit">Submit Review</button>
    </form>

    <!-- View Book Reviews -->
    <h2>View Book Reviews</h2>
    <form action="viewReviews" method="GET">
        <label for="bookIdReview">Enter Book ID:</label>
        <input type="number" id="bookIdReview" name="bookId" required>
        <button type="submit">View Reviews</button>
    </form>
    <h2>Share a Book</h2>
    <form action="shareBook" method="POST">
        <label for="book-Id">Book ID:</label>
        <input type="number" id="book-Id" name="book-Id" required>
        <br>
        <label for="sharedWith">Share With (Username):</label>
        <input type="text" id="sharedWith" name="sharedWith" required>
        <br>
        <label for="notes">Comment:</label>
        <textarea id="notes" name="notes"></textarea>
        <br>
        <button type="submit">Share Book</button>
    </form>
    <a class="link-button" href="viewSharedBooks">View Shared Books With You</a>

</div>
</body>
</html>
