package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import server.ChatInterface;
import server.ChatMessageObj;

public class ChatClient extends UnicastRemoteObject implements ClientInterface {
    private static final long serialVersionUID = 1L;
    private String username;

    // Constructor to initialize the client with a username
    protected ChatClient(String username) throws RemoteException {
        super();
        this.username = username;
    }

    // Method to receive a message from the server
    @Override
    public void receiveMessage(Object obj) throws RemoteException {
        if (obj instanceof ChatMessageObj) {
            ChatMessageObj message = (ChatMessageObj) obj;
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        try {
            // Connect to the server using the RMI registry
            ChatInterface server = (ChatInterface) java.rmi.Naming.lookup("rmi://localhost:1099/server.ChatServer");

            // Get the username for the client
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            // Create client instance and register with the server
            ChatClient client = new ChatClient(username);
            server.registerClient(client);
            server.sendMessage(new ChatMessageObj("Server", username + " has joined the chat."));
            System.out.println("Connected to the chat server.");

            // Send messages to the server
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    server.sendMessage(new ChatMessageObj("Server", username + " has left the chat."));
                    break;
                }
                server.sendMessage(new ChatMessageObj(username, message));
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}