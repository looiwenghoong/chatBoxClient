package chatBoxServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {

    // Init values for server starting
    static ChatServer chatServer = null;
    final static int port = 9000;

    private ServerSocket server;
    public ArrayList<Connection> list;
    private Socket s;
    private Connection c = null;


    public static void main(String args[]) {
        chatServer = new ChatServer(port);
        chatServer.onServerRunning();
    }

    public ChatServer (int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server has been initialised on port " + port);
        }
        catch (Exception e) {
            System.err.println("error initialising server");
        }
    }

    private void onServerRunning() {
        list = new ArrayList<>();
        while(true) {
            try {
                s = server.accept();
                createConnection(s);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // Function to create connection by passing in incoming socket
    // Add newly created connection to list
    public void createConnection(Socket socket) {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            c = new Connection(s, this, dis, dos);
            addNewConnectionToList(c);

            Thread t = new Thread(c);
            System.out.println(c + " connected to the server");
            System.out.println("Adding new Client to active client list");

            t.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Function to broadcast message to all the clients
    public void broadcastMessage(String theMessage){
        for( Connection clientThread: list){
            clientThread.sendMessages(theMessage);
        }
    }

    //Function to broadcast message to 1 target client
    public void broadcastMessageToTargetClient(String selfClient, String targetClient, String message) {
        Connection clientThread;
        for(int i = 0; i< list.size(); i++) {
            clientThread = list.get(i);
            if(clientThread.toString().matches(selfClient) || clientThread.toString().matches(targetClient)) {
                clientThread.sendMessages(message);
            }
        }
    }

    public void addNewConnectionToList(Connection c) {
        list.add(c);
    }

    public int getNumberOfUsers() {
        return list.size();
    }

    public Connection getConnectionID() {
        return c;
    }

    public void removeConnection(Connection connectionID) {
        list.remove(connectionID);
    }
}
