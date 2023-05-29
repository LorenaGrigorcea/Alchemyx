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
public class SuplierSearchController implements Initializable {
    @FXML
    private TableView<SuppliersSearchModel> suplierTableView;
    @FXML
    private TableColumn<SuppliersSearchModel,Integer> IdTableColumn;
    @FXML
    private TableColumn<SuppliersSearchModel,String> NumeTableColumn;
    @FXML
    private TableColumn<SuppliersSearchModel,String> CategoryTableColumn;
    @FXML
    private TableColumn<SuppliersSearchModel,String> ContactPersonTableColumn;
    @FXML
    private TableColumn<SuppliersSearchModel,String> CityTableColumn;
    @FXML
    private TableColumn<SuppliersSearchModel,String> CountryTableColumn;

    ObservableList<SuppliersSearchModel> suppliersSearchModelObservationList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resource){
        //  DatabaseConnection connectNow = new DatabaseConnection();
    }

}
