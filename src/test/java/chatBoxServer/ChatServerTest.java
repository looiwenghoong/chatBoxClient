package chatBoxServer;

import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChatServerTest {


    // Test init server component
    // If any exception during server init then test case failed
    @Test
    public void initChatServer() {
        int port = 9100;
        try{
            ChatServer server = new ChatServer(port);
        } catch (Exception e) {
            fail("Action of creating new server socket with same port is prohibited");
        }
    }


    // Test createConnection function
    @Test
    public void testCreateConnectionSuccess() {
        int port = 9101;
        try {
            ChatServer server = new ChatServer(port);
            Socket clientSocket = new Socket("127.0.0.1", port);
            server.onServerRunning();
        } catch (Exception e) {
            fail("Unable to create Connection");
        }
    }

    @Test
    public void testAddNewConnectionSuccess () {
        int port = 9003;
        try {
            // Create ChatServer instance to access the addNewConnectionToList function
            ChatServer server = new ChatServer(port);

            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(9004);

            // Create client socket 1
            // Create connection 1
            Socket clientSocket1 = new Socket("127.0.0.1", 9004);
            Socket s = serverSocket.accept();

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            Connection c1 = new Connection(s, server, dis, dos);

            // Create client socket 2
            // Create connection 2
            Socket clientSocket2 = new Socket("127.0.0.1", 9004);
            s = serverSocket.accept();

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            Connection c2 = new Connection(s, server, dis, dos);

            /***
             * Test Function addNewConnectionToList(Connection c)
             */
            // Add the custom connection to the list using ChatServer instance function
            server.addNewConnectionToList(c1);
            server.addNewConnectionToList(c2);

            /**
             * Test Function getNumberOfUsers()
             */
            assertEquals(2, server.getNumberOfUsers());

        } catch (Exception e) {
            fail("Error");
        }
    }

    /**
     * Test Case for getConnectionID()
     * If the return object is instance of class Connection then test case pass
     */
    @Test
    public void testGetConnectionID () {
        int port = 9005;
        try {
            ChatServer server = new ChatServer(port);
            Socket clientSocket = new Socket("127.0.0.1", port);
            server.onServerRunning();
            Connection c = server.getConnectionID();
            assertTrue("Able to get the connection id", c instanceof Connection);
        } catch (Exception e) {
            fail("Unable to create Connection");
        }
    }

    /**
     * Test Case for removeConnection(Connection connectionID)
     */
    @Test
    public void testRemoveConnection () {
        int port = 9006;
        try {
            // Create ChatServer instance to access the addNewConnectionToList function
            ChatServer server = new ChatServer(port);

            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(9007);

            // Create client socket 1
            // Create connection 1
            Socket clientSocket1 = new Socket("127.0.0.1", 9007);
            Socket s = serverSocket.accept();

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            Connection c1 = new Connection(s, server, dis, dos);

            // Create client socket 2
            // Create connection 2
            Socket clientSocket2 = new Socket("127.0.0.1", 9007);
            s = serverSocket.accept();

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            Connection c2 = new Connection(s, server, dis, dos);


            // Add the custom connection to the list using ChatServer instance function
            server.addNewConnectionToList(c1);
            server.addNewConnectionToList(c2);

            /**
             * removeConnection function
             */
            server.removeConnection(c1);

            assertEquals(1, server.getNumberOfUsers());

        } catch (Exception e) {
            fail("Error");
        }
    }

    /**
     * Test Case for verifying the list order after removing an element
     * from the list
     */
    @Test
    public void testRemoveConnectionListSequence() {
        int port = 9008;
        ArrayList<Connection> connectionList = new ArrayList<>();
        try {
            // Create ChatServer instance to access the addNewConnectionToList function
            ChatServer server = new ChatServer(port);

            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(9009);

            // Create client socket 1
            // Create connection 1
            Socket clientSocket1 = new Socket("127.0.0.1", 9009);
            Socket s = serverSocket.accept();

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            Connection c1 = new Connection(s, server, dis, dos);

            // Create client socket 2
            // Create connection 2
            Socket clientSocket2 = new Socket("127.0.0.1", 9009);
            s = serverSocket.accept();

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            Connection c2 = new Connection(s, server, dis, dos);

            // Create client socket 3
            // Create connection 3
            Socket clientSocket3 = new Socket("127.0.0.1", 9009);
            s = serverSocket.accept();

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            Connection c3 = new Connection(s, server, dis, dos);

            // Add connection to an array list for comparison with function output
            // ArrayList content:
            // Connection c1, Connection c3
            connectionList.add(c1);
            connectionList.add(c3);

            // Add the custom connection to the list using ChatServer instance function
            server.addNewConnectionToList(c1);
            server.addNewConnectionToList(c2);
            server.addNewConnectionToList(c3);

            // Remove connection c2
            server.removeConnection(c2);

            ArrayList<Connection> cList = server.getConnectionList();

            for (int i = 0; i < connectionList.size(); i++) {
                if (!connectionList.get(i).toString().matches(cList.get(i).toString())) {
                    fail("Array List Not Matching");
                }
            }
        } catch (Exception e) {
            fail("Error");
        }
    }
}