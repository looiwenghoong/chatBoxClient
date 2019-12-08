package chatBoxServer;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Connection implements Runnable {

    final static int STATE_UNREGISTERED = 0;

    private volatile boolean running;
    private String username;
    private static String usernameList = "USERNAMElist:";
    private int messageCount;
    private int state;
    private Socket client;
    private ChatServer serverReference;
    private BufferedReader readerIn;
    private PrintWriter printOutWriter;
    private DataInputStream dis;
    private DataOutputStream dos;
    private static HashMap<Connection, String> userHashMap = new HashMap<>();


    Connection (Socket client, ChatServer serverReference, DataInputStream dis, DataOutputStream dos) {
        this.serverReference = serverReference;
        this.client = client;
        this.dis = dis;
        this.dos = dos;
        this.state = STATE_UNREGISTERED;
        messageCount = 0;
    }

    public void run(){
        running = true;
        try {
            readerIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            printOutWriter = new PrintWriter(client.getOutputStream(), true);

            getNumberOfUsers();
            broadcastConnectionIDToClient();
            getClientUsername();
            computeUserConnectionHashMap();

            Set set = userHashMap.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();
                System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
                System.out.println(mentry.getValue());
            }

            do {
                String clientMessage = dis.readUTF();
                System.out.println(clientMessage);
                serverReference.broadcastMessage(clientMessage);
            } while (running);

        } catch (IOException e) {
            removeConnection();
        }
    }

    public void getNumberOfUsers() {
        if(serverReference.getNumberOfUsers() == 0) {
            printOutWriter.println("No other users connected");
        } else {
            String msg = "Currently " + serverReference.getNumberOfUsers() + " users online.";
            serverReference.broadcastMessage(msg);
        }
    }

    public void getClientUsername() {
        ArrayList<String> clientName;
        String concatPattern = "%CoNcAt%";
        try {
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            try {
                Object obj = ois.readObject();
                clientName = (ArrayList<String>) obj;
                username = clientName.get(0);
                System.out.println("I got the " + username);

                usernameList = usernameList + concatPattern + username;
                // Add the every connected client username to the username list
                // Use to broadcast back to other clients
                serverReference.broadcastMessage(usernameList);
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void removeConnection() {
        Connection connectionID = this;

        System.out.println(connectionID + " or " + username + " quit");
        userHashMap.remove(connectionID);
        if(userHashMap.size() == 0){
            System.exit(-1);
        } else {
            Set set = userHashMap.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();
                System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
                System.out.println(mentry.getValue());
            }
        }
    }

    public void broadcastConnectionIDToClient() {
        printOutWriter.println(serverReference.getConnectionID().toString());
    }


    public void sendMessages(String message) {
        printOutWriter.println(message);
    }

    public void computeUserConnectionHashMap() {
        userHashMap.put(this, username);
    }
}
