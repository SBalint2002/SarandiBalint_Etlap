package com.etlap.etlap;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.events.MouseEvent;

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

    private void sqlAlert(SQLException e) {
        alert(Alert.AlertType.ERROR,
                "Hiba történt az adatbázis kapcsolat kialakításakor",
                e.getMessage());
    }

    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    private void readEtlap() throws SQLException {
        List<Etlap> dogs = db.readEtlap();
        etlapTable.getItems().clear();
        etlapTable.getItems().addAll(dogs);
    }

    private Etlap getSelectedKaja() {
        int selectedIndex = etlapTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert(Alert.AlertType.WARNING,
                    "Előbb válasszon ki egy ételt a táblázatból", "");
            return null;
        }
        return etlapTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void showDescription(Event event) {
        description.getItems().clear();
        description.getItems().add(getSelectedKaja().getLeiras());
    }

    @FXML
    public void sortList(Event event) {
        etlapTable.getOnSort();
    }

    @FXML
    public void newFoodClick(ActionEvent actionEvent) {
    }

    @FXML
    public void deleteFoodClick(ActionEvent actionEvent) {
    }
}