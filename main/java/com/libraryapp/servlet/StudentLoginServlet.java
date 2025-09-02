package com.libraryapp.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class StudentLoginServlet extends HttpServlet {

    private final String JDBC_URL = "jdbc:mysql://localhost:3306/librarydb?useSSL=false&serverTimezone=UTC";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "@Zxcvb1204"; // Replace with your actual MySQL password

    /**
     * Handles both login and registration based on 'action' parameter from the form.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve parameters from request
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String action = request.getParameter("action"); // either "login" or "register"

        if (username == null || password == null || action == null ||
            username.trim().isEmpty() || password.trim().isEmpty() ||
            (!action.equals("login") && !action.equals("register"))
        ) {
            request.setAttribute("error", "Please enter a username, password, and select an action.");
            request.getRequestDispatcher("studentLogin.jsp").forward(request, response);
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("error", "Internal error: JDBC driver not found.");
            request.getRequestDispatcher("studentLogin.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            if ("login".equals(action)) {
                if (checkUserCredentials(conn, username, password)) {
                    // Successful login
                    HttpSession session = request.getSession(true);
                    session.setAttribute("student", username);
                    response.sendRedirect("bookStatus");
                } else {
                    request.setAttribute("error", "Invalid username or password!");
                    request.getRequestDispatcher("studentLogin.jsp").forward(request, response);
                }
            } else if ("register".equals(action)) {
                if (checkUserExists(conn, username)) {
                    request.setAttribute("error", "Username already exists. Please choose another username or login.");
                    request.getRequestDispatcher("studentLogin.jsp").forward(request, response);
                } else {
                    if (registerNewUser(conn, username, password)) {
                        request.setAttribute("message", "Registration successful! Please log in.");
                        request.getRequestDispatcher("studentLogin.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", "Registration failed. Please try again.");
                        request.getRequestDispatcher("studentLogin.jsp").forward(request, response);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("studentLogin.jsp").forward(request, response);
        }
    }

    private boolean checkUserCredentials(Connection conn, String username, String password) throws SQLException {
        String sql = "SELECT * FROM students WHERE username = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean checkUserExists(Connection conn, String username) throws SQLException {
        String sql = "SELECT * FROM students WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean registerNewUser(Connection conn, String username, String password) throws SQLException {
        String sql = "INSERT INTO students (username, password) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}
