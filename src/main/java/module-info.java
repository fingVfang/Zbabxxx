module com.example.hexone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.hexone to javafx.fxml;
    exports com.example.hexone;
}