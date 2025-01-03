<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Patrons</title>
    <link rel="stylesheet" href="styles/librarian-styles.css">
</head>
<body>
<h1>Manage Patrons</h1>

<!-- Add Patron -->
<h2>Add Patron</h2>
<form action="patronManagement?action=add" method="POST">
    <label>Username:</label>
    <input type="text" name="username" required>
    <label>Password:</label>
    <input type="password" name="password" required>
    <label>Date of Birth:</label>
    <input type="date" name="dob">
    <button type="submit">Add Patron</button>
</form>

<!-- Edit Patron -->
<h2>Edit Patron</h2>
<form action="patronManagement?action=edit" method="POST">
    <label>User ID:</label>
    <input type="number" name="userId" required>
    <label>Username:</label>
    <input type="text" name="username">
    <label>Password:</label>
    <input type="text" name="password">
    <label>Date of Birth:</label>
    <input type="date" name="dob">
    <button type="submit">Edit Patron</button>
</form>

<!-- Remove Patron -->
<h2>Remove Patron</h2>
<form action="patronManagement?action=remove" method="POST">
    <label>Username:</label>
    <input type="text" name="username" required>
    <button type="submit">Remove Patron</button>
</form>

<!-- View Patron Borrowing History -->
<h2>View Patron Borrowing History</h2>
<form action="patronManagement?action=viewHistory" method="POST">
    <label>Username:</label>
    <input type="text" name="username" required>
    <button type="submit">View History</button>
</form>
</body>
</html>
