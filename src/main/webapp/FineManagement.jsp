<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Fine Management</title>
</head>
<link rel="stylesheet" href="styles/librarian-styles.css">
<body>
<h1>Manage Fines</h1>
<form action="librarian" method="POST">
  <input type="hidden" name="action" value="updateFineStatus">
  <label>Fine ID: <input type="text" name="fineId"></label><br>
  <button type="submit">Mark as Paid</button>
</form>
</body>
</html>
