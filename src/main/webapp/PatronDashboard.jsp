<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Patron Dashboard</title>
    <link rel="stylesheet" href="styles/patron-styles.css">
</head>
<body>
<header>
    <h1>Patron Dashboard</h1>
    <p>Welcome <%= session.getAttribute("username") %> </p>
    <p>You are logged in as: <b>Patron</b></p>
</header>
<nav>
    <a href="#searchBooks">Search for Books</a>
    <a href="#borrowBook">Borrow a Book</a>
    <a href="#reserveBook">Reserve a Book</a>
    <a href="#borrowingHistory">View Borrowing History</a>
    <a href="#fineStatus">View Fine Status</a>
    <a href="#addReview">Add a Review</a>
    <a href="#viewReviews">View Book Reviews</a>
    <a href="#shareBook">Share a Book</a>
    <a href="viewSharedBooks">View Shared Books</a>
</nav>
<div class="container">

    <!-- Search for Books -->
    <h2 id="searchBooks">Search for Books</h2>
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
    <h2 id="borrowBook">Borrow a Book</h2>
    <form action="borrowBook" method="POST">
        <label for="bookId">Enter Book ID:</label>
        <input type="text" id="bookId" name="bookId" required>
        <button type="submit">Borrow Book</button>
    </form>

    <!-- Reserve a Book -->
    <h2 id="reserveBook">Reserve a Book</h2>
    <form action="reserveBook" method="POST">
        <label for="reserveBookId">Enter Book ID:</label>
        <input type="text" id="reserveBookId" name="bookId" required>
        <button type="submit">Reserve Book</button>
    </form>

    <!-- View Borrowing History -->
    <h2 id="borrowingHistory">Your Borrowing History</h2>
    <a class="link-button" href="borrowingHistory">View Borrowing History</a>

    <!-- View Fine Status -->
    <h2 id="fineStatus">Your Fine Status</h2>
    <a class="link-button" href="fineStatus">View Fine Status</a>

    <!-- Add a Review -->
    <h2 id="addReview">Add a Review</h2>
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
    <h2 id="viewReviews">View Book Reviews</h2>
    <a class="link-button" href="ViewReviews.jsp">View Book Reviews</a>

    <!-- Share a Book -->
    <h2 id="shareBook">Share a Book</h2>
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
