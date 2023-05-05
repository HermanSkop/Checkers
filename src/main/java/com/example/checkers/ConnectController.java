package com.example.checkers;

import com.example.checkers.Client.Client;
import com.example.checkers.Server.Server;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.util.Objects;

public class ConnectController {
    public ToggleGroup color;
    public TextField port;
    public TextField address;
    @FXML
    Button connectButton;
    @FXML
    protected void onConnectButton() throws IOException, AlreadyBoundException {
        RadioButton radioButton = (RadioButton) color.getSelectedToggle();

        Client.Color hostColor = Objects.equals(radioButton.getText(), "White")? Client.Color.RED : Client.Color.BLUE;
        Server server = new Server(address.getText(), Integer.parseInt(port.getText()), hostColor);

        // Create a new stage for the new window
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(ConnectApplication.class.getResource("board.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        stage.setTitle("Checkers");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setUserData(server);
        stage.show();
    }
}