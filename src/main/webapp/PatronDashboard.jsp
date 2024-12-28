<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Patron Dashboard</title>
</head>
<body>
<h1>Welcome to the Patron Dashboard</h1>
<p>You are logged in as: Patron</p>

<form action="borrowBook" method="POST">
    <label for="bookId">Enter Book ID:</label>
    <input type="text" id="bookId" name="bookId" required>
    <button type="submit">Borrow Book</button>
</form>

</body>
</html>
