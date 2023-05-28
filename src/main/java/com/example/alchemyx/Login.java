package com.example.alchemyx;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.EventObject;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class Login {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchToFirstPage (ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private Button login;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    public void loginButtonOnAction(ActionEvent event) throws Exception {
        String username = usernameTextField.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        root = loader.load();

        Dashboard sceneDashboard = loader.getController();
        sceneDashboard.displayName(username);


        if (!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
            validateLogin(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect login information");
            alert.setContentText("Please complete your username and password.");
            alert.showAndWait();
        }
    }
    public void validateLogin(ActionEvent event) throws SQLException {
        Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java","root","1234");
        try{
            Statement statement = connectionDB.createStatement();
            String verifyLogin = "SELECT count(1) FROM users WHERE Username = '" + usernameTextField.getText() + "' AND Password ='" + passwordTextField.getText() + "'";
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Congratulations!");
                    alert.setHeaderText("You have successfully logged in.");
                    alert.setContentText("You have unlocked the spell 'Login'. Use it wisely!");
                    alert.showAndWait();

                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Spell Error");
                    alert.setHeaderText("Incorrect login information");
                    alert.setContentText("The spell did not work. Please check your username and password and try again.");
                    alert.showAndWait();
                }
            }
            connectionDB.close();
            statement.close();

        }catch (Exception ex){
            ex.printStackTrace();
            ex.getCause();
        }
    }
}
