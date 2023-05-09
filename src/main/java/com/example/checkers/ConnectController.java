package com.example.checkers;

import com.example.checkers.client.Client;
import com.example.checkers.client.UIBoardController;
import com.example.checkers.server.IPlayable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectController extends Controller{
    public TextField address;
    public TextField port;
    public Button connectButton;

    public void onConnectButton(ActionEvent actionEvent) {
        Client.Color host;
        try {
            Registry reg = LocateRegistry.getRegistry();
            IPlayable server = (IPlayable) reg.lookup("IPlayable");
            host = server.getHostColor();


            Stage currentStage = (Stage) connectButton.getScene().getWindow();
            currentStage.close();

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(CreateApplication.class.getResource("pages/board.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 650, 650);

            stage.setTitle("Your color: " + (host==Client.Color.RED?Client.Color.BLUE:Client.Color.RED));
            stage.setUserData(host==Client.Color.RED?Client.Color.BLUE.toString():Client.Color.RED.toString());
            stage.setResizable(false);
            stage.setScene(scene);

            stage.setOnShown(windowEvent -> {
                try {
                    UIBoardController controller = fxmlLoader.getController();
                    controller.onShown(windowEvent);
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            });
            stage.setOnCloseRequest(event -> {
                Platform.exit();
            });

            stage.show();
        } catch (NotBoundException | IOException e) {
            showError("This game doesnt exist!");
        }
    }
}
