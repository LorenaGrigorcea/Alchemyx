package com.example.alchemyx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import java.util.Optional;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customers implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public TableView<CustomerData> tblCustomers;
    public TableColumn<CustomerData, String> colName;
    public TableColumn<CustomerData, String> colEmail;
    public TableColumn<CustomerData, String> colAddress;
    public TableColumn<CustomerData, String> colCity;
    public TableColumn<CustomerData, String> colCountry;
    public ObservableList<CustomerData> customerDataList;
    public Connection con;


    public TextField txtFirstName;
    public TextField txtLastName;
    public TextField txtEmail;
    public TextField txtAddress;
    public TextField txtCity;
    public TextField txtCountry;



    public void addCustomer(){
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExist = null;
        ResultSet resultSet = null;
        // Connection connection = null;

        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String address = txtAddress.getText();
        String city = txtCity.getText();
        String country = txtCountry.getText();

        // Email validation regex pattern
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,6}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);


        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error adding customer!");
            alert.setContentText("First Name, Last Name and email are mandatory!");
            alert.showAndWait();
            return;

        }
        if (!matcher.matches()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error adding customer!");
            alert.setContentText("Invalid email format!");
            alert.showAndWait();
            return;
        }
        if (!email.contains("@")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error adding customer!");
            alert.setContentText("Email incorrect!");
            alert.showAndWait();
            return;
        }
        try {
            //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "1234");
            psCheckUserExist = con.prepareStatement("SELECT * FROM Customers WHERE email = ?");
            psCheckUserExist.setString(1, txtEmail.getText());
            resultSet = psCheckUserExist.executeQuery();

            if (resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Registration Failed");
                alert.setContentText("The customer already exists!");
                alert.showAndWait();
            } else {
                psInsert = con.prepareStatement("INSERT INTO customers (first_name, last_name, email, address, city, country) VALUES (?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, firstName);
                psInsert.setString(2, lastName);
                psInsert.setString(3, email);
                psInsert.setString(4, address);
                psInsert.setString(5, city);
                psInsert.setString(6, country);
                psInsert.executeUpdate();

                buildData();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Customer Created");
                alert.setHeaderText("Congratulations!");
                alert.setContentText("Your magical customer has been successfully added!");
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
        }
    }
    public void modifyCustomer() {
        // Obțineți datele clientului selectat din tabel
        CustomerData selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            // Nu a fost selectat niciun client
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No customer selected");
            alert.setContentText("Please select a customer to modify.");
            alert.showAndWait();
            return;
        }

        // Afisați o fereastră de dialog pentru modificarea informațiilor despre client
        Dialog<CustomerData> dialog = new Dialog<>();
        dialog.setTitle("Modify Customer");
        dialog.setHeaderText("Modify Customer Information");

        // Creați etichete și câmpuri de introducere pentru noile informații despre client
        Label lblFirstName = new Label("First Name:");
        TextField txtFirstName = new TextField(selectedCustomer.getName());
        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField(selectedCustomer.getEmail());
        Label lblAddress = new Label("Address:");
        TextField txtAddress = new TextField(selectedCustomer.getAddress());
        Label lblCity = new Label("City:");
        TextField txtCity = new TextField(selectedCustomer.getCity());
        Label lblCountry = new Label("Country:");
        TextField txtCountry = new TextField(selectedCustomer.getCountry());

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(lblFirstName, 0, 0);
        gridPane.add(txtFirstName, 1, 0);
        gridPane.add(lblEmail, 0, 2);
        gridPane.add(txtEmail, 1, 2);
        gridPane.add(lblAddress, 0, 3);
        gridPane.add(txtAddress, 1, 3);
        gridPane.add(lblCity, 0, 4);
        gridPane.add(txtCity, 1, 4);
        gridPane.add(lblCountry, 0, 5);
        gridPane.add(txtCountry, 1, 5);

        dialog.getDialogPane().setContent(gridPane);

        // Adăugați butoane de acțiune (OK și Cancel)
        ButtonType btnTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnTypeOk, btnTypeCancel);

        // Setarea rezultatului fereastrei de dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnTypeOk) {
                String firstName = txtFirstName.getText();
                String lastName = txtLastName.getText();
                String email = txtEmail.getText();
                String address = txtAddress.getText();
                String city = txtCity.getText();
                String country = txtCountry.getText();

                // Actualizați înregistrarea din baza de date cu noile informații
                try {
                    PreparedStatement psUpdate = con.prepareStatement("UPDATE Customers SET first_name = ?, last_name = ?, email = ?, address = ?, city = ?, country = ? WHERE email = ?");
                    psUpdate.setString(1, firstName);
                    psUpdate.setString(2, lastName);
                    psUpdate.setString(3, email);
                    psUpdate.setString(4, address);
                    psUpdate.setString(5, city);
                    psUpdate.setString(6, country);
                    psUpdate.setString(7, selectedCustomer.getEmail());
                    psUpdate.executeUpdate();

                    // Reîncărcați datele în tabel după modificare
                    buildData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
    public void deleteCustomer() {
        // Obțineți datele clientului selectat din tabel
        CustomerData selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            // Nu a fost selectat niciun client
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No customer selected");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
            return;
        }

        // Confirmați ștergerea cu utilizatorul, de exemplu, prin intermediul unei ferestre de dialog de confirmare
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete customer");
        confirmationAlert.setContentText("Are you sure you want to delete the customer?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                PreparedStatement psDelete = con.prepareStatement("DELETE FROM customers WHERE email = ?");
                psDelete.setString(1, selectedCustomer.getEmail());
                int rowsDeleted = psDelete.executeUpdate();

                if (rowsDeleted > 0) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Customer deleted");
                    successAlert.setContentText("The customer has been successfully deleted.");
                    successAlert.showAndWait();

                    // Reîmprospătați tabelul pentru a reflecta modificările
                    buildData();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Deletion failed");
                    errorAlert.setContentText("Failed to delete the customer. Please try again.");
                    errorAlert.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void switchToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Home");
        stage.show();

    }

    public void switchToProducts(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("products.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Products");
        stage.show();
    }

    public void switchToRecipes(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("recipes.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Recipes");
        stage.show();
    }

    public void switchToCustomers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("customers.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Customers");
        stage.show();
    }

    public void switchToSuppliers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("suppliers.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Suppliers");
        stage.show();
    }

    public void switchToOrders(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("orders.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Orders");
        stage.show();
    }

    public void switchToUser(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("user.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - User");
        stage.show();
    }

    public void switchToInventory(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("inventory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Alchemyx - Inventory");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("City"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("Country"));

        //tblCustomers.setItems(customerDataList);
        DBClass objDbClass = new DBClass();
        try {
            con = objDbClass.getConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        buildData();
    }
    public void buildData(){
        customerDataList=FXCollections.observableArrayList();
        try{
            String mySql="Select * from Customers";
            ResultSet resultSet = con.createStatement().executeQuery(mySql);
            while(resultSet.next()) {
                String name = resultSet.getString("first_name");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String country = resultSet.getString("country");

                CustomerData customerData = new CustomerData(name, email, address, city, country);
                customerDataList.add(customerData);
            }
            tblCustomers.setItems(customerDataList);
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }



//    ObservableList<CustomerData> customerDataList = FXCollections.observableArrayList(
//            new CustomerData("Cristina", "c@e.ro", "wizard street 1","Ploiesti OZ", "Magic Romania"),
//            new CustomerData("Radu", "cr@e.ro", "wizard street 2","OZ", "Romania Magica")
//    );

//    public TableColumn<CustomerData, String> getColCountry() {
//        return colCountry;
//    }
//
//    public void setColCountry(TableColumn<CustomerData, String> colCountry) {
//        this.colCountry = colCountry;
//    }
//
//    public TableView<CustomerData> getTblCustomers() {
//        return tblCustomers;
//    }
//
//    public void setTblCustomers(TableView<CustomerData> tblCustomers) {
//        this.tblCustomers = tblCustomers;
//    }
//
//    public TableColumn<CustomerData, String> getColName() {
//        return colName;
//    }
//
//    public void setColName(TableColumn<CustomerData, String> colName) {
//        this.colName = colName;
//    }
//
//    public TableColumn<CustomerData, String> getColEmail() {
//        return colEmail;
//    }
//
//    public void setColEmail(TableColumn<CustomerData, String> colEmail) {
//        this.colEmail = colEmail;
//    }
//
//    public TableColumn<CustomerData, String> getColAddress() {
//        return colAddress;
//    }
//
//    public void setColAddress(TableColumn<CustomerData, String> colAddress) {
//        this.colAddress = colAddress;
//    }
//
//    public TableColumn<CustomerData, String> getColCity() {
//        return colCity;
//    }
//
//    public void setColCity(TableColumn<CustomerData, String> colCity) {
//        this.colCity = colCity;
//    }

}
