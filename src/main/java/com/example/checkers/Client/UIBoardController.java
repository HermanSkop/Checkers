package com.example.checkers.Client;

import com.example.checkers.Server.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UIBoardController {
    @FXML
    public GridPane gridPane;
    public IPlayable server;

    @FXML
    public void initialize() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry();
        server = (IPlayable) reg.lookup("IPlayable");
        Board board = server.getBoard();
        drawField(board);
    }

    private void drawField(Board board){
        gridPane.getChildren().clear();
        for (Square square:board.getField()) {
            square.setStyle(square.getColor()== Client.Color.WHITE ?"-fx-background-color: white":"-fx-background-color: black;");
            if(square.getChecker()!=null) {
                Checker checker = new Checker(square.getRow(), square.getColumn());
                checker.setRadius(30);
                checker.setFill(square.getChecker()==Client.Color.RED?Color.RED:Color.BLUE);

                checker.setOnMouseClicked(event -> {
                    dropSelections();
                    try {
                        showPossibleMoves(board, square, server.getCurrentPlayer());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });

                square.getChildren().add(checker);
                StackPane.setAlignment(checker, Pos.CENTER);
            }
            square.setOnMouseClicked(event -> {
                dropSelections();

                    });
            gridPane.add(square, square.getColumn(), square.getRow());
        }
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
    private void showPossibleMoves(Board board, StackPane pane, Client.Color currPlayer){
        Square selectedSquare = (Square) pane;
        Checker selectedChecker = (Checker) pane.getChildren().get(0);
        if(currPlayer!=selectedSquare.getChecker()) return;
        selectedChecker.setFill(Color.ORANGE);
        for(Square square:board.getField()){
            if(board.possibleMove(board, selectedChecker.getRow(), selectedChecker.getCol(), square.getRow(), square.getColumn(), board.getSquare(selectedChecker.getRow(), selectedChecker.getCol()).getChecker())){
                square.setStyle("-fx-background-color: #be9570");
            }
            else if(board.possibleTake(board, selectedChecker.getRow(), selectedChecker.getCol(), square.getRow(), square.getColumn(), board.getSquare(selectedChecker.getRow(), selectedChecker.getCol()).getChecker())){
                square.setStyle("-fx-background-color: #be9570");
            }
        }
    }
    private void move(Board board, StackPane pane, Client.Color currPlayer){
       // TODO perform move
    }
}

