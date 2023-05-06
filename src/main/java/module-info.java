module com.example.checkers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    exports com.example.checkers.server to java.rmi;
    exports com.example.checkers.client;
    opens com.example.checkers.client to javafx.fxml;
    exports com.example.checkers;
    opens com.example.checkers to javafx.fxml;
}