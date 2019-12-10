package chatBoxServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {

    // Init values for server starting
    static ChatServer chatServer;
    final static int port = 9000;

    private ServerSocket server;
    private ArrayList<Connection> list;
    private Socket s;
    private Connection c = null;


    public static void main(String args[]) {

        chatServer = new ChatServer(port);
    }

    public ChatServer (int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server has been initialised on port " + port);
        }
        catch (IOException e) {
            System.err.println("error initialising server");
            e.printStackTrace();
        }
        list = new ArrayList<Connection>();
        while(true) {

            try {
                s = server.accept();

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                c = new Connection(s, this, dis, dos);

                Thread t = new Thread(c);
                System.out.println(c + " connected to the server");
                System.out.println("Adding new Client to active client list");
                list.add(c);

                t.start();
            } catch (Exception e) {
                System.out.println(e);
            }
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


    public int getNumberOfUsers() {
        return list.size();
    }


    public Connection getConnectionID() {
        return c;
    }

    public ArrayList<Connection> getConnectionList() {
        return list;
    }

    public void removeConnection(Connection connectionID) {
        list.remove(connectionID);
    }
}
