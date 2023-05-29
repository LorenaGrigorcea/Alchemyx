package com.example.alchemyx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Orders extends SwitchPages implements Initializable {

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


    @FXML
    private TableView<OrdersSearch> ordersTableView;
    @FXML
    private TableColumn<OrdersSearch, Integer> idColumn;
    @FXML
    private TableColumn<OrdersSearch, String> itemNameColumn;
    @FXML
    private TableColumn<OrdersSearch, String> itemTypeColumn;
    @FXML
    private TableColumn<OrdersSearch, Integer> quantityColumn;
    @FXML
    private TableColumn<OrdersSearch, String> orderDateColumn;
    @FXML
    private TableColumn<OrdersSearch, String> statusColumn;
    @FXML
    private TextField keywordTextField;
    @FXML
    private TextField itemNameDetails;
    @FXML
    private ComboBox<String> itemTypeDetails;
    @FXML
    private TextField quantityDetails;
    @FXML
    private TextField orderDateDetails;
    @FXML
    private TextField statusDetails;
    @FXML
    private Button addItem;
    @FXML
    private Button updateItem;
    @FXML
    private Button deleteItem;
    @FXML
    private Button clearDetails;
    @FXML
    private Button logOutButton;
    @FXML
    private Label userName;

    public void displayName(String username){
        userName.setText(username);
    }

    ObservableList<OrdersSearch> ordersSearchObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource){
        try{
            Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java","root","1234");
            Statement statement = connectionDB.createStatement();
            String productViewQuery = "SELECT id, item_name, item_type, quantity, order_date, status FROM orders;";
            ResultSet queryOutput = statement.executeQuery(productViewQuery);

            while(queryOutput.next()){
                Integer queryId = queryOutput.getInt("id");
                String queryItemName = queryOutput.getString("item_name");
                String queryItemType = queryOutput.getString("item_type");
                Integer queryQuantity = queryOutput.getInt("quantity");
                String queryOrderDate = queryOutput.getString("order_date");
                String queryStatus = queryOutput.getString("status");

                // Populate the ObservableList
                ordersSearchObservableList.add(new OrdersSearch(queryId, queryItemName, queryItemType,
                        queryQuantity, queryOrderDate, queryStatus));
            }

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
            itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


            ordersTableView.setItems(ordersSearchObservableList);

            FilteredList<OrdersSearch> filteredData = new FilteredList<>(ordersSearchObservableList, b -> true);

            keywordTextField.textProperty().addListener((observable, oldValue, newValue) ->{
                filteredData.setPredicate(ordersSearch -> {
                    if(newValue.isEmpty() || newValue.isBlank() || newValue == null ) {
                        return true;
                    }

                    String searchKeywords  = newValue.toLowerCase();
                    if(ordersSearch.getItemName().toLowerCase().indexOf(searchKeywords) > -1){
                        return  true;
                    }else if(ordersSearch.getStatus().toLowerCase().indexOf(searchKeywords) > -1){
                        return true;
                    }else if(ordersSearch.getItemType().toLowerCase().indexOf(searchKeywords) > -1){
                        return true;
                    }else if(ordersSearch.getQuantity().toString().indexOf(searchKeywords) > -1){
                        return true;
                    }else
                        return false;
                });
            });

            SortedList<OrdersSearch> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(ordersTableView.comparatorProperty());
            ordersTableView.setItems(sortedData);

            ordersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                updateItemView(null);
            });

            // Populare combobox cu tipurile de iteme existente
            List<String> itemTypes = getItemTypes();
            itemTypes.add("Add New Type"); // Adăugarea opțiunii "Add New Type"
            itemTypeDetails.setItems(FXCollections.observableList(itemTypes));

            itemTypeDetails.setOnAction(event -> {
                String selectedType = itemTypeDetails.getSelectionModel().getSelectedItem();
                if (selectedType != null && selectedType.equals("Add New Type")) {
                    String newType = showNewItemDialog();
                    if (newType != null && !newType.isEmpty()) {
                        if (itemTypes.contains(newType)) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText("Item Type Already Exists");
                            alert.setContentText("The specified item type already exists in the item type list.");
                            alert.showAndWait();
                        } else {
                            // Adăugarea noului tip în combobox
                            itemTypeDetails.getItems().add(itemTypes.size() - 1, newType);
                            itemTypeDetails.getSelectionModel().select(newType);
                        }
                    }
                }
            });

            Date();

        }catch (SQLException e){
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    @FXML
    void addNewItem(ActionEvent event){
        OrdersSearch item = getItem();
        if (item != null) {
            try {
                Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java","root","1234");
                PreparedStatement preparedStatement = connectionDB.prepareStatement("INSERT INTO " +
                        "orders (item_name, item_type, quantity, order_date, status) VALUES (?, ?, ?, ?, ?)");
                preparedStatement.setString(1, item.getItemName());
                preparedStatement.setString(2, item.getItemType());
                preparedStatement.setInt(3, item.getQuantity());
                preparedStatement.setString(4, item.getDate());
                preparedStatement.setString(5, item.getStatus());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                connectionDB.close();

                refreshTable();
            } catch (SQLException e) {
                Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
                e.printStackTrace();
                showErrorAlert("An error occurred while adding the item. Please try again.");
            }
        }
    }

    public OrdersSearch getItem (){
        OrdersSearch item = null;
        try{
            String itemName = itemNameDetails.getText();
            String itemType = itemTypeDetails.getValue();
            int quantity = Integer.parseInt(quantityDetails.getText());
            String orderDate = orderDateDetails.getText();
            String status = statusDetails.getText();
            item = new OrdersSearch(itemName, itemType, quantity, orderDate, status);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return item;
    }

    public List<String> getItemTypes() {
        List<String> itemTypes = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT item_type FROM orders")) {

            while (resultSet.next()) {
                String itemType = resultSet.getString("item_type");
                itemTypes.add(itemType);
            }
        } catch (SQLException e) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

        return itemTypes;
    }

    private void Date(){
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateNow = dataFormat.format(new Date());
        orderDateDetails.setText(dateNow);
    }

    @FXML
    void updateItemView(MouseEvent event){
        OrdersSearch selected = ordersTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String itemName = selected.getItemName();
            if (itemName != null) {
                itemNameDetails.setText(itemName);
            } else {
                itemNameDetails.setText("");
            }
            quantityDetails.setText(String.format("%d", selected.getQuantity()));
            statusDetails.setText(selected.getStatus());
            itemTypeDetails.setValue(selected.getItemType());
            orderDateDetails.setText(selected.getDate());
        }
    }

    @FXML
    void updateItemTable() {
        OrdersSearch selected = ordersTableView.getSelectionModel().getSelectedItem();

        if (selected != null) {
            String newItemName = itemNameDetails.getText();
            String newItemType = itemTypeDetails.getValue();
            int newQuantity = Integer.parseInt(quantityDetails.getText());
            String newStatus = statusDetails.getText();


            if (!selected.getItemName().equals(newItemName) || selected.getQuantity() != newQuantity ||
                    !selected.getStatus().equals(newStatus) || !selected.getItemType().equals(newItemType)) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java",
                        "root", "1234");
                     PreparedStatement preparedStatement = connection.prepareStatement("UPDATE orders " +
                             "SET item_name = ?, item_type = ?, quantity = ?, status = ? WHERE id = ?")) {

                    preparedStatement.setString(1, newItemName);
                    preparedStatement.setString(2, newItemType);
                    preparedStatement.setInt(3, newQuantity);
                    preparedStatement.setString(4, newStatus);
                    preparedStatement.setInt(5, selected.getId());

                    preparedStatement.executeUpdate();

                    refreshTable();
                    showSuccessAlert("Item updated successfully.");
                } catch (SQLException e) {
                    Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
                    e.printStackTrace();
                    showErrorAlert("An error occurred while updating the item. Please try again.");
                }
            }
        } else {
            showErrorAlert("No item selected.");
        }
        clearData();
    }

    @FXML
    void deleteItemTable(ActionEvent event){
        OrdersSearch selected = ordersTableView.getSelectionModel().getSelectedItem();

        if (selected != null && selected.getItemName() != null) {
            String itemName = selected.getItemName();
            String itemStatus = selected.getStatus();
            String itemType = selected.getItemType();
            Integer quantity = selected.getQuantity();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Item");
            alert.setHeaderText("Are you sure you want to delete this item?");
            alert.setContentText("Click OK to delete the item.");

            ButtonType okButton = new ButtonType("OK");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(okButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == okButton) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java",
                        "root", "1234");
                     PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM orders " +
                             "WHERE id = ?")) {

                    preparedStatement.setInt(1, selected.getId());
                    preparedStatement.executeUpdate();

                    refreshTable();
                    showSuccessAlert("Item deleted successfully.");
                } catch (SQLException e) {
                    Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
                    e.printStackTrace();
                    showErrorAlert("An error occurred while deleting the item. Please try again.");
                }
                clearData();
            }
        } else {
            System.out.println("Selected item is null.");
            showErrorAlert("No item selected.");
        }
    }

    private void showSuccessAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshTable() {
        ordersSearchObservableList.clear();

        try {
            Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
            Statement statement = connectionDB.createStatement();
            String productViewQuery = "SELECT id, item_name, item_type, quantity, order_date, status FROM orders";
            ResultSet queryOutput = statement.executeQuery(productViewQuery);

            while(queryOutput.next()){
                Integer queryId = queryOutput.getInt("id");
                String queryItemName = queryOutput.getString("item_name");
                String queryItemType = queryOutput.getString("item_type");
                Integer queryQuantity = queryOutput.getInt("quantity");
                String queryOrderDate = queryOutput.getString("order_date");
                String queryStatus = queryOutput.getString("status");

                // Populate the ObservableList
                ordersSearchObservableList.add(new OrdersSearch(queryId, queryItemName, queryItemType,
                        queryQuantity, queryOrderDate, queryStatus));
            }

            queryOutput.close();
            statement.close();
            connectionDB.close();
        } catch (SQLException e) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    @FXML
    void clearDetails(ActionEvent event){
        clearData();
    }

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

    private void clearData() {
        itemNameDetails.clear();
        quantityDetails.clear();
        statusDetails.clear();
        orderDateDetails.clear();
        itemTypeDetails.setValue(null);
    }

    private String showNewItemDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Item Type");
        dialog.setHeaderText("Enter a new item type:");
        dialog.setContentText("Item Type:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

}
