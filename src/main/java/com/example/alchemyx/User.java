package com.example.alchemyx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class User extends SwitchPages{
    private Stage stage;
    private Scene scene;
    private Parent root;
    @Override
    public void switchToHome(ActionEvent event) throws IOException {
        super.switchToHome(event);
    }

    @Override
    public void switchToInventory(ActionEvent event) throws IOException {
        super.switchToInventory(event);
    }

    @Override
    public void switchToSuppliers(ActionEvent event) throws IOException {
        super.switchToSuppliers(event);
    }

    @Override
    public void switchToCustomers(ActionEvent event) throws IOException {
        super.switchToCustomers(event);
    }
    @Override
    public void switchToOrders(ActionEvent event) throws IOException {
        super.switchToOrders(event);
    }

    @FXML
    private Button logOutButton;
    public void setLogOut (ActionEvent event) throws  IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log out");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Click OK to log out.");

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == okButton) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Alchemyx");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            alert.close();
        }
    }
}
