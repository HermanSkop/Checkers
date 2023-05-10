package com.example.checkers.server;

import com.example.checkers.client.Properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    private final List<Square> field;

    public Board() {
        this.field = new ArrayList<>();
        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                Properties.Color color = (row + col) % 2 == 0 ? Properties.Color.WHITE : Properties.Color.BLACK;
                if (color == Properties.Color.BLACK) {
                    if (row < 3) field.add(new Square(row, col, color, Properties.Color.RED));
                    else if (row > 4) field.add(new Square(row, col, color, Properties.Color.BLUE));
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
        if(possibleMove(pieceRow, pieceCol, moveRow, moveCol, squareFrom)){
            int iTo = getIndexOfSquare(moveRow, moveCol);
            int iFrom = getIndexOfSquare(pieceRow, pieceCol);

            if(squareFrom.getType()== Properties.Type.KING || squareTo.getColumn()==0 || squareTo.getColumn()==7) {
                squareTo.setType(Properties.Type.KING);
                squareFrom.setType(Properties.Type.MAN);
            }

            squareTo.setChecker(squareFrom.getChecker());
            squareFrom.setChecker(null);

            field.set(iTo, squareTo);
            field.set(iFrom, squareFrom);
            return true;
        }
        return false;
    }
    public boolean takePiece(int pieceRow, int pieceCol, int moveRow, int moveCol, Square lastTake){
        Square squareTo = getSquare(moveRow, moveCol);
        Square squareFrom = getSquare(pieceRow, pieceCol);
        int colDirection = getColDirection(squareFrom.getChecker())*2;
        if (getSquare(pieceRow, pieceCol).getType()== Properties.Type.KING) colDirection = moveCol-pieceCol;
        int rowDirection = getRowDirection(pieceRow, moveRow);
        Square takeSquare = getSquare(moveRow-rowDirection/2, moveCol-colDirection/2);
        if(possibleTake(pieceRow, pieceCol, moveRow, moveCol, squareFrom, lastTake)){
            if(squareFrom.getType()== Properties.Type.KING || squareTo.getColumn()==0 || squareTo.getColumn()==7) {
                squareTo.setType(Properties.Type.KING);
                squareFrom.setType(Properties.Type.MAN);
            }
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
    public boolean possibleMove(int pieceRow, int pieceCol, int moveRow, int moveCol, Square square){
        int colDirection = getColDirection(square.getChecker());
        int rowDirection = getRowDirection(pieceRow, moveRow) ;
        if(rowDirection!=1 && rowDirection!=-1) return false;
        if (square.getType()== Properties.Type.KING){
            colDirection = moveCol-pieceCol;
            if ((colDirection!=1 && colDirection!=-1) || moveRow - pieceRow != rowDirection) return false;
        }
        else if(moveCol-pieceCol!=colDirection || moveRow-pieceRow!=rowDirection) return false;
        return this.getSquare(moveRow, moveCol).getChecker() == null;
    }
    public boolean possibleTake(int pieceRow, int pieceCol, int moveRow, int moveCol, Square square, Square lastTake){
        int colDirection = getColDirection(square.getChecker())*2;
        int rowDirection = getRowDirection(pieceRow, moveRow);
        if(lastTake!=null) if (lastTake.getRow()!=pieceRow || lastTake.getColumn()!=pieceCol) return false;
        if(rowDirection!=2 && rowDirection!=-2) return false;
        if(square.getType()== Properties.Type.KING){
            colDirection = moveCol-pieceCol;
            if ((colDirection!=2 && colDirection!=-2) || moveRow - pieceRow != rowDirection) return false;
        }
        else if (moveCol - pieceCol != colDirection || moveRow - pieceRow != rowDirection) return false;
        if (this.getSquare(moveRow - rowDirection / 2, moveCol - colDirection / 2) != null &&
                (this.getSquare(moveRow - rowDirection / 2, moveCol - colDirection / 2).getChecker() == null ||
                        this.getSquare(moveRow - rowDirection / 2, moveCol - colDirection / 2).getChecker() == square.getChecker()))
            return false;
        return this.getSquare(moveRow, moveCol).getChecker() == null;
    }
    public static int getRowDirection(int moveFrom, int moveTo){
        return moveTo-moveFrom;
    }
    public static int getColDirection(Properties.Color checker){
        return checker== Properties.Color.RED?1:-1;
    }
    private int getIndexOfSquare(int row, int col){
        for(Square square:field){
            if(square.getRow()==row && square.getColumn()==col) return field.indexOf(square);
        }
        return -1;
    }

    public boolean hasTake(Properties.Color color, Square lastTake){
        for(Square square:field)
            if (square.getChecker()==color)
                for (int row = 0; row<8; row++)
                    for (int col = 0; col<8; col++)
                        if (possibleTake(square.getRow(), square.getColumn(), row, col, square, lastTake)) return true;
        return false;
    }
    public boolean canTake(int pieceRow, int pieceCol, Square lastTake){
        for (int row = 0; row<8; row++)
            for (int col = 0; col<8; col++)
                if (possibleTake(pieceRow, pieceCol, row, col, getSquare(pieceRow, pieceCol), lastTake))
                    return true;
        return false; // r1 c2
    }

    public int getPiecesNumber(Properties.Color color) {
        int n = 0;
        for(Square square:field)
            if (square.getChecker()==color)
                n++;
        return n;
    }

}
