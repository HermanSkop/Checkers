package com.example.checkers.client;

import java.io.Serializable;

public class Properties implements Serializable {
    public enum Color{
        RED,
        BLUE,
        WHITE,
        BLACK
    }
    public enum Type{
        MAN,
        KING
    }

}
