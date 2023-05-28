module com.example.alchemyx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    //requires mysql.connector.java;


    opens com.example.alchemyx to javafx.fxml;
    exports com.example.alchemyx;
}