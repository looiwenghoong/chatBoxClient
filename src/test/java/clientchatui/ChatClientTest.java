package clientchatui;

import chatBoxServer.ChatServer;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;
import org.junit.Test;

import java.net.Socket;
import java.net.URL;

import static org.junit.Assert.*;

public class ChatClientTest {
    @Test
    public void testInitChatClient() {
        ChatClient chatClient = null;
        try {
            ChatServer server = new ChatServer(1000);
            URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            FXMLDocumentController controller = loader.getController();
            chatClient = new ChatClient("127.0.0.1", 1000, "user1", controller);
            server.onServerRunning();
        } catch (Exception e) {
            fail("Unable to create Connection");
        }

        assertTrue("ChatClient instance created", chatClient instanceof ChatClient);
    }

    @Test(expected = Test.None.class)
    public void testWriteUsernameToServer () {
        ChatClient chatClient = null;
        try {
            ChatServer server = new ChatServer(1001);
            URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            FXMLDocumentController controller = loader.getController();
            chatClient = new ChatClient("127.0.0.1", 1001, "user1", controller);

            server.onServerRunning();
        } catch (Exception e) {
            fail("Unable to create Connection");
        }

        chatClient.writeUsernameToServer("User1");
    }
}