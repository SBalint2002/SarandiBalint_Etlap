module com.etlap.etlap {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.etlap.etlap to javafx.fxml;
    exports com.etlap.etlap;
}