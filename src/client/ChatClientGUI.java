package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import server.ChatInterface;
import server.ChatMessageObj;

public class ChatClientGUI extends UnicastRemoteObject implements ClientInterface {
    private static final long serialVersionUID = 1L;
    private String username;
    private ChatInterface server;
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    // Constructor to initialize the GUI client with a username and server reference
    protected ChatClientGUI(String username, ChatInterface server) throws RemoteException {
        super();
        this.username = username;
        this.server = server;
        initializeGUI();
    }

    // Method to initialize the GUI components
    private void initializeGUI() {
        frame = new JFrame("Chat Client - " + username);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);
        frame.add(panel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        frame.setVisible(true);
    }

    // Method to send a message to the server
    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            try {
                //Clients send serialized ChatMessage objects
                server.sendMessage(new ChatMessageObj(username, message));
                inputField.setText("");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to receive a message from the server
    @Override
    public void receiveMessage(Object obj) throws RemoteException {
        if (obj instanceof ChatMessageObj) {
            ChatMessageObj message = (ChatMessageObj) obj;
            chatArea.append(message + "\n");
        }
    }

    public static void main(String[] args) {
        try {
            // Connect to the server using the RMI registry
            ChatInterface server = (ChatInterface) java.rmi.Naming.lookup("rmi://localhost:1099/server.ChatServer");

            // Get the username for the client
            String username = JOptionPane.showInputDialog("Enter your username:");
            if (username != null && !username.trim().isEmpty()) {
                // Create client instance and register with the server
                ChatClientGUI client = new ChatClientGUI(username, server);
                server.registerClient(client);
                server.sendMessage(new ChatMessageObj("Server", username + " has joined the chat."));
                System.out.println("Connected to the chat server.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}