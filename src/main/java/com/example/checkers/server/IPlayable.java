package com.example.checkers.server;

import com.example.checkers.client.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayable extends Remote {
    Board getBoard() throws RemoteException;
    Client.Color getCurrentPlayer() throws RemoteException;
    Client.Color getHostColor() throws RemoteException;
    boolean performMove(Square squareFrom, Square squareTo) throws RemoteException;
    Client.Color getWinner() throws RemoteException;
    int getMove() throws RemoteException;

    void finishGame(Client.Color winner) throws RemoteException;
    void interrupt() throws RemoteException;

    void makeKing(int row, int col) throws RemoteException;

    Square getLastTake() throws RemoteException;

    void setLastTake(Square lastTake) throws RemoteException;
}
