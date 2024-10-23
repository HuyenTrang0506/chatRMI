package server;

import client.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

// Provide implementation for the methods in the ChatInterface
public class ChatServerImpl extends UnicastRemoteObject implements ChatInterface {
    private static final long serialVersionUID = 1L;
    private List<ClientInterface> clients;

    // Default constructor to throw RemoteException
    // from its parent constructor
    protected ChatServerImpl() throws RemoteException {
        super();
        clients = new ArrayList<>();
    }

    // Method to send a message to all registered clients
    @Override
    public void sendMessage(Object obj) throws RemoteException {
        // Broadcast message to all clients
        for (ClientInterface client : clients) {
            client.receiveMessage(obj);
        }
    }

    // Method to register a new client
    @Override
    public void registerClient(ClientInterface client) throws RemoteException {
        clients.add(client);
        System.out.println("Client registered: " + client);
    }

    // This method will not be used by the server directly, but will be used by clients
    @Override
    public void receiveMessage(Object obj) throws RemoteException {
        // No implementation needed for the server
    }

    public static void main(String[] args) {
        try {
            // Start RMI registry on port 1099
            java.rmi.registry.LocateRegistry.createRegistry(1099);

            // Create an instance of ChatServer
            ChatServerImpl server = new ChatServerImpl();

            // Bind the server instance to the name "server.ChatServer" in the RMI registry
            java.rmi.Naming.rebind("server.ChatServer", server);

            System.out.println("Chat server started...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}