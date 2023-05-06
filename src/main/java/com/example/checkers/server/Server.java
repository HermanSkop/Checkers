package com.example.checkers.server;

import com.example.checkers.client.Client;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server extends Thread implements IPlayable {
    private boolean isRunning = true;

    private String ip;
    private int port;
    private Client.Color hostColor;

    private Client.Color currentPlayer = Client.Color.RED;

    private Board board = new Board();

    public Server(String ip, int port, Client.Color hostColor) throws RemoteException, AlreadyBoundException {
        this.ip = ip;
        this.port = port;
        this.hostColor = hostColor;
    }
    public Board getBoard() throws RemoteException {
        return board;
    }

    @Override
    public Client.Color getCurrentPlayer() throws RemoteException {
        return currentPlayer;
    }

    @Override
    public Client.Color getHostColor() throws RemoteException {
        return hostColor;
    }

    @Override
    public void run() {
        try {
            IPlayable server = (IPlayable) UnicastRemoteObject.exportObject(this, 0);
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.bind("IPlayable", server);

            while (isRunning) {}

            UnicastRemoteObject.unexportObject(this, true); // stop the server

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void interrupt() {
        isRunning = false;
    }
}
