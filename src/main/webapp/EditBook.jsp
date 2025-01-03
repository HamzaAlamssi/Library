<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Book</title>
</head>
<link rel="stylesheet" href="styles/librarian-pages-styles.css">
<body>
<h1>Edit a Book</h1>
<form action="librarian" method="POST">
    <input type="hidden" name="action" value="editBook">
    <label>Book ID: <input type="text" name="bookId"></label><br>
    <label>Title: <input type="text" name="title"></label><br>
    <label>Author: <input type="text" name="author"></label><br>
    <label>Genre: <input type="text" name="genre"></label><br>
    <label>ISBN: <input type="text" name="isbn"></label><br>
    <label>Year: <input type="number" name="year"></label><br>
    <label for="availability">Availability:</label>
    <select id="availability" name="availability">
        <option value="true">Available</option>
        <option value="false">Not Available</option>
    </select>
    <button type="submit">Edit Book</button>
</form>
</body>
</html>
