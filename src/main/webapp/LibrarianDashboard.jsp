<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Librarian Dashboard</title>
</head>
<link rel="stylesheet" href="styles/librarian-pages-styles.css">
<body>
<h1>Librarian Dashboard</h1>
<h1>Welcome <%= session.getAttribute("username") %> </h1>
<ul>
  <li><a href="ManageBooks.jsp">Add Book</a></li>
  <li><a href="EditBook.jsp">Edit Book</a></li>
  <li><a href="RemoveBook.jsp">Remove Book</a></li>
  <li><a href="BorrowingTransactions.jsp">Return Borrowed Book</a></li>
  <li><a href="FineManagement.jsp">Set Fine Paid</a></li>
  <li><a href="Reports.jsp">View Fine Reports</a></li>
  <h2>Patron Management</h2>
  <li><a href="ManagePatrons.jsp" >Manage Patrons</a></li>
  <h2>View Library Usage Statistics</h2>
  <li><a href="libraryStats">View Statistics</a></li>
  <h2>View All Books</h2>
  <form action="bookManagement" method="GET">
    <input type="hidden" name="action" value="viewAll">
    <button type="submit">View Books</button>
  </form>

</ul>
</body>
</html>