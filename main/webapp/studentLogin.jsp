<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Student Login / Register</title>
<style>
    body {font-family: Arial, sans-serif; background-color: #f0f2f5; padding-top: 50px; text-align: center;}
    .login-container {background: white; padding: 20px; border-radius: 5px; display: inline-block; width: 280px;}
    input[type="text"], input[type="password"] {
        padding: 8px; margin: 8px 0; width: 100%; box-sizing: border-box;
    }
    input[type="submit"] {
        padding: 8px 15px; margin-top: 8px;
        background-color: #007bff; border: none; color: white; cursor: pointer; width: 100%;
    }
    input[type="submit"]:hover { background-color: #0056b3; }
    .radio-group { margin: 15px 0; text-align: left; }
    .error {color: red; margin-top: 10px;}
    .message {color: green; margin-top: 10px;}
</style>
</head>
<body>
<div class="login-container">
    <h2>Student Login / Register</h2>
    <form action="login" method="post">
        <input type="text" name="username" placeholder="Username" required autofocus><br>
        <input type="password" name="password" placeholder="Password" required><br>
        <div class="radio-group">
            <label><input type="radio" name="action" value="login" checked> Login</label>
            <label style="margin-left:20px;"><input type="radio" name="action" value="register"> Register</label>
        </div>
        <input type="submit" value="Submit">
    </form>

    <div class="error">
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) { out.print(error); }
        %>
    </div>

    <div class="message">
        <%
            String message = (String) request.getAttribute("message");
            if (message != null) { out.print(message); }
        %>
    </div>
</div>
</body>
</html>