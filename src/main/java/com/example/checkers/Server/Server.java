package com.example.checkers.Server;

import com.example.checkers.Client.Client;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server implements IPlayable {
    private String ip;
    private int port;
    private Client.Color hostColor;

    private Client.Color currentPlayer = Client.Color.RED;

    private Board board = new Board();

    public Server(String ip, int port, Client.Color hostColor) throws RemoteException, AlreadyBoundException {
        this.ip = ip;
        this.port = port;
        this.hostColor = hostColor;

        IPlayable server = (IPlayable) UnicastRemoteObject.exportObject(this, 0);

        Registry reg = LocateRegistry.createRegistry(1099);
        reg.bind("IPlayable", server);
    }
    public Board getBoard() throws RemoteException {
        return board;
    }

    @Override
    public Client.Color getCurrentPlayer() throws RemoteException {
        return currentPlayer;
    }
}
