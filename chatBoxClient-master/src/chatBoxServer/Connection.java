package chatBoxServer;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Connection implements Runnable {

    final static int STATE_UNREGISTERED = 0;

    private volatile boolean running;
    private String username;
    private static String usernameList = "USERNAMElist:%CoNcAt%Group Chat";
    private static String connectionList = "CONNECTIONlist:%CoNcAt%chatBoxServer.Connection@1a24k3c0";
    String concatPattern = "%CoNcAt%";
    private int messageCount;
    private int state;
    private Socket client;
    private ChatServer serverReference;
    private BufferedReader readerIn;
    private PrintWriter printOutWriter;
    private DataInputStream dis;
    private DataOutputStream dos;

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

            broadcastConnectionIDToClient();
            getClientUsername();

            do {
                String clientMessage = dis.readUTF();
                decodeMessage(clientMessage);
            } while (running);

        } catch (IOException e) {
            removeConnection();
        }
    }

    public void decodeMessage(String response) {
        String msgHeader, targetClient, selfClient;

        if(response.startsWith("MSGheaderFromCLIENT:")){
            String[] removeHeader = response.split("(MSGheaderFromCLIENT:)");
            removeHeader =  removeHeader[1].split("(MSGbodyFromCLIENT:)");
            msgHeader = removeHeader[0];

            removeHeader = msgHeader.split("(==>)");
            selfClient = removeHeader[0];
            targetClient = removeHeader[1];

            if(targetClient.matches("chatBoxServer.Connection@1a24k3c0")) {
                serverReference.broadcastMessage("grp->"+response);
            } else {
                serverReference.broadcastMessageToTargetClient(selfClient, targetClient, "pm->"+response);
            }
        } else {
            serverReference.broadcastMessage(response);
        }
    }

    public String getNumberOfUsers() {
        String msg = "Currently " + serverReference.getNumberOfUsers() + " users online.";
        return msg;
    }

    public void getClientUsername() {
        ArrayList<String> clientName;
        try {
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            try {
                Object obj = ois.readObject();
                clientName = (ArrayList<String>) obj;
                username = clientName.get(0);

                // Print newly joined client and total clients connecting to the server
                System.out.println("Client joined: " + username);
                System.out.println(getNumberOfUsers());

                usernameList = usernameList + concatPattern + username;
                connectionList = connectionList + concatPattern + this.toString();

                // Add the every connected client username to the username list
                // Use to broadcast back to other clients
                serverReference.broadcastMessage(usernameList);
                serverReference.broadcastMessage(connectionList);
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void removeConnection() {
        Connection connectionID = this;
        String usernameToRemove = concatPattern + username;
        String connectionToRemove = concatPattern + connectionID.toString();

        System.out.println("Client left: " + username);

        usernameList = usernameList.replaceAll("("+usernameToRemove+")", "");
        connectionList = connectionList.replaceAll("("+connectionToRemove+")", "");

        serverReference.removeConnection(connectionID);
        System.out.println(getNumberOfUsers());

        if(serverReference.getNumberOfUsers() == 0){
            System.out.println("Exit the server.");
            System.exit(-1);
        } else {
            serverReference.broadcastMessage(usernameList);
            serverReference.broadcastMessage(connectionList);
        }
    }

    public void broadcastConnectionIDToClient() {
        String header = "CONNECTIONid:";
        header = header + serverReference.getConnectionID().toString();
        printOutWriter.println(header);
    }


    public void sendMessages(String message) {
        printOutWriter.println(message);
    }

}
