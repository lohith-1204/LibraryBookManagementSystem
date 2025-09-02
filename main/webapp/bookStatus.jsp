<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.libraryapp.model.Book" %>

<!DOCTYPE html>
<html>
<head>
    <title>Book Status</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 30px; }
        h2 { color: #333; text-align: center; margin-bottom: 20px; }
        table { width: 80%; margin: 0 auto; border-collapse: collapse; }
        th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #007bff; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
        .logout { margin: 20px auto; width: 80%; text-align: right; }
        .logout a { text-decoration: none; color: #007bff; font-weight: bold; }
        .logout a:hover { text-decoration: underline; }
    </style>
</head>
<body>

<div class="logout">
    <a href="logout">Logout</a>
</div>

<h2>Library Book Status</h2>

<table>
    <thead>
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Status</th>
        </tr>
    </thead>
    <tbody>
        <%
            List<Book> books = (List<Book>) request.getAttribute("books");
            if (books != null && !books.isEmpty()) {
                for (Book book : books) {
        %>
        <tr>
            <td><%= book.getTitle() %></td>
            <td><%= book.getAuthor() %></td>
            <td><%= book.getStatus() %></td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="3" style="text-align:center;">No books found.</td>
        </tr>
        <%
            }
        %>
    </tbody>
</table>

</body>
</html>