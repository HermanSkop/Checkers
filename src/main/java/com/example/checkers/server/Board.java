package com.example.checkers.server;

import com.example.checkers.client.Client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    private List<Square> field;

    public Board() {
        this.field = new ArrayList<>();
        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                Client.Color color = (row + col) % 2 == 0 ? Client.Color.WHITE : Client.Color.BLACK;
                if (color == Client.Color.BLACK) {
                    if (row < 3) field.add(new Square(row, col, color, Client.Color.RED));
                    else if (row > 4) field.add(new Square(row, col, color, Client.Color.BLUE));
                    else field.add(new Square(row, col, color));
                }
                else field.add(new Square(row, col, color));
            }
        }
    }

    @Override
    public String toString() {
        return field.toString().replace(",", "");
    }

    public List<Square> getField() {
        return field;
    }

    public Square getSquare(int row, int col){
        for (Square square:field) if(square.row==row && square.column==col) return square;
        return null;
    }

    public boolean movePiece(int pieceRow, int pieceCol, int moveRow, int moveCol, Client.Color checker){
        if(possibleMove(pieceRow, pieceCol, moveRow, moveCol, checker));
        return false;
    }
    public boolean possibleMove(int pieceRow, int pieceCol, int moveRow, int moveCol, Client.Color checker){
        int colDirection = checker== Client.Color.RED?1:-1;
        int rowDirection = moveRow-pieceRow;
        if(rowDirection!=1 && rowDirection!=-1) return false;
        if(moveCol-pieceCol!=colDirection || moveRow-pieceRow!=rowDirection) return false;
        else return this.getSquare(moveRow, moveCol).getChecker() == null;
    }
    public boolean possibleTake(int pieceRow, int pieceCol, int moveRow, int moveCol, Client.Color checker){
        int colDirection = checker== Client.Color.RED?2:-2;
        int rowDirection = moveRow-pieceRow; // can be 2 or -2
        if(rowDirection!=2 && rowDirection!=-2) return false;
        if(moveCol-pieceCol!=colDirection || moveRow-pieceRow!=rowDirection) return false;
        else if (this.getSquare(moveRow, moveCol).getChecker()!=null) return false;
        else if (this.getSquare(moveRow-rowDirection/2, moveCol-colDirection/2)!=null &&
                (this.getSquare(moveRow-rowDirection/2, moveCol-colDirection/2).getChecker()==null ||
                this.getSquare(moveRow-rowDirection/2, moveCol-colDirection/2).getChecker()==checker)) return false;
        else return true;
    }
}
