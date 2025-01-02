<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Patron Dashboard</title>
</head>
<link rel="stylesheet" href="styles/patron-styles.css">
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
    <a class="link-button" href="ViewReviews.jsp">View Book Reviews</a>
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
