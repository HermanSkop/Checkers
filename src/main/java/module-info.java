module com.example.checkers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    exports com.example.checkers.Server to java.rmi;
    opens com.example.checkers to javafx.fxml;
    exports com.example.checkers;
    exports com.example.checkers.Client;
    opens com.example.checkers.Client to javafx.fxml;
}