package clientchatui;

import chatBoxServer.ChatServer;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author ZhengKhai
 * @author LooiWengHoong
 */
public class ChatClient {

    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    public ObjectOutputStream oos;
    public ReadThread readThread;
    private ChatServer server = null;


    public ChatClient (String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected to server at port " + port);
        } catch (UnknownHostException u) {
            System.out.println("Unable to connect to server");
//            throw new RuntimeException("Unknown Host Exception in the Application",u);
        } catch (IOException e) {
            System.out.println("Unable to connect to server");
//            throw new RuntimeException("IO Exception in the Application",e);
        }
    }

    public void startReadWrite(String loginUsername, FXMLDocumentController controller) {
        writeUsernameToServer(loginUsername);
        // Thread to listen to incoming messages
        readThread = new ReadThread(socket, this, controller);
        Thread t = new Thread(readThread);
        t.start();
    }

    public void writeUsernameToServer(String username) {
        ArrayList<String> usernameList = new ArrayList<String>();
        usernameList.add(username);
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(usernameList);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("IO Exception", e);
        }
    }

    // Function to write message to the server
    public void writeMessage(String msgString) {
        if(msgString != "") {
            try {
                output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(msgString);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
