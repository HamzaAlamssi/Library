<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Admin Dashboard</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f9f9f9;
      margin: 0;
      padding: 0;
    }
    header {
      background-color: #4CAF50;
      color: white;
      padding: 10px 20px;
      text-align: center;
    }
    nav {
      background-color: #333;
      overflow: hidden;
    }
    nav a {
      float: left;
      display: block;
      color: white;
      text-align: center;
      padding: 14px 16px;
      text-decoration: none;
    }
    nav a:hover {
      background-color: #ddd;
      color: black;
    }
    .container {
      padding: 20px;
    }
    .section {
      margin-bottom: 30px;
      padding: 15px;
      background-color: white;
      border: 1px solid #ddd;
      border-radius: 8px;
      box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
    }
    h2 {
      color: #333;
    }
    p {
      color: #666;
    }
    button {
      padding: 10px 15px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    button:hover {
      background-color: #45a049;
    }
  </style>
</head>
<body>
<header>
  <h1>Admin Dashboard</h1>
  <p>You are logged in as: <b>Admin</b></p>
</header>
<nav>
  <a href="#addBook">Add Book</a>
  <a href="#editBook">Edit Book</a>
  <a href="#removeBook">Remove Book</a>
  <a href="#viewBooks">View All Books</a>
</nav>
<div class="container">
  <!-- Add Book Section -->
  <div id="addBook" class="section">
    <h2>Add a New Book</h2>
    <p>Use this form to add a new book to the library's inventory.</p>
    <form action="bookManagement" method="POST">
      <input type="hidden" name="action" value="add">
      <label for="title">Title:</label><br>
      <input type="text" id="title" name="title" required><br><br>
      <label for="author">Author:</label><br>
      <input type="text" id="author" name="author" required><br><br>
      <label for="genre">Genre:</label><br>
      <input type="text" id="genre" name="genre" required><br><br>
      <label for="isbn">ISBN:</label><br>
      <input type="text" id="isbn" name="isbn" required><br><br>
      <label for="year">Year of Publication:</label><br>
      <input type="number" id="year" name="year" required><br><br>
      <button type="submit">Add Book</button>
    </form>
  </div>

  <!-- Edit Book Section -->
  <div id="editBook" class="section">
    <h2>Edit an Existing Book</h2>
    <p>Update details for an existing book in the library.</p>
    <form action="bookManagement" method="POST">
      <input type="hidden" name="action" value="edit">
      <label for="bookId">Book ID:</label><br>
      <input type="number" id="bookId" name="bookId" required><br><br>
      <label for="title">Title:</label><br>
      <input type="text" id="title" name="title"><br><br>
      <label for="author">Author:</label><br>
      <input type="text" id="author" name="author"><br><br>
      <label for="genre">Genre:</label><br>
      <input type="text" id="genre" name="genre"><br><br>
      <label for="isbn">ISBN:</label><br>
      <input type="text" id="isbn" name="isbn"><br><br>
      <label for="year">Year of Publication:</label><br>
      <input type="number" id="year" name="year"><br><br>
      <label for="availability">Availability:</label><br>
      <select id="availability" name="availability">
        <option value="true">Available</option>
        <option value="false">Not Available</option>
      </select><br><br>
      <button type="submit">Update Book</button>
    </form>
  </div>

  <!-- Remove Book Section -->
  <div id="removeBook" class="section">
    <h2>Remove a Book</h2>
    <p>Delete a book from the library's inventory.</p>
    <form action="bookManagement" method="POST">
      <input type="hidden" name="action" value="remove">
      <label for="bookId">Book ID:</label><br>
      <input type="number" id="id" name="bookId" required><br><br>
      <button type="submit">Remove Book</button>
    </form>
  </div>

  <!-- View All Books Section -->
  <div id="viewBooks" class="section">
    <h2>View All Books</h2>
    <p>Browse the complete list of books available in the library.</p>
    <form action="bookManagement" method="GET">
      <input type="hidden" name="action" value="viewAll">
      <button type="submit">View Books</button>
    </form>
  </div>
</div>
</body>
</html>
