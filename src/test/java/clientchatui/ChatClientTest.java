package clientchatui;


import javafx.fxml.FXMLLoader;
import org.junit.Test;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;

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
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateHashMap () {
        List<String> nameOutput = new ArrayList<>();
        String connection = "Test Connection2";
        nameOutput.add("Test User1");
        nameOutput.add("Test User2");
        List<String> connectionOutput = new ArrayList<>();
        connectionOutput.add("Test Connection1");
        connectionOutput.add("Test Connection2");
        HashMap<String, ArrayList<String>> userHashMap = new HashMap<>();

        try {
            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(1003);

            URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            FXMLDocumentController controller = loader.getController();
            ChatClient chatClient = new ChatClient("127.0.0.1", 1003);
            Socket socket = chatClient.getSocket();
            ReadThread readThread = new ReadThread(socket, chatClient, controller);

            readThread.setNameOutput(nameOutput);
            readThread.setConnectionOutput(connectionOutput);
            readThread.setUserHashMap(userHashMap);

            readThread.updateHashMap();
            HashMap<String, ArrayList<String>> userHashMapTest = readThread.getUserHashMap();

            Set set = userHashMapTest.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();
                if(mentry.getKey().toString().matches("Test Connection2")) {
                    if(userHashMapTest.get(connection).size() == 0) {
                        assertTrue("Matching content in hashmap", true);
                    } else {
                        fail("Value Not Matching");
                    }
                } else {
                    fail("Key Not Matching");
                }
//                System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
//                System.out.println(mentry.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDecodeGroupMessage() {
        String connection1 = "Connection1";
        String connection2 = "Connection2";
        HashMap<String, ArrayList<String>> userHashMap = new HashMap<>();
        userHashMap.put("Connection1", new ArrayList<>());
        userHashMap.put("Connection2", new ArrayList<>());
        String message = "grp->MSGheaderFromCLIENT:"+connection1+"==>"+connection2+"MSGbodyFromCLIENT:Hello World";

        try {
            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(1004);

            URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            FXMLDocumentController controller = loader.getController();
            ChatClient chatClient = new ChatClient("127.0.0.1", 1004);
            Socket socket = chatClient.getSocket();
            ReadThread readThread = new ReadThread(socket, chatClient, controller);

            readThread.setUserHashMap(userHashMap);
            readThread.decodeMessage(message);

            HashMap<String, ArrayList<String>> userHashMapTest = readThread.getUserHashMap();

            ArrayList<String> msgList = userHashMapTest.get(connection2);
            if(msgList.get(0).matches(message)) {
                assertTrue("Input output matching", true);
            } else {
                fail("Input Output Mismatch");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDecodePrivateMessageClient1() {
        String connection1 = "Connection1";
        String connection2 = "Connection2";
        HashMap<String, ArrayList<String>> userHashMap = new HashMap<>();
        userHashMap.put("Connection1", new ArrayList<>());
        userHashMap.put("Connection2", new ArrayList<>());
        String message = "pm->MSGheaderFromCLIENT:"+connection1+"==>"+connection2+"MSGbodyFromCLIENT:Hello World";

        try {
            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(1005);

            URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            FXMLDocumentController controller = loader.getController();
            ChatClient chatClient = new ChatClient("127.0.0.1", 1005);
            Socket socket = chatClient.getSocket();
            ReadThread readThread = new ReadThread(socket, chatClient, controller);

            readThread.setConnectionID(connection2);
            readThread.setUserHashMap(userHashMap);
            readThread.decodeMessage(message);

            HashMap<String, ArrayList<String>> userHashMapTest = readThread.getUserHashMap();

            ArrayList<String> msgList1 = userHashMapTest.get(connection1);
            if(msgList1.get(0).matches(message)) {
                assertTrue("Input output matching", true);
            } else {
                fail("Input Output Mismatch");
            }
//            Set set = userHashMapTest.entrySet();
//            Iterator iterator = set.iterator();
//            while(iterator.hasNext()) {
//                Map.Entry mentry = (Map.Entry)iterator.next();
//                System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
//                System.out.println(mentry.getValue());
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDecodePrivateMessageClient2() {
        String connection1 = "Connection1";
        String connection2 = "Connection2";
        HashMap<String, ArrayList<String>> userHashMap = new HashMap<>();
        userHashMap.put("Connection1", new ArrayList<>());
        userHashMap.put("Connection2", new ArrayList<>());
        String message = "pm->MSGheaderFromCLIENT:"+connection1+"==>"+connection2+"MSGbodyFromCLIENT:Hello World";

        try {
            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(1006);

            URL fxmlURL = ClassLoader.getSystemResource("FXMLDocument.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlURL);
            FXMLDocumentController controller = loader.getController();
            ChatClient chatClient = new ChatClient("127.0.0.1", 1006);
            Socket socket = chatClient.getSocket();
            ReadThread readThread = new ReadThread(socket, chatClient, controller);

            readThread.setConnectionID(connection1);
            readThread.setUserHashMap(userHashMap);
            readThread.decodeMessage(message);

            HashMap<String, ArrayList<String>> userHashMapTest = readThread.getUserHashMap();

            ArrayList<String> msgList2 = userHashMapTest.get(connection2);
            if(msgList2.get(0).matches(message)) {
                assertTrue("Input output matching", true);
            } else {
                fail("Input Output Mismatch");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}