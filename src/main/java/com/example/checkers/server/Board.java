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
        for (Square square:field) if(square.getRow()==row && square.getColumn()==col) return square;
        return null;
    }

    public boolean movePiece(int pieceRow, int pieceCol, int moveRow, int moveCol){
        Square squareTo = getSquare(moveRow, moveCol);
        Square squareFrom = getSquare(pieceRow, pieceCol);
        if(possibleMove(pieceRow, pieceCol, moveRow, moveCol, squareFrom.getChecker())){
            int iTo = getIndexOfSquare(moveRow, moveCol);
            int iFrom = getIndexOfSquare(pieceRow, pieceCol);

            squareTo.setChecker(squareFrom.getChecker());
            squareFrom.setChecker(null);

            field.set(iTo, squareTo);
            field.set(iFrom, squareFrom);
            return true;
        }
        return false;
    }
    public boolean takePiece(int pieceRow, int pieceCol, int moveRow, int moveCol){
        Square squareTo = getSquare(moveRow, moveCol);
        Square squareFrom = getSquare(pieceRow, pieceCol);
        int colDirection = getColDirection(squareFrom.getChecker())*2;
        int rowDirection = getRowDirection(pieceRow, moveRow);
        Square takeSquare = getSquare(moveRow-rowDirection/2, moveCol-colDirection/2);
        if(possibleTake(pieceRow, pieceCol, moveRow, moveCol, squareFrom.getChecker())){
            squareTo.setChecker(squareFrom.getChecker());
            takeSquare.setChecker(null);
            squareFrom.setChecker(null);

            int iTo = getIndexOfSquare(moveRow, moveCol);
            int iFrom = getIndexOfSquare(pieceRow, pieceCol);
            int iTake = getIndexOfSquare(moveRow-rowDirection/2, moveCol-colDirection/2);

            field.set(iTo, squareTo);
            field.set(iFrom, squareFrom);
            field.set(iTake, takeSquare);
            return true;
        }
        return false;
    }
    public boolean possibleMove(int pieceRow, int pieceCol, int moveRow, int moveCol, Client.Color checker){
        int colDirection = getColDirection(checker);
        int rowDirection = getRowDirection(pieceRow, moveRow) ;
        if(rowDirection!=1 && rowDirection!=-1) return false;
        if(moveCol-pieceCol!=colDirection || moveRow-pieceRow!=rowDirection) return false;
        else return this.getSquare(moveRow, moveCol).getChecker() == null;
    }
    public boolean possibleTake(int pieceRow, int pieceCol, int moveRow, int moveCol, Client.Color checker){
        int colDirection = getColDirection(checker)*2;
        int rowDirection = getRowDirection(pieceRow, moveRow);
        if(rowDirection!=2 && rowDirection!=-2) return false;
        if(moveCol-pieceCol!=colDirection || moveRow-pieceRow!=rowDirection) return false;
        else if (this.getSquare(moveRow, moveCol).getChecker()!=null) return false;
        else if (this.getSquare(moveRow-rowDirection/2, moveCol-colDirection/2)!=null &&
                (this.getSquare(moveRow-rowDirection/2, moveCol-colDirection/2).getChecker()==null ||
                this.getSquare(moveRow-rowDirection/2, moveCol-colDirection/2).getChecker()==checker)) return false;
        else return true;
    }
    public static int getRowDirection(int moveFrom, int moveTo){
        return moveTo-moveFrom;
    }
    public static int getColDirection(Client.Color checker){
        return checker==Client.Color.RED?1:-1;
    }
    private int getIndexOfSquare(int row, int col){
        for(Square square:field){
            if(square.getRow()==row && square.getColumn()==col) return field.indexOf(square);
        }
        return -1;
    }

    public boolean hasTake(Client.Color color){
        for(Square square:field)
            if (square.getChecker()==color)
                for (int row = 0; row<8; row++)
                    for (int col = 0; col<8; col++)
                        if (possibleTake(square.getRow(), square.getColumn(), row, col, color)) return true;
        return false;
    }

    public int getPiecesNumber(Client.Color color) {
        int n = 0;
        for(Square square:field)
            if (square.getChecker()==color)
                n++;
        return n;
    }
}
