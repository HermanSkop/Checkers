package com.example.checkers;

import com.example.checkers.client.Client;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public abstract class Controller {
    public void showError(String errorMessage, String title) {
        Stage errorStage = new Stage();
        errorStage.setTitle(title);

        Label messageLabel = new Label(errorMessage);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> errorStage.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(messageLabel, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene errorScene = new Scene(layout, 300, 100);
        errorStage.setScene(errorScene);
        errorStage.show();
    }
    public static Client.Color getColor(Stage stage){
        return Objects.equals(stage.getUserData(), "RED")? Client.Color.RED: Client.Color.BLUE;
    }
}
