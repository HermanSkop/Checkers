package com.example.checkers;

import com.example.checkers.Controller;
import com.example.checkers.client.Client;
import com.example.checkers.server.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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

public class UIBoardController{
    @FXML
    public GridPane gridPane;
    public IPlayable server;
    public Client.Color user;
    public Stage stage;

    @FXML
    public void initialize() throws IOException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry();
        server = (IPlayable) reg.lookup("IPlayable");
        Board board = server.getBoard();

        drawField(board);
    }

    @FXML
    public void onShown(WindowEvent windowEvent) throws UnknownHostException {
        stage = (Stage) windowEvent.getSource();
        user = Controller.getColor(stage);
        System.out.println("IP address: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("Color: " + user);
    }
    private void drawField(Board board){
        gridPane.getChildren().clear();
        for (Square square:board.getField()) {
            square.setStyle(square.getColor()== Client.Color.WHITE ?"-fx-background-color: white":"-fx-background-color: black;");
            if(square.getChecker()!=null) setChecker(square);
            else square.setOnMouseClicked(event -> {
                dropSelections();
            });
            gridPane.add(square, square.getColumn(), square.getRow());
        }
    }
    private void setChecker(Square square){
        Checker checker = new Checker(square.getRow(), square.getColumn());
        checker.setRadius(30);
        checker.setFill(square.getChecker()==Client.Color.RED?Color.RED:Color.BLUE);

        checker.setOnMouseClicked(event -> {
            dropSelections();
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
    private void move(Board board, StackPane pane, Client.Color currPlayer){
       // TODO perform move
    }
}

