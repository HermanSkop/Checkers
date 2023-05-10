package com.example.checkers.server;

import com.example.checkers.client.Properties;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayable extends Remote {
    Board getBoard() throws RemoteException;
    Properties.Color getCurrentPlayer() throws RemoteException;
    Properties.Color getHostColor() throws RemoteException;
    boolean performMove(Square squareFrom, Square squareTo) throws RemoteException;
    Properties.Color getWinner() throws RemoteException;
    int getMove() throws RemoteException;

    void finishGame(Properties.Color winner) throws RemoteException;
    void interrupt() throws RemoteException;

    void makeKing(int row, int col) throws RemoteException;

    Square getLastTake() throws RemoteException;

    void setLastTake(Square lastTake) throws RemoteException;
}
