package clientchatui;

import chatBoxServer.ChatServer;
import javafx.fxml.FXMLLoader;

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

    public ChatClient (String address, int port, String loginUsername, ClientChatUI application, FXMLDocumentController controller) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected to server at port " + port);

            writeUsernameToServer(loginUsername);

            // Thread to listen to incoming messages
            readThread = new ReadThread(socket, this, application, controller);
            Thread t = new Thread(readThread);
            t.start();
        } catch (UnknownHostException u) {
            System.out.println("Unable to connect to server");
        } catch (IOException e) {
            System.out.println("Unable to connect to server");
        }
    }

    public void writeUsernameToServer(String username) {
        ArrayList<String> usernameList = new ArrayList<String>();
        usernameList.add(username);
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(usernameList);
        } catch (Exception e) {
            System.out.println(e);
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
}
