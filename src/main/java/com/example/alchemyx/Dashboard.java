package com.example.alchemyx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class Dashboard {


    @FXML
    private Label userName;
    public void displayName(String username){
        userName.setText(username);
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToInventory (ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("inventory.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Inventory");
        stage.show();
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

    public void switchToProducts(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("products.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Products");
        stage.show();


    }

    @FXML
    private Label total_seals;
    @FXML
    private Label revenue;
    @FXML
    private Label cost;
    @FXML
    private Label profit;


    @FXML
    private Label quantity_in_hand;
    @FXML
    private Label will_be_received;


    @FXML
    private Label low_stock;
    @FXML
    private Label  item_group;
    @FXML
    private Label no_of_items;

    @FXML
    private Label total_customers;
    @FXML
    private Label total_suppliers;

    @FXML
    private Label cancel_orders;
    @FXML
    private Label returns;
    @FXML
    private Label no_of_purchases;
    @FXML
    private Label costPurchase;

    @FXML
    private void initialize() throws SQLException {

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() throws SQLException {
        try (Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
             Statement statement = connectionDB.createStatement()) {

            // Retrieve data from the sales_overview table
            String salesQuery = "SELECT * FROM sales_overview";
            ResultSet salesResult = statement.executeQuery(salesQuery);

            if (salesResult.next()) {
                int totalSales = salesResult.getInt("total_sales");
                double revenueValue = salesResult.getDouble("revenue");
                double costValue = salesResult.getDouble("cost");
                double profitValue = salesResult.getDouble("profit");

                // Set the label values
                total_seals.setText(String.valueOf(totalSales));
                revenue.setText(String.valueOf(revenueValue));
                cost.setText(String.valueOf(costValue));
                profit.setText(String.valueOf(profitValue));
            }

            // Close the sales result set
            salesResult.close();

            // Retrieve data from the inventory_summary table
            String inventoryQuery = "SELECT * FROM inventory_summary";
            ResultSet inventoryResult = statement.executeQuery(inventoryQuery);

            if (inventoryResult.next()) {
                int quantityInHand = inventoryResult.getInt("quantity_in_hand");
                int willBeReceived = inventoryResult.getInt("will_be_received");

                // Set the label values
                quantity_in_hand.setText(String.valueOf(quantityInHand));
                will_be_received.setText(String.valueOf(willBeReceived));
            }

            // Close the inventory result set
            inventoryResult.close();

            // Retrieve data from the products_details table
            String productsQuery = "SELECT * FROM products_details";
            ResultSet productsResult = statement.executeQuery(productsQuery);

            if (productsResult.next()) {
                int lowStock = productsResult.getInt("low_stock");
                int itemGroup = productsResult.getInt("item_group");
                int noOfItems = productsResult.getInt("no_of_items");

                // Set the label values
                low_stock.setText(String.valueOf(lowStock));
                item_group.setText(String.valueOf(itemGroup));
                no_of_items.setText(String.valueOf(noOfItems));
            }

            // Close the products result set
            productsResult.close();

            // Retrieve data from the purchase_overview table
            String purchaseQuery = "SELECT * FROM purchase_overview";
            ResultSet purchaseResult = statement.executeQuery(purchaseQuery);

            if (purchaseResult.next()) {
                int cancelOrders = purchaseResult.getInt("cancel_orders");
                int returnsValue = purchaseResult.getInt("returns");
                int noOfPurchases = purchaseResult.getInt("no_of_purchases");
                double costPurchaseValue = purchaseResult.getDouble("cost");

                // Set the label values
                cancel_orders.setText(String.valueOf(cancelOrders));
                returns.setText(String.valueOf(returnsValue));
                no_of_purchases.setText(String.valueOf(noOfPurchases));
                costPurchase.setText(String.valueOf(costPurchaseValue));
            }


            // Close the purchase result set
            purchaseResult.close();

            String customersCountQuery = "SELECT COUNT(*) AS total_customers FROM customers";
            ResultSet customersCountResult = statement.executeQuery(customersCountQuery);

            if (customersCountResult.next()) {
                int totalCustomers = customersCountResult.getInt("total_customers");

                // Set the label value
                total_customers.setText(String.valueOf(totalCustomers));
            }

            // Close the customers count result set
            customersCountResult.close();

            // Retrieve total number of suppliers
            String suppliersCountQuery = "SELECT COUNT(*) AS total_suppliers FROM suppliers";
            ResultSet suppliersCountResult = statement.executeQuery(suppliersCountQuery);

            if (suppliersCountResult.next()) {
                int totalSuppliers = suppliersCountResult.getInt("total_suppliers");

                // Set the label value
                total_suppliers.setText(String.valueOf(totalSuppliers));
            }

            // Close the suppliers count result set
            suppliersCountResult.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential errors here
        }
    }


    public void switchToRecipes(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("recipes.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Recipes");
        stage.show();
    }
    public void switchToCustomers(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("customers.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Customers");
        stage.show();
    }
    public void switchToSuppliers(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("suppliers.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Suppliers");
        stage.show();
    }
    public void switchToOrders(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("orders.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Orders");
        stage.show();
    }
    public void switchToUser(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("user.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - User");
        stage.show();
    }
}
