package com.example.checkers.server;

import com.example.checkers.client.Client;
import javafx.scene.layout.StackPane;

import java.io.Serializable;

public class Square extends StackPane implements Serializable {
    private int row;
    private int column;

    Client.Color checker;
    private final Client.Color color;

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

    public void setChecker(Client.Color checker) {
        this.checker = checker;
    }
}
