package com.example.checkers.server;

import com.example.checkers.client.Properties;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server extends Thread implements IPlayable {
    private boolean isRunning = false;
    private Properties.Color winner;
    private int move = 0;
    private final String ip;
    private final int port;
    private final Properties.Color hostColor;
    private Square lastTake;

    private Properties.Color currentPlayer = Properties.Color.RED;

    private final Board board = new Board();

    public Server(String ip, int port, Properties.Color hostColor) throws RemoteException {
        this.ip = ip;
        this.port = port;
        this.hostColor = hostColor;
    }
    public Board getBoard() throws RemoteException {
        return board;
    }

    @Override
    public Properties.Color getCurrentPlayer() throws RemoteException {
        return currentPlayer;
    }

    @Override
    public Properties.Color getHostColor() throws RemoteException {
        return hostColor;
    }

    @Override
    public boolean performMove(Square squareFrom, Square squareTo) throws RemoteException {
        if(board.takePiece(squareFrom.getRow(), squareFrom.getColumn(), squareTo.getRow(), squareTo.getColumn(), lastTake)) {
            lastTake = squareTo;
            if(!board.canTake(squareTo.getRow(), squareTo.getColumn(), lastTake)) pass();
            move++;
            return true;
        }
        else if (!board.hasTake(squareFrom.getChecker(), lastTake) && board.movePiece(squareFrom.getRow(), squareFrom.getColumn(), squareTo.getRow(), squareTo.getColumn())) {
            if(squareTo.getColumn()==7||squareTo.getColumn()==0) makeKing(squareTo.getRow(), squareTo.getColumn());
            pass();
            move++;
            return true;
        }
        else return false;
    }

    @Override
    public Properties.Color getWinner() throws RemoteException {
        return winner;
    }

    private void pass() {
        Properties.Color enemy = currentPlayer== Properties.Color.RED? Properties.Color.BLUE: Properties.Color.RED;
        if (board.getPiecesNumber(currentPlayer)<=0)
            finishGame(enemy);
        else if (board.getPiecesNumber(enemy)<=0)
            finishGame(currentPlayer);
        setLastTake(null);
        currentPlayer = enemy;
    }
    @Override
    public void run() {
        try {
            isRunning = true;
            IPlayable server = (IPlayable) UnicastRemoteObject.exportObject(this, 0);
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.bind("IPlayable", server);

            while (isRunning) {}

            UnicastRemoteObject.unexportObject(this, true);

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

    @Override
    public void finishGame(Properties.Color winner){
        this.winner = winner;
    }
    @Override
    public void makeKing(int row, int col){
        board.getSquare(row, col).setType(Properties.Type.KING);
    }
    @Override
    public Square getLastTake() {
        return lastTake;
    }
    @Override
    public void setLastTake(Square lastTake) {
        this.lastTake = lastTake;
    }}
