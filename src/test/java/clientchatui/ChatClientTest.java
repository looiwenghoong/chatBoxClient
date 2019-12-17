package clientchatui;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class ChatClientTest {
    @Test
    public void testInitChatClient() {
        URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
        System.out.println(fxmlURL);
        FXMLLoader loader = new FXMLLoader(fxmlURL);
        FXMLDocumentController controller = loader.getController();
        ChatClient chatClient = new ChatClient("127.0.0.1", 1000, "user1", controller);

        assertTrue("ChatClient instance created", chatClient instanceof ChatClient);
    }

//    @Test()
//    public void testInitChatClientWithException() {
//        URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
//        System.out.println(fxmlURL);
//        FXMLLoader loader = new FXMLLoader(fxmlURL);
//        FXMLDocumentController controller = loader.getController();
//        ChatClient chatClient = new ChatClient("127.0.0.1", 1000, "user1", controller);
//
//        assertTrue("ChatClient instance created", chatClient instanceof ChatClient);
//    }
}