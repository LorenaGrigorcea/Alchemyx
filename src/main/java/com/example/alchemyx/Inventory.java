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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Inventory extends Dashboard implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label userName;
    public void displayName(String username){
        userName.setText(username);
    }

    public void switchToHome (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Home");
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

    @FXML
    private TextField searchKeywords;
    @FXML
    private TableView<ProductSearch> inventoryTable ;
    @FXML
    private TableColumn<ProductSearch, Integer> iDItem;
    @FXML
    private TableColumn<ProductSearch, String> itemName;
    @FXML
    private TableColumn<ProductSearch, String> itemType;
    @FXML
    private TableColumn<ProductSearch, String> itemDescription;
    @FXML
    private TableColumn<ProductSearch, Integer> itemQuantity;

    ObservableList<ProductSearch> productSearchesModule = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        try {
            Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
            Statement statement = connectionDB.createStatement();
            String productViewQuery = "SELECT id, item_name, item_type, quantity, description FROM inventory";
            ResultSet queryOutput = statement.executeQuery(productViewQuery);

            while (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("id");
                String queryItemName = queryOutput.getString("item_name");
                String queryItemType = queryOutput.getString("item_type");
                Integer queryQuantity = queryOutput.getInt("quantity");
                String queryItemDescription = queryOutput.getString("description");

                productSearchesModule.add(new ProductSearch(queryID, queryItemName, queryItemType, queryQuantity, queryItemDescription));
            }

            iDItem.setCellValueFactory(new PropertyValueFactory<>("IDItem"));
            itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
            itemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
            itemQuantity.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));
            itemDescription.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));

            inventoryTable.setItems(productSearchesModule);

            FilteredList<ProductSearch> filteredData = new FilteredList<>(productSearchesModule, b -> true);

            searchKeywords.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(productSearch -> {
                    if (newValue.isBlank() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeywords = newValue.toLowerCase();
                    if (productSearch.getItemName().toLowerCase().indexOf(searchKeywords) > -1) {
                        return true;
                    } else if (productSearch.getItemDescription().toLowerCase().indexOf(searchKeywords) > -1) {
                        return true;
                    } else if (productSearch.getItemType().toLowerCase().indexOf(searchKeywords) > -1) {
                        return true;
                    } else if (productSearch.getItemQuantity().toString().indexOf(searchKeywords) > -1) {
                        return true;
                    } else
                        return false;
                });
            });

            SortedList<ProductSearch> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(inventoryTable.comparatorProperty());
            inventoryTable.setItems(sortedData);

            inventoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                updateItemView(null);
            });
            descriptionDetails.setWrapText(true);

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
        } catch (SQLException e) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    @FXML
    private TextField itemNameDetails;
    @FXML
    private ComboBox<String> itemTypeDetails;
    @FXML
    private TextField quantityDetails;
    @FXML
    private TextArea descriptionDetails;
    @FXML
    private Button addItem;
    @FXML
    private Button updateItem;
    @FXML
    private Button deleteItem;
    @FXML
    private Button clearDetails;

    @FXML
    void addNewItem(ActionEvent event){
        ProductSearch item = getItem();
        if (item != null) {
            try {
                Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java","root","1234");
                PreparedStatement preparedStatement = connectionDB.prepareStatement("INSERT INTO inventory (item_name, item_type, quantity, description) VALUES (?, ?, ?, ?)");
                preparedStatement.setString(1, item.getItemName());
                preparedStatement.setString(2, item.getItemType());
                preparedStatement.setInt(3, item.getItemQuantity());
                preparedStatement.setString(4, item.getItemDescription());
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
    private void refreshTable() {
        productSearchesModule.clear();

        try {
            Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
            Statement statement = connectionDB.createStatement();
            String productViewQuery = "SELECT id, item_name, item_type, quantity, description FROM inventory";
            ResultSet queryOutput = statement.executeQuery(productViewQuery);

            while (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("id");
                String queryItemName = queryOutput.getString("item_name");
                String queryItemType = queryOutput.getString("item_type");
                Integer queryQuantity = queryOutput.getInt("quantity");
                String queryItemDescription = queryOutput.getString("description");

                productSearchesModule.add(new ProductSearch(queryID, queryItemName, queryItemType, queryQuantity, queryItemDescription));
            }

            queryOutput.close();
            statement.close();
            connectionDB.close();
        } catch (SQLException e) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
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
    @FXML
    void updateItemView(MouseEvent event){
        ProductSearch selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String itemName = selected.getItemName();
            if (itemName != null) {
                itemNameDetails.setText(itemName);
            } else {
                itemNameDetails.setText("");
            }
            quantityDetails.setText(String.format("%d", selected.getItemQuantity()));
            descriptionDetails.setText(selected.getItemDescription());
            itemTypeDetails.setValue(selected.getItemType());
        }
    }
    @FXML
    void updateItemTable() {
        ProductSearch selected = inventoryTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            String newItemName = itemNameDetails.getText();
            int newQuantity = Integer.parseInt(quantityDetails.getText());
            String newDescription = descriptionDetails.getText();
            String newItemType = itemTypeDetails.getValue();

            if (!selected.getItemName().equals(newItemName) || selected.getItemQuantity() != newQuantity || !selected.getItemDescription().equals(newDescription) || !selected.getItemType().equals(newItemType)) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
                     PreparedStatement preparedStatement = connection.prepareStatement("UPDATE inventory SET item_name = ?, item_type = ?, quantity = ?, description = ? WHERE id = ?")) {

                    preparedStatement.setString(1, newItemName);
                    preparedStatement.setString(2, newItemType);
                    preparedStatement.setInt(3, newQuantity);
                    preparedStatement.setString(4, newDescription);
                    preparedStatement.setInt(5, selected.getIDItem());

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
        ProductSearch selected = inventoryTable.getSelectionModel().getSelectedItem();

        if (selected != null && selected.getItemName() != null) {
            String itemName = selected.getItemName();
            String itemDescription = selected.getItemDescription();
            String itemType = selected.getItemType();
            Integer quantity = selected.getItemQuantity();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Item");
            alert.setHeaderText("Are you sure you want to delete this item?");
            alert.setContentText("Click OK to delete the item.");

            ButtonType okButton = new ButtonType("OK");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(okButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == okButton) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
                     PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM inventory WHERE id = ?")) {

                    preparedStatement.setInt(1, selected.getIDItem());
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

    public List<String> getItemTypes() {
        List<String> itemTypes = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DISTINCT item_type FROM inventory")) {

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

    public ProductSearch getItem (){
        ProductSearch item = null;
        try{
            String itemName = itemNameDetails.getText();
            int quantity = Integer.parseInt(quantityDetails.getText());
            String description = descriptionDetails.getText();
            String itemType = itemTypeDetails.getValue();
            item = new ProductSearch(itemName, itemType,quantity, description);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return item;
    }

    @FXML
    void clearDetails(ActionEvent event){
        clearData();
    }
    private void clearData() {
        itemNameDetails.clear();
        quantityDetails.clear();
        descriptionDetails.clear();
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
