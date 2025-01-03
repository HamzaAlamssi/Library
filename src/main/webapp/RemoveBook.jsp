<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Remove Book</title>
</head>
<link rel="stylesheet" href="styles/librarian-pages-styles.css">
<body>
<h1>Remove a Book</h1>
<form action="librarian" method="POST">
    <input type="hidden" name="action" value="removeBook">
    <label>Book ID: <input type="text" name="bookId"></label><br>
    <button type="submit">Edit Book</button>
</form>
</body>
</html>
