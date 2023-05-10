package com.example.checkers.client;

import com.example.checkers.Controller;
import com.example.checkers.server.Board;
import com.example.checkers.server.Checker;
import com.example.checkers.server.IPlayable;
import com.example.checkers.server.Square;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UIBoardController extends Controller{
    @FXML
    public GridPane gridPane;
    public IPlayable server;
    public Properties.Color user;
    public Stage stage;
    @FXML
    public Button leaveButton;
    private Square selected;
    private boolean isRunning = false;

    @FXML
    public void initialize() throws IOException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry();
        server = (IPlayable) reg.lookup("IPlayable");
        startUpdatingField();

        leaveButton.setOnAction(event -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            try {
                server.finishGame(getColor(stage)== Properties.Color.RED? Properties.Color.BLUE: Properties.Color.RED);
            } catch (RemoteException e) {
                showError("Game already finished!", "ERROR");
            }
        });

    }
    private void startUpdatingField() {
        Task<Void> updateField = new Task<>() {
            @Override
            protected Void call() throws Exception {
                isRunning = true;
                int move = -1;
                while (isRunning){
                    if(server.getWinner()!=null) {
                        endGame(server.getWinner());
                    }
                    else if(move!=server.getMove()) {
                        Platform.runLater(() -> {
                            try {
                                drawField(server.getBoard());
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        move = server.getMove();
                    }
                }
                return null;
            }
        };

        Thread thread = new Thread(updateField);
        thread.start();
    }
    @FXML
    public void onShown(WindowEvent windowEvent) throws UnknownHostException {
        stage = (Stage) windowEvent.getSource();
        user = Controller.getColor(stage);
    }
    private void drawField(Board board) {
        selected = null;
        gridPane.getChildren().clear();
        for (Square square : board.getField()) {
            square.setStyle(square.getColor() == Properties.Color.WHITE ? "-fx-background-color: white" : "-fx-background-color: black;");
            if (square.getChecker() != null) setChecker(square);
            else
                square.setOnMouseClicked(event -> {
                    try {
                        if(selected != null)performMove(selected, square);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            try {
                gridPane.add(square, square.getColumn(), square.getRow());
            }
            catch (IllegalArgumentException e){
                System.out.println(e);
            }
        }
    }
    private void performMove(Square from, Square to) throws RemoteException {
        if(!server.performMove(from, to)){drawField(server.getBoard());}
    }

    private void endGame(Properties.Color winner) {
        Platform.runLater(() -> {
            try {
                drawField(server.getBoard());
                server.interrupt();
                if(isRunning)showError(winner.toString() + " WINS!", "Game finished");
            } catch (RemoteException ignored) {}
            interrupt();
        });
    }

    private void setChecker(Square square){
        Checker checker = new Checker(square.getRow(), square.getColumn());
        checker.setRadius(30);
        Color color = square.getType()== Properties.Type.MAN?
                (Properties.Color.RED==square.getChecker()? Color.RED:Color.BLUE):
                (square.getChecker()== Properties.Color.RED? Color.DARKRED:Color.DARKBLUE);
        checker.setFill(color);

        checker.setOnMouseClicked(event -> {
            try {
                drawField(server.getBoard());
                showPossibleMoves(square);
            } catch (RemoteException e) {
                showError("Game already finished!", "ERROR");
            }
        });

        square.getChildren().add(checker);
        StackPane.setAlignment(checker, Pos.CENTER);
    }

    private void showPossibleMoves(Square square) throws RemoteException {
        Board board = server.getBoard();

        if(server.getCurrentPlayer()!=user) return;
        Checker selectedChecker = (Checker) square.getChildren().get(0);
        if(server.getCurrentPlayer()!=square.getChecker()) return;

        selected = square;
        for(Node moveNode:gridPane.getChildren()){
            Square moveSquare = (Square) moveNode;
            if(board.possibleTake(selectedChecker.getRow(), selectedChecker.getCol(), moveSquare.getRow(),
                    moveSquare.getColumn(), board.getSquare(selectedChecker.getRow(), selectedChecker.getCol()),
                    server.getLastTake()))
                moveSquare.setStyle("-fx-background-color: #be9570");
        }
        if(!board.hasTake(square.getChecker(), server.getLastTake()))
            for(Node moveNode:gridPane.getChildren()){
                Square moveSquare = (Square) moveNode;
                if(board.possibleMove(selectedChecker.getRow(), selectedChecker.getCol(), moveSquare.getRow(), moveSquare.getColumn(), board.getSquare(selectedChecker.getRow(), selectedChecker.getCol()))){
                    moveSquare.setStyle("-fx-background-color: #be9570");
                }
            }
    }

    public void interrupt() {
        isRunning = false;
    }

}

