package chatBoxServer;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Connection implements Runnable {

    private volatile boolean running;
    private String username;
    private static String usernameList = "USERNAMElist:%CoNcAt%Group Chat";
    private static String connectionList = "CONNECTIONlist:%CoNcAt%chatBoxServer.Connection@1a24k3c0";
    String concatPattern = "%CoNcAt%";
    private Socket client;
    public ChatServer serverReference;
    private BufferedReader readerIn;
    private PrintWriter printOutWriter;
    private DataInputStream dis;
    private DataOutputStream dos;
    private static ArrayList<String> usernameArrayList = new ArrayList<>();
    private static ArrayList<String> connectionArrayList = new ArrayList<>();

    Connection (Socket client, ChatServer serverReference, DataInputStream dis, DataOutputStream dos) {
        this.serverReference = serverReference;
        this.client = client;
        this.dis = dis;
        this.dos = dos;
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
                usernameArrayList.add(username);
                connectionArrayList.add(this.toString());

                for(int i = 0; i< usernameArrayList.size(); i++) {
                    System.out.println("Name: " + usernameArrayList.get(i) + " connectionid: " + connectionArrayList.get(i));
                }
                System.out.println(usernameList + "\n");
                System.out.println(connectionList + "\n");

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
        String newUsernameList = "USERNAMElist:%CoNcAt%Group Chat";
        String newConnectionList = "CONNECTIONlist:%CoNcAt%chatBoxServer.Connection@1a24k3c0";

        System.out.println("Client left: " + username);

        int indexToRemove = 0;

        // Loop through connection array to find which connection and username to remove
        for (int i = 0; i < connectionArrayList.size(); i++) {
            if(connectionArrayList.get(i).matches(connectionID.toString())){
                connectionArrayList.remove(i);
                indexToRemove = i;
                usernameArrayList.remove(i);
                serverReference.removeConnection(connectionID);
            }
        }

        // Remove the username from the concatenated string
        String[] removeHeader = usernameList.split("USERNAMElist:%CoNcAt%Group Chat");
        removeHeader = removeHeader[1].split(concatPattern);
        System.out.println(removeHeader);
        List<String> list = Arrays.asList(removeHeader);

        for (int i = 1; i < list.size(); i++) {
            if(i != indexToRemove + 1) {
                newUsernameList = newUsernameList + concatPattern + list.get(i);
            }
        }

        usernameList = newUsernameList;

        // Remove the connection from the concatenated string
        removeHeader = connectionList.split("CONNECTIONlist:%CoNcAt%chatBoxServer.Connection@1a24k3c0");
        removeHeader = removeHeader[1].split(concatPattern);
        list = Arrays.asList(removeHeader);

        for (int i = 1; i < list.size(); i++) {
            if(i != indexToRemove + 1) {
                newConnectionList = newConnectionList + concatPattern + list.get(i);
            }
        }

        connectionList = newConnectionList;

//        for(int i = 0; i< usernameArrayList.size(); i++) {
//            System.out.println("Name: " + usernameArrayList.get(i) + " connectionid: " + connectionArrayList.get(i));
//        }
//        System.out.println(usernameList + "\n");
//        System.out.println(connectionList + "\n");

        // If there is no connection to the server then exit the server
        // else send the updated online user to every client.
        if(serverReference.getNumberOfUsers() == 0){
            System.out.println("Exit the server.");
            System.exit(-1);
        } else {
            serverReference.broadcastMessage(usernameList);
            serverReference.broadcastMessage(connectionList);
        }
    }

    public void broadcastConnectionIDToClient() {
        try {
            String header = "CONNECTIONid:";
            header = header + serverReference.getConnectionID().toString();
            printOutWriter.println(header);
        } catch (Exception e) {
            throw e;
        }

    }


    public void sendMessages(String message) {
        printOutWriter.println(message);
    }

    public ArrayList<String> getUsernameArrayList () {
        return usernameArrayList;
    }

    public ArrayList<String> getConnectionArrayList () {
        return connectionArrayList;
    }


}
