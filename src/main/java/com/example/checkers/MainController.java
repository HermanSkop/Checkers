package com.example.checkers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController extends Controller{
    @FXML
    public Button createButton;
    @FXML
    public Button joinButton;

    @FXML
    public void join(ActionEvent actionEvent) throws IOException {
        replaceScene(joinButton, "pages/connect.fxml");
    }
    @FXML
    public void create(ActionEvent actionEvent) throws IOException {
        replaceScene(createButton, "pages/create.fxml");
    }

    private void replaceScene(Button createButton, String fileName) throws IOException {
        Stage currentStage = (Stage) createButton.getScene().getWindow();
        currentStage.close();

        Stage stage = new Stage();
        stage.setTitle("Create new game");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
        stage.show();
    }
}
