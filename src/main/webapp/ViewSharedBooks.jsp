<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Shared Books</title>
</head>
<body>
<h1>Books Shared with You</h1>
<%
    List<Map<String, Object>> sharedBooks = (List<Map<String, Object>>) request.getAttribute("sharedBooks");
    if (sharedBooks != null && !sharedBooks.isEmpty()) {
%>
<table border="1">
    <tr>
        <th>Title</th>
        <th>Author</th>
        <th>Shared By</th>
        <th>Comment</th>
        <th>Shared Date</th>
    </tr>
    <%
        for (Map<String, Object> book : sharedBooks) {
    %>
    <tr>
        <td><%= book.get("title") %></td>
        <td><%= book.get("author") %></td>
        <td><%= book.get("shared_by") %></td>
        <td><%= book.get("comment") != null ? book.get("comment") : "No Comment" %></td>
        <td><%= book.get("share_date") %></td>
    </tr>
    <%
        }
    %>
</table>
<%
} else {
%>
<p>No books have been shared with you.</p>
<%
    }
%>
</body>
</html>
