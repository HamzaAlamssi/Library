<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Admin Dashboard</title>
  <link rel="stylesheet" href="styles/admin-styles.css">
</head>
<body>
<header>
  <h1>Admin Dashboard</h1>
  <p>Welcome <%= session.getAttribute("username") %> <p>
  <p>You are logged in as: <b>Admin</b></p>
</header>
<nav>
  <a href="#addBook">Add Book</a>
  <a href="#editBook">Edit Book</a>
  <a href="#removeBook">Remove Book</a>
  <a href="#viewBooks">View All Books</a>
<%--  <a href="#addLibrary">Add Library</a>--%>
<%--  <a href="#addBookToLibrary">Add Book to Library</a>--%>
<%--  <a href="#viewLibraries">View Libraries</a>--%>
  <a href="#borrowReturn">Return Books</a>
  <a href="#report & statistics">Report & Statistics</a>
  <a href="#systemConfiguration" >System Configuration</a>
  <a href="ManagePatrons.jsp" >Manage Patrons</a>

</nav>
<div class="container">
  <!-- Add Book Section -->
  <div id="addBook" class="section">
    <h2>Add a New Book</h2>
    <form action="bookManagement" method="POST">
      <input type="hidden" name="action" value="add">
      <label for="title">Title:</label>
      <input type="text" id="title" name="title" required>
      <label for="author">Author:</label>
      <input type="text" id="author" name="author" required>
      <label for="genre">Genre:</label>
      <input type="text" id="genre" name="genre" required>
      <label for="isbn">ISBN:</label>
      <input type="text" id="isbn" name="isbn" required>
      <label for="year">Year of Publication:</label>
      <input type="number" id="year" name="year" required>
      <button type="submit">Add Book</button>
    </form>
  </div>

  <!-- Edit Book Section -->
  <div id="editBook" class="section">
    <h2>Edit an Existing Book</h2>
    <form action="bookManagement" method="POST">
      <input type="hidden" name="action" value="edit">
      <label >Book ID:</label>
      <input type="number" id="bookId11" name="bookId" required>
      <label for="title">Title:</label>
      <input type="text" id="title1" name="title">
      <label for="author">Author:</label>
      <input type="text" id="author1" name="author">
      <label for="genre">Genre:</label>
      <input type="text" id="genre1" name="genre">
      <label for="isbn">ISBN:</label>
      <input type="text" id="isbn1" name="isbn">
      <label for="year">Year of Publication:</label>
      <input type="number" id="year1" name="year">
      <label for="availability">Availability:</label>
      <select id="availability" name="availability">
        <option value="true">Available</option>
        <option value="false">Not Available</option>
      </select>
      <button type="submit">Update Book</button>
    </form>
  </div>

  <!-- Borrow and Return Books Section -->
  <div id="borrowReturn" class="section">
    <h2>Return Books</h2>

<%--    <h3>Borrow a Book</h3>--%>
<%--    <form action="borrowBook" method="POST">--%>
<%--      <input type="hidden" name="action" value="borrow">--%>
<%--      <label for="bookId">Book ID:</label>--%>
<%--      <input type="number" id="bookId" name="bookId" required>--%>
<%--      <button type="submit">Borrow Book</button>--%>
<%--    </form>--%>

    <h3>Return a Book</h3>
    <form action="borrowReturn" method="POST">
      <input type="hidden" name="action" value="return">
      <label for="transactionId">Transaction ID:</label>
      <input type="text" id="transactionId" name="transactionId" required>
      <button type="submit">Return Book</button>
    </form>
  </div>

  <!-- Remove Book Section -->
  <div id="removeBook" class="section">
    <h2>Remove a Book</h2>
    <form action="bookManagement" method="POST">
      <input type="hidden" name="action" value="remove">
      <label >Book ID:</label>
      <input type="number" id="bookId123" name="bookId" required>
      <button type="submit">Remove Book</button>
    </form>
  </div>

  <!-- View All Books Section -->
  <div id="viewBooks" class="section">
    <h2>View All Books</h2>
    <form action="bookManagement" method="GET">
      <input type="hidden" name="action" value="viewAll">
      <button type="submit">View Books</button>
    </form>
  </div>

  <!-- Add Library Section -->
<%--  <div id="addLibrary" class="section">--%>
<%--    <h2>Add a New Library</h2>--%>
<%--    <form action="libraryManagement" method="POST">--%>
<%--      <input type="hidden" name="action" value="addLibrary">--%>
<%--      <label for="libraryName">Library Name:</label>--%>
<%--      <input type="text" id="libraryName" name="libraryName" required>--%>
<%--      <label for="location">Location:</label>--%>
<%--      <input type="text" id="location" name="location" required>--%>
<%--      <button type="submit">Add Library</button>--%>
<%--    </form>--%>
<%--  </div>--%>

  <!-- Add Book to Library Section -->
<%--  <div id="addBookToLibrary" class="section">--%>
<%--    <h2>Add Book to Library</h2>--%>
<%--    <form action="libraryManagement" method="POST">--%>
<%--      <input type="hidden" name="action" value="addBookToLibrary">--%>
<%--      <label for="libraryId">Library ID:</label>--%>
<%--      <input type="number" id="libraryId" name="libraryId" required>--%>
<%--      <input type="number" id="bookId1" name="bookId" required>--%>
<%--      <button type="submit">Add Book to Library</button>--%>
<%--    </form>--%>
<%--  </div>--%>

  <!-- View Libraries Section -->
<%--  <div id="viewLibraries" class="section">--%>
<%--    <h2>View All Libraries</h2>--%>
<%--    <form action="libraryManagement" method="GET">--%>
<%--      <input type="hidden" name="action" value="viewLibraries">--%>
<%--      <button type="submit">View Libraries</button>--%>
<%--    </form>--%>
<%--  </div>--%>

  <div id="report & statistics" class="section">
    <h2>Reports & Statistics </h2>
    <form action="librarian" method="GET">
      <input type="hidden" name="action" value="viewFineReports">
      <button type="submit">View Fine Reports</button>
    </form>

    <form action="libraryStats" method="GET">
      <button type="submit">View Library Statistics</button>
    </form>
<%--    <button class="bb"><a href="libraryStats">View Library Statistics</a></button>--%>
  </div>


  <div id="systemConfiguration" class="section">
    <h2>System Configuration</h2>
    <form action="updateConfig" method="POST">
      <label for="fineRate">Fine Rate:</label>
      <input type="number" id="fineRate" name="fineRate" value="<%= application.getInitParameter("fineRate") %>" required><br>

      <label for="maxBorrowLimit">Max Borrow Limit:</label>
      <input type="number" id="maxBorrowLimit" name="maxBorrowLimit" value="<%= application.getInitParameter("maxBorrowLimit") %>" required><br>

      <label for="borrowingPeriod">Borrowing Period (days):</label>
      <input type="number" id="borrowingPeriod" name="borrowingPeriod" value="<%= application.getInitParameter("borrowingPeriod") %>" required><br>

      <button type="submit">Update Configuration</button>
    </form>
  </div>
</div>
</body>
</html>
