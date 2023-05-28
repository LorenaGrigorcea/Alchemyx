package com.example.alchemyx;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBClass {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/java","root","1234");
    }
    public List<String> getItemTypes() {
        List<String> itemTypes = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT item_type FROM inventory")) {

            while (resultSet.next()) {
                String itemType = resultSet.getString("item_type");
                itemTypes.add(itemType);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemTypes;
    }

}
