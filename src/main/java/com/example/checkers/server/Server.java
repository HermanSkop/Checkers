package com.example.checkers.server;

import com.example.checkers.client.Client;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server extends Thread implements IPlayable {
    private boolean isRunning = false;
    private int move = 0;
    private String ip;
    private int port;
    private Client.Color hostColor;

    private Client.Color currentPlayer = Client.Color.RED;

    private Board board = new Board();

    public Server(String ip, int port, Client.Color hostColor) throws RemoteException {
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
    public boolean move(Square squareFrom, Square squareTo) throws RemoteException {
        if(board.movePiece(squareFrom.getRow(), squareFrom.getColumn(), squareTo.getRow(), squareTo.getColumn())) {
            pass();
            return true;
        }
        else return false;
    }
    private void pass(){
        currentPlayer = currentPlayer==Client.Color.RED?Client.Color.BLUE:Client.Color.RED;
        move++;
    }
    @Override
    public void run() {
        try {
            isRunning = true;
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
    @Override
    public int getMove() {
        return move;
    }
}
