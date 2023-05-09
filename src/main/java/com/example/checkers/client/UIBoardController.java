package com.example.checkers.client;

import com.example.checkers.Controller;
import com.example.checkers.server.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UIBoardController extends Thread{
    @FXML
    public GridPane gridPane;
    public IPlayable server;
    public Client.Color user;
    public Stage stage;
    private Square selected;
    private boolean isRunning = false;

    @FXML
    public void initialize() throws IOException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry();
        server = (IPlayable) reg.lookup("IPlayable");
        startUpdatingField();
    }

    private void startUpdatingField() {
        Task<Void> updateField = new Task<>() {
            @Override
            protected Void call() throws Exception {
                isRunning = true;
                int move = -1;
                while (isRunning){
                    Thread.sleep(100);
                    if(move!=server.getMove()) {
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
        System.out.println("IP address: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("Color: " + user);
    }
    private void drawField(Board board) {
        selected = null;
        gridPane.getChildren().clear();
        for (Square square : board.getField()) {
            square.setStyle(square.getColor() == Client.Color.WHITE ? "-fx-background-color: white" : "-fx-background-color: black;");
            if (square.getChecker() != null) setChecker(square);
            else
                square.setOnMouseClicked(event -> {
                    try {
                        performMove(selected, square);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            gridPane.add(square, square.getColumn(), square.getRow());
        }
    }
    private void performMove(Square from, Square to) throws RemoteException {
        if(!server.move(from, to)){drawField(server.getBoard());}
    }

    private void setChecker(Square square){
        Checker checker = new Checker(square.getRow(), square.getColumn());
        checker.setRadius(30);
        checker.setFill(square.getChecker()==Client.Color.RED?Color.RED:Color.BLUE);

        checker.setOnMouseClicked(event -> {
            try {
                drawField(server.getBoard());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                showPossibleMoves(square);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        square.getChildren().add(checker);
        StackPane.setAlignment(checker, Pos.CENTER);
    }

    private void dropSelections(){
        selected = null;
        for (Node node: gridPane.getChildren()){
            Square square = (Square) node;
            square.setStyle(square.getColor()== Client.Color.WHITE ?"-fx-background-color: white":"-fx-background-color: black;");
            if(square.getChecker()!=null){
                Checker checker = (Checker) square.getChildren().get(0);
                checker.setFill(square.getChecker()== Client.Color.RED?
                        Color.RED:Color.BLUE);
            }
        }
    }

    private void showPossibleMoves(Square square) throws RemoteException {
        Board board = server.getBoard();

        if(server.getCurrentPlayer()!=user) return;
        Checker selectedChecker = (Checker) square.getChildren().get(0);
        if(server.getCurrentPlayer()!=square.getChecker()) return;

        selected = square;
        selectedChecker.setFill(Color.ORANGE);
        for(Node moveNode:gridPane.getChildren()){
            Square moveSquare = (Square) moveNode;
            if(board.possibleMove(selectedChecker.getRow(), selectedChecker.getCol(), moveSquare.getRow(), moveSquare.getColumn(), board.getSquare(selectedChecker.getRow(), selectedChecker.getCol()).getChecker())){
                moveSquare.setStyle("-fx-background-color: #be9570");
            }
            else if(board.possibleTake(selectedChecker.getRow(), selectedChecker.getCol(), moveSquare.getRow(), moveSquare.getColumn(), board.getSquare(selectedChecker.getRow(), selectedChecker.getCol()).getChecker())){
                moveSquare.setStyle("-fx-background-color: #be9570");
            }
        }
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                drawField(server.getBoard());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void interrupt() {
        isRunning = false;
    }

}

