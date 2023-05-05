package com.example.checkers.Server;

import com.example.checkers.Client.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayable extends Remote {
    Board getBoard() throws RemoteException;
    Client.Color getCurrentPlayer() throws RemoteException;
}
