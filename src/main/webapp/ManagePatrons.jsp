<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Patrons</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            color: #333;
            margin: 20px;
        }

        h1 {
            color: #2c3e50;
        }

        h2 {
            color: #16a085;
        }

        form {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #fff;
            max-width: 400px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }

        input, button {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 14px;
        }

        button {
            background-color: #16a085;
            color: white;
            border: none;
            cursor: pointer;
        }

        button:hover {
            background-color: #12876f;
        }
    </style>
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
    <label>User ID:</label>
    <input type="number" name="userId" required>
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
