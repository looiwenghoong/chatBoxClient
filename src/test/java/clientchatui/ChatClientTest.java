package clientchatui;

import chatBoxServer.ChatServer;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChatClientTest {
    @Test
    public void testInitChatClient() {
        try {
            URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            FXMLDocumentController controller = loader.getController();
            ChatClient chatClient = new ChatClient("127.0.0.1", 900, "user1", controller);

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
            ChatClient chatClient = new ChatClient("127.0.0.1", 1001, "User1", controller);
        } catch (Exception e) {
            fail("Unable to create Connection");
        }
    }

    @Test
    public void testWriteMessage() {

    }
}