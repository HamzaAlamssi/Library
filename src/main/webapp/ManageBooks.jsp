<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Books</title>
</head>
<link rel="stylesheet" href="styles/librarian-styles.css">
<body>
<h1>Manage Books</h1>
<form action="librarian" method="POST">
    <input type="hidden" name="action" value="addBook">
    <label>Title: <input type="text" name="title"></label><br>
    <label>Author: <input type="text" name="author"></label><br>
    <label>Genre: <input type="text" name="genre"></label><br>
    <label>ISBN: <input type="text" name="isbn"></label><br>
    <label>Year: <input type="number" name="year"></label><br>
    <button type="submit">Add Book</button>
</form>
</body>
</html>
