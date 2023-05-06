package com.example.checkers.server;

import com.example.checkers.client.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayable extends Remote {
    Board getBoard() throws RemoteException;
    Client.Color getCurrentPlayer() throws RemoteException;
    Client.Color getHostColor() throws RemoteException;
}
