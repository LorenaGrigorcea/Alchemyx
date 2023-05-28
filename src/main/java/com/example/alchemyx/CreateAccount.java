package com.example.alchemyx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class CreateAccount {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToLoginA(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField firstLastName;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button createAccount;

    public void changeScene(ActionEvent event) throws IOException {
        String usernameText = username.getText();
        String firstLastNameText = firstLastName.getText();
        String passwordText = password.getText();

        if (!usernameText.isEmpty() && !firstLastNameText.isEmpty() && !passwordText.isEmpty()) {
            if (!validatePassword(passwordText)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Registration Failed");
                alert.setContentText("Please make sure your password meets the requirements:\n- At least 8 characters\n- At least one uppercase letter\n- At least one special character");
                alert.showAndWait();
            } else {
                RegistrationForm(usernameText, firstLastNameText, passwordText, event);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect signup information");
            alert.setContentText("Please complete your name, username, and password.");
            alert.showAndWait();
        }
    }

    public void RegistrationForm(String username, String firstLastName, String password, ActionEvent event) {
        if (!validatePassword(password)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Registration Failed");
            alert.setContentText("Please make sure your password meets the requirements:\n- At least 8 characters\n- At least one uppercase letter\n- At least one special character");
            alert.showAndWait();
            return;
        }
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExist = null;
        ResultSet resultSet = null;
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
            psCheckUserExist = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExist.setString(1, username);
            resultSet = psCheckUserExist.executeQuery();

            if (resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Registration Failed");
                alert.setContentText("The user already exists!");
                alert.showAndWait();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, name, password) VALUES (?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, firstLastName);
                psInsert.setString(3, password);
                psInsert.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Account Created");
                alert.setHeaderText("Congratulations!");
                alert.setContentText("Your magical account has been successfully created. Enter the realm of AlchemyX and unleash your inner sorcerer!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExist != null) {
                try {
                    psCheckUserExist.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private boolean validatePassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            return false;
        }
        return true;
    }
}
