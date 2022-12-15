package com.etlap.etlap;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EtlapController {

    private EtlapDB db;
    @FXML
    private Button newFoodButton;
    @FXML
    private Button deleteFoodButton;
    @FXML
    private TableView<Etlap> etlapTable;
    @FXML
    private TableColumn<Etlap, String> nevCol;
    @FXML
    private TableColumn<Etlap, String> kategoriaCol;
    @FXML
    private TableColumn<Etlap, Integer> arCol;
    @FXML
    private Spinner<Integer> szazalekInput;
    @FXML
    private Spinner<Integer> fixInput;
    @FXML
    private ListView<String> description;

    //-----------------------------INIT----------------------------
    @FXML
    private void initialize() {
        nevCol.setCellValueFactory(new PropertyValueFactory<>("nev"));
        kategoriaCol.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        arCol.setCellValueFactory(new PropertyValueFactory<>("ar"));
        szazalekInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200));
        fixInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000));
        try {
            db = new EtlapDB();
            readEtlap();
        } catch (SQLException e) {
            Platform.runLater(() -> {
                sqlAlert(e);
                Platform.exit();
            });
        }
    }

    //-------------------- ALERTS ---------------------------
    //SQL Alert
    private void sqlAlert(SQLException e) {
        alert(Alert.AlertType.ERROR,
                "Hiba történt az adatbázis kapcsolat kialakításakor",
                e.getMessage());
    }

    //Alert
    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    //--------------------SELECT----------------------------

    //Kiválasztott étel
    private Etlap getSelectedKaja() {
        int selectedIndex = etlapTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert(Alert.AlertType.WARNING,
                    "Előbb válasszon ki egy ételt a táblázatból", "");
            return null;
        }
        return etlapTable.getSelectionModel().getSelectedItem();
    }

    //--------------LIST ADD DELETE UPDATE-----------------------

    //Ételek kilistázása
    private void readEtlap() throws SQLException {
        List<Etlap> dogs = db.readEtlap();
        etlapTable.getItems().clear();
        etlapTable.getItems().addAll(dogs);
    }

    @FXML
    public void newFoodClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("new-etlap-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 300, 300);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Étel létrehozása");
        stage.setScene(scene);
        EtlapFormController controller = fxmlLoader.getController();
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                stage.close();
                try {
                    readEtlap();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    @FXML
    public void deleteFoodClick(ActionEvent actionEvent) {
    }


    //-----------------------DESCRIPTION---------------------------
    @FXML
    public void showDescription(Event event) {
        description.getItems().clear();
        description.getItems().add(getSelectedKaja().getLeiras());
    }

    //-----------------------SORT-------------------------------
    @FXML
    public void sortList(Event event) {
        etlapTable.getOnSort();
    }
}