package clientchatui;

import chatBoxServer.ChatServer;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChatClientTest {
    @Test
    public void testInitChatClient() {
        try {
            ChatClient chatClient = new ChatClient("127.0.0.1", 900);

            assertTrue("ChatClient instance created", chatClient instanceof ChatClient);
        } catch (Exception e) {
            fail("Unable to create Connection");
        }
    }

    @Test
    public void testWriteUsernameToServer () {
        ArrayList<String> clientName;
        try {
            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(1001);

            URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            FXMLDocumentController controller = loader.getController();
            ChatClient chatClient = new ChatClient("127.0.0.1", 1001);
            chatClient.startReadWrite("User1", controller);

            Socket s = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            try {
                Object obj = ois.readObject();
                clientName = (ArrayList<String>) obj;
                String username = clientName.get(0);
                assertTrue("Matching output", username.matches("User1"));
            } catch (IOException e) {
                fail("IO Exception");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testInitReadThread() {
        try {
            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(1002);

            URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            FXMLDocumentController controller = loader.getController();
            ChatClient chatClient = new ChatClient("127.0.0.1", 1002);
            Socket socket = chatClient.getSocket();
            ReadThread readThread = new ReadThread(socket, chatClient, controller);

            assertTrue("Read Thread created", readThread instanceof ReadThread);
        }catch (Exception e) {
            fail("Exception");
        }
    }
}