package chatBoxServer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable {

    final static int STATE_UNREGISTERED = 0;
    final static int STATE_REGISTERED = 1;

    private volatile boolean running;
    private int messageCount;
    private int state;
    private Socket client;
    private ChatServer serverReference;
    private BufferedReader readerIn;
    private PrintWriter printOutWriter;
    private String username;
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
        String line;
        running = true;

        try {
            readerIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            printOutWriter = new PrintWriter(client.getOutputStream(), true);

            getNumberOfUsers();
            broadcastConnectionIDToClient();

            do {
                String clientMessage = dis.readUTF();
                System.out.println(clientMessage);
                serverReference.broadcastMessage(clientMessage);
            } while (running);
        } catch (IOException e) {
            System.out.println("in or out failed");
            System.exit(-1);
        }
    }

    public void getNumberOfUsers() {
        if(serverReference.getNumberOfUsers() == 0) {
            printOutWriter.println("No other users connected");
        } else {
            printOutWriter.println("Currently " + serverReference.getNumberOfUsers() + " users online.");
        }
    }

    public void broadcastConnectionIDToClient() {
        printOutWriter.println(serverReference.getConnectionID());
    }

    private void validateMessage(String message) {

        if(message.length() < 4){
            sendOverConnection ("BAD invalid command to server");
        } else {
            switch(message.substring(0,4)){
                case "LIST":
                    list();
                    break;

                case "STAT":
                    stat();
                    break;

                case "IDEN":
                    iden(message.substring(5));
                    break;

                case "HAIL":
                    hail(message.substring(5));
                    break;

                case "MESG":
                    mesg(message.substring(5));
                    break;

                case "QUIT":
                    quit();
                    break;

                default:
                    sendOverConnection("BAD command not recognised");
                    break;
            }
        }

    }

    private void stat() {
        String status = "There are currently "+serverReference.getNumberOfUsers()+" user(s) on the server ";
        switch(state) {
            case STATE_REGISTERED:
                status += "You are logged im and have sent " + messageCount + " message(s)";
                break;

            case STATE_UNREGISTERED:
                status += "You have not logged in yet";
                break;
        }
        sendOverConnection("OK " + status);
    }

    private void list() {
        switch(state) {
            case STATE_REGISTERED:
                ArrayList<String> userList = serverReference.getUserList();
                String userListString = new String();
                for(String s: userList) {
                    userListString += s + ", ";
                }
                sendOverConnection("OK " + userListString);
                break;

            case STATE_UNREGISTERED:
                sendOverConnection("BAD You have not logged in yet");
                break;
        }

    }

    private void iden(String message) {
        switch(state) {
            case STATE_REGISTERED:
                sendOverConnection("BAD you are already registerd with username " + username);
                break;

            case STATE_UNREGISTERED:
                String username = message.split(" ")[0];
                if(serverReference.doesUserExist(username)) {
                    sendOverConnection("BAD username is already taken");
                } else {
                    this.username = username;
                    state = STATE_REGISTERED;
                    sendOverConnection("OK Welcome to the chat server " + username);
                }
                break;
        }
    }

    private void hail(String message) {
        switch(state) {
            case STATE_REGISTERED:
                serverReference.broadcastMessage("Broadcast from " + username + ": " + message);
                messageCount++;
                break;

            case STATE_UNREGISTERED:
                sendOverConnection("BAD You have not logged in yet");
                break;
        }
    }

    public boolean isRunning(){
        return running;
    }

    private void mesg(String message) {

        switch(state) {
            case STATE_REGISTERED:

                if(message.contains(" ")) {
                    int messageStart = message.indexOf(" ");
                    String user = message.substring(0, messageStart);
                    String pm = message.substring(messageStart+1);
                    if(serverReference.sendPrivateMessage("PM from " + username + ":" + pm, user)){
                        sendOverConnection("OK your message has been sent");
                    } else {
                        sendOverConnection("BAD the user does not exist");
                    }
                }
                else{
                    sendOverConnection("BAD Your message is badly formatted");
                }
                break;

            case STATE_UNREGISTERED:
                sendOverConnection("BAD You have not logged in yet");
                break;
        }
    }

    private void quit() {
        switch(state) {
            case STATE_REGISTERED:
                sendOverConnection("OK thank you for sending " + messageCount + " message(s) with the chat service, goodbye. ");
                break;
            case STATE_UNREGISTERED:
                sendOverConnection("OK goodbye");
                break;
        }
        running = false;
        try {
            client.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        serverReference.removeDeadUsers();
    }

    private synchronized void sendOverConnection (String message){
        System.out.println(message);
    }

    public void messageForConnection (String message){
//        sendOverConnection(message);
        System.out.println(message);
    }

    public int getState() {
        stat();
        return state;
    }

    public String getUserName() {
        return username;
    }

    void sendMessages(String message) {
        printOutWriter.println(message);
    }
}
