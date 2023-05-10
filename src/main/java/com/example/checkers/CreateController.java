package com.example.checkers;

import com.example.checkers.client.Client;
import com.example.checkers.client.UIBoardController;
import com.example.checkers.server.Server;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.util.Objects;

public class CreateController extends Controller{
    public ToggleGroup color;
    public TextField port;
    public TextField address;
    @FXML
    Button createButton;
    @FXML
    public void onCreateButton() throws IOException {
        RadioButton radioButton = (RadioButton) color.getSelectedToggle();

        Client.Color hostColor = Objects.equals(radioButton.getText(), "Red")? Client.Color.RED : Client.Color.BLUE;
        Server server = new Server(address.getText(), Integer.parseInt(port.getText()), hostColor);
        server.start();

        Stage currentStage = (Stage) createButton.getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(CreateApplication.class.getResource("pages/board.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);

        stage.setTitle("Your color: " + hostColor + " | IP: " + address.getText() + " | port:" + port.getText());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setUserData(hostColor.toString());

        stage.setOnShown(windowEvent -> {
            try {
                UIBoardController controller = fxmlLoader.getController();
                controller.onShown(windowEvent);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        });
        stage.setOnCloseRequest(event -> {
            server.interrupt();
            Platform.exit();
        });
        stage.show();
    }
    @FXML
    public void initialize() throws UnknownHostException {
        address.setText(InetAddress.getLocalHost().getHostAddress());
    }
}