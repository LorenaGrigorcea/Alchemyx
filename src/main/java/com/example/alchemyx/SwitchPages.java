package com.example.alchemyx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitchPages {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Home");
        stage.show();
    }

    public void switchToInventory(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("inventory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - inventory");
        stage.show();
    }

    public void switchToCustomers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("customers.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - customers");
        stage.show();
    }

    public void switchToSuppliers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("suppliers.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - suppliers");
        stage.show();
    }

    public void switchToOrders(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("orders.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - orders");
        stage.show();
    }

    public void switchToUser(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("user.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - user");
        stage.show();
    }

    public void switchToRecipes(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("recipes.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - recipes");
        stage.show();
    }
}
