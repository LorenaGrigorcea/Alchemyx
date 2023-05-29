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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Suppliers extends SwitchPages implements  Initializable {
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
    public void switchToCustomers(ActionEvent event) throws IOException {
        super.switchToCustomers(event);
    }
    @Override
    public void switchToOrders(ActionEvent event) throws IOException {
        super.switchToOrders(event);
    }
    @Override
    public void switchToUser(ActionEvent event) throws IOException {
        super.switchToUser(event);
    }


    @FXML
    private TextField KeywordTextField;
    @FXML
    private TableView<SuppliersSearchModel> suppliersTableView;
    @FXML
    private TableColumn<SuppliersSearchModel, Integer> IdTableColumn;
    @FXML
    private TableColumn<SuppliersSearchModel, String> NameTableColumn;
    @FXML
    private TableColumn<SuppliersSearchModel, String> CategoryTableColumn;
    @FXML
    private TableColumn<SuppliersSearchModel, String> ContactPersonTableColumn;
    @FXML
    private TableColumn<SuppliersSearchModel, String> CityTableColumn;
    @FXML
    private TableColumn<SuppliersSearchModel, String> CountryTableColumn;

    //  ObservableList<SuppliersSearchModel> suppliersSearchModelObservationList = FXCollections.observableArrayList();
    ObservableList<SuppliersSearchModel> suplierSearchesModule = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        try {
            Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
            Statement statement = connectionDB.createStatement();
            //sql wuery - executed in the backend database
            String productViewQuery = "select id, supplier_name, category , contact_person, city, country from suppliers;";
            ResultSet queryOutput = statement.executeQuery(productViewQuery);

            while (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("id");
                String querySupplier = queryOutput.getString("supplier_name");
                String queryCategory = queryOutput.getString("category");
                String queryContactPerson = queryOutput.getString("contact_person");
                String queryCity = queryOutput.getString("city");
                String queryCountry = queryOutput.getString("country");

                //Populate the Observable list

                suplierSearchesModule.add(new SuppliersSearchModel(queryID, querySupplier, queryCategory, queryContactPerson, queryCity,queryCountry));

            }

            //PropertyValueFactory corresponds to the new ProductSearchModel fields
            //The table column is the one you annotate above
            IdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            NameTableColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            CategoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            ContactPersonTableColumn.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
            CityTableColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
            CountryTableColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

            suppliersTableView.setItems(suplierSearchesModule);


            FilteredList<SuppliersSearchModel> filteredData= new FilteredList<>(suplierSearchesModule,b->true);

            KeywordTextField.textProperty().addListener((observable ,oldValue,newValue)->{
                filteredData.setPredicate(productSearchModel->{
                    if(newValue.isEmpty()||newValue.isBlank()||newValue==null){
                        return true;
                    }
                    String searchKeyword = newValue.toLowerCase();
                    if(productSearchModel.getSupplierName().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if(productSearchModel.getCategory().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if(productSearchModel.getContactPerson().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if(productSearchModel.getCity().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else if(productSearchModel.getCountry().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    }else
                        return false;//no matches found
                });
            });
            SortedList<SuppliersSearchModel> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(suppliersTableView.comparatorProperty());

            suppliersTableView.setItems(sortedData);

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

    }



    @FXML
    private Button addbtn;

    @FXML
    private TextField categorytxb;

    @FXML
    private TextField citytxb;

    @FXML
    private Button clearbtn;

    @FXML
    private TextField contactpersontxb;

    @FXML
    private TextField countrytxb;

    @FXML
    private Button deletebtn;

    @FXML
    private TextField idtxb;

    @FXML
    private TextField nametxb;



    @FXML
    private Button updatebtn;




    @FXML
    void updateView(MouseEvent event) {
        SuppliersSearchModel selected = suppliersTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String itemName = selected.getSupplierName();
            if (itemName != null) {
                nametxb.setText(itemName);
            } else {
                nametxb.setText("");
            }
            idtxb.setText(String.format("%d", selected.getId()));
            categorytxb.setText(selected.getCategory());
            ContactPersonTableColumn.setText(selected.getContactPerson());
            CityTableColumn.setText(selected.getCity());
            CountryTableColumn.setText(selected.getCountry());
        }
    }



    @FXML
    void addNewSupplier(ActionEvent event) {
        SuppliersSearchModel item = getItem();
        if (item != null) {
            try {
                Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
                PreparedStatement preparedStatement = connectionDB.prepareStatement("INSERT INTO suppliers (supplier_name, category, contact_person, email, address, city, country) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, item.getSupplierName());
                preparedStatement.setString(2, item.getCategory());
                preparedStatement.setString(3, item.getContactPerson());
                preparedStatement.setString(4, item.getCity());
                preparedStatement.setString(5, item.getCountry());
                preparedStatement.setNull(6, java.sql.Types.VARCHAR); // Set email as NULL
                preparedStatement.setNull(7, java.sql.Types.VARCHAR); // Set address as NULL

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        item.setId(generatedId);
                    }
                    generatedKeys.close();
                    preparedStatement.close();
                    connectionDB.close();

                    suplierSearchesModule.add(item);
                    showSuccessAlert("Item added successfully.");
                    clearData();
                } else {
                    showErrorAlert("Failed to add item. Please try again.");
                }
            } catch (SQLException e) {
                Logger.getLogger(Suppliers.class.getName()).log(Level.SEVERE, null, e);
                e.printStackTrace();
                showErrorAlert("An error occurred while adding the item. Please try again.");
            }
        }
    }

    private void refreshTable() {
        suplierSearchesModule.clear();

        try {
            Connection connectionDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
            Statement statement = connectionDB.createStatement();
            String productViewQuery = "select id, supplier_name, category , contact_person, city, country from suppliers;";
            ResultSet queryOutput = statement.executeQuery(productViewQuery);

            while (queryOutput.next()) {
                Integer queryID = queryOutput.getInt("id");
                String querySupplier = queryOutput.getString("supplier_name");
                String queryCategory = queryOutput.getString("category");
                String queryContactPerson = queryOutput.getString("contact_person");
                String queryCity = queryOutput.getString("city");
                String queryCountry = queryOutput.getString("country");

                suplierSearchesModule.add(new SuppliersSearchModel(queryID, querySupplier, queryCategory, queryContactPerson, queryCity,queryCountry));
            }

            queryOutput.close();
            statement.close();
            connectionDB.close();
        } catch (SQLException e) {
            Logger.getLogger(Suppliers.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }



    private void showSuccessAlert(String messege){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(messege);
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
        SuppliersSearchModel selected = suppliersTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String itemName = selected.getSupplierName();
            if (itemName != null) {
                nametxb.setText(itemName);
            } else {
                nametxb.setText("");
            }
            idtxb.setText(String.format("%d", selected.getId()));
            categorytxb.setText(selected.getCategory());
            contactpersontxb.setText(selected.getContactPerson()); // Corrected line
            citytxb.setText(selected.getCity()); // Corrected line
            countrytxb.setText(selected.getCountry()); // Corrected line


        }
    }
    @FXML
    void updateItemTable() {
        SuppliersSearchModel selected = suppliersTableView.getSelectionModel().getSelectedItem();

        if (selected != null) {
            String newSupplierName = nametxb.getText();
            String newCategory = categorytxb.getText();
            String newContactPerson = contactpersontxb.getText(); // Corrected line
            String newCity = citytxb.getText(); // Corrected line
            String newCountry = countrytxb.getText(); // Corrected line

            if (!selected.getCategory().equals(newCategory) || !selected.getContactPerson().equals(newContactPerson) || !selected.getCity().equals(newCity) || !selected.getCountry().equals(newCountry)) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
                     PreparedStatement preparedStatement = connection.prepareStatement("UPDATE suppliers SET supplier_name = ?, category = ?, contact_person = ?, city = ?, country = ? WHERE id = ?")) {

                    preparedStatement.setString(1, newSupplierName);
                    preparedStatement.setString(2, newCategory);
                    preparedStatement.setString(3, newContactPerson);
                    preparedStatement.setString(4, newCity);
                    preparedStatement.setString(5, newCountry);
                    preparedStatement.setInt(6, selected.getId());

                    preparedStatement.executeUpdate();

                    refreshTable();
                    showSuccessAlert("Item updated successfully.");
                } catch (SQLException e) {
                    Logger.getLogger(Suppliers.class.getName()).log(Level.SEVERE, null, e);
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
        SuppliersSearchModel selected = suppliersTableView.getSelectionModel().getSelectedItem();

        if (selected != null && selected.getSupplierName() != null) {
            String supplierName = selected.getSupplierName();
            String category = selected.getCategory();
            String contactPerson = selected.getContactPerson();
            String city = selected.getCity();
            String country = selected.getCountry();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Item");
            alert.setHeaderText("Confirm deletion");
            alert.setContentText("Are you sure you want to delete the item: " + supplierName + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
                     PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM suppliers WHERE supplier_name = ? AND category = ? AND contact_person = ? AND city = ? AND country = ?")) {

                    preparedStatement.setString(1, supplierName);
                    preparedStatement.setString(2, category);
                    preparedStatement.setString(3, contactPerson);
                    preparedStatement.setString(4, city);
                    preparedStatement.setString(5, country);

                    preparedStatement.executeUpdate();

                    refreshTable();
                    showSuccessAlert("Item deleted successfully.");
                } catch (SQLException e) {
                    Logger.getLogger(Suppliers.class.getName()).log(Level.SEVERE, null, e);
                    e.printStackTrace();
                    showErrorAlert("An error occurred while deleting the item. Please try again.");
                }
            }
        } else {
            showErrorAlert("No item selected.");
        }
        clearData();
    }



    public SuppliersSearchModel getItem (){
        SuppliersSearchModel item = null;
        try{
            Integer Id = Integer.parseInt(idtxb.getText());
            String supplierName = nametxb.getText();
            String category = categorytxb.getText();
            String ContactPerson = contactpersontxb.getText();
            String City = citytxb.getText();
            String Country = countrytxb.getText();
            item = new SuppliersSearchModel(Id,supplierName, category,ContactPerson, City,Country);
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
        idtxb.clear();
        nametxb.clear();
        categorytxb.clear();
        contactpersontxb.clear();
        citytxb.clear();
        countrytxb.clear();
    }

    private String showNewItemDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Item Type");
        dialog.setHeaderText("Enter a new item type:");
        dialog.setContentText("Item Type:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
    @FXML
    public void updateItemView(javafx.scene.input.MouseEvent mouseEvent) {
        SuppliersSearchModel selected = suppliersTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String itemName = selected.getSupplierName();
            if (itemName != null) {
                nametxb.setText(itemName);
            } else {
                nametxb.setText("");
            }
            idtxb.setText(String.format("%d", selected.getId()));
            categorytxb.setText(selected.getCategory());
            contactpersontxb.setText(selected.getContactPerson()); // Corrected line
            citytxb.setText(selected.getCity()); // Corrected line
            countrytxb.setText(selected.getCountry()); // Corrected line
        }
    }
}

