<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Borrowing Transactions</title>
</head>
<link rel="stylesheet" href="styles/librarian-pages-styles.css">
<body>
<h1>Return a Book</h1>
<form action="librarian" method="POST">
  <input type="hidden" name="action" value="returnBook">
  <label>Transaction ID: <input type="text" name="transactionId"></label><br>
  <button type="submit">Return Book</button>
</form>
</body>
</html>
