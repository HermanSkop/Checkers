package com.example.checkers.server;

import com.example.checkers.client.Properties;
import javafx.scene.layout.StackPane;

import java.io.Serializable;

public class Square extends StackPane implements Serializable {
    private final int row;
    private final int column;

    private Properties.Color checker;
    private Properties.Type type = Properties.Type.MAN;
    private final Properties.Color color;

    public Square(int column, int row, Properties.Color color, Properties.Color checker) {
        this.row = row;
        this.column = column;
        this.checker = checker;
        this.color = color;
    }

    public Square(int column, int row, Properties.Color color) {
        this.row = row;
        this.column = column;
        this.checker = null;
        this.color = color;
    }

    @Override
    public String toString() {
        if(column==8) {
            if (color == Properties.Color.RED) return "#\n";
            else return " \n";
        }
        else {
            if (color == Properties.Color.RED) return "#";
            else return " ";
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Properties.Color getChecker() {
        return checker;
    }

    public Properties.Color getColor() {
        return color;
    }

    public void setChecker(Properties.Color checker) {
        this.checker = checker;
    }

    public void setType(Properties.Type type) {
        this.type = type;
    }

    public Properties.Type getType() {
        return type;
    }
}
