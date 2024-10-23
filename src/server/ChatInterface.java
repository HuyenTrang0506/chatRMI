package server;

import client.ClientInterface;

import java.rmi.RemoteException;

public interface ChatInterface extends ClientInterface {
    void receiveMessage(Object obj) throws RemoteException;
    void sendMessage(Object obj) throws RemoteException;
    void registerClient(ClientInterface client) throws RemoteException;
}