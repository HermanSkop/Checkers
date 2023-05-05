package com.example.checkers.Server;

import com.example.checkers.Client.Client;
import javafx.scene.shape.Circle;

public class Checker extends Circle {
    private int row;
    private int col;


    public Checker(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
