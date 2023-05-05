package com.example.checkers.Server;

import com.example.checkers.Client.Client;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.Serializable;

public class Square extends StackPane implements Serializable {
    int row;
    int column;

    Client.Color checker;
    Client.Color color;

    public Square(int column, int row, Client.Color color, Client.Color checker) {
        this.row = row;
        this.column = column;
        this.checker = checker;
        this.color = color;
    }

    public Square(int column, int row, Client.Color color) {
        this.row = row;
        this.column = column;
        this.checker = null;
        this.color = color;
    }

    @Override
    public String toString() {
        if(column==8) {
            if (color == Client.Color.RED) return "#\n";
            else return " \n";
        }
        else {
            if (color == Client.Color.RED) return "#";
            else return " ";
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Client.Color getChecker() {
        return checker;
    }

    public Client.Color getColor() {
        return color;
    }
}
