package com.libraryapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/librarydb?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root"; // Change if your MySQL user is different
    private static final String JDBC_PASSWORD = "@Zxcvb1204"; // Change to your MySQL password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
}
