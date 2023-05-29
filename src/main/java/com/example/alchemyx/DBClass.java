package com.example.alchemyx;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBClass {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/java","root","1234");
    }
}
