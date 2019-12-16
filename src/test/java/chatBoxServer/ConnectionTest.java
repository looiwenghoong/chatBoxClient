package chatBoxServer;

import clientchatui.ChatClient;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ConnectionTest {

    /**
     * Test case to create Connection instance
     */
    @Test
    public void testCreateConnectionInstance() {
        try {
            ChatServer server = new ChatServer(8000);

            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(8001);

            // Create client socket 1
            // Create connection 1
            Socket clientSocket1 = new Socket("127.0.0.1", 8001);
            Socket s = serverSocket.accept();

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            Connection c1 = new Connection(s, server, dis, dos);

            assertTrue("Connection instance created", c1 instanceof Connection);
        }catch (Exception e) {
            fail("Exception caught");
        }
    }

    /**
     * Testing on broadcastConnectionIDToClient()
     */
    @Test(expected = Test.None.class)
    public void testBroadcastConnectionIDToClient() {
        int port = 8002;
        try {
            // Create connection in ChatServer
            ChatServer server = new ChatServer(port);
            Socket clientSocket = new Socket("127.0.0.1", port);
            server.onServerRunning();

            // get the connection instance
            Connection c = server.getConnectionID();

            /**
             * Testing on broadcastConnectionIDToClient()
             */
            c.broadcastConnectionIDToClient();
        }catch (Exception e) {
            fail("Exception caught");
        }
    }

//    @Test
//    public void testRemoveConnection() {
//        int port = 8003;
//        try {
//            // Create connection in ChatServer
//            ChatServer server = new ChatServer(port);
//            Socket clientSocket = new Socket("127.0.0.1", port);
//            server.onServerRunning();
//
//            // get the connection instance
//            Connection c = server.getConnectionID();
//
//
//        }catch (Exception e) {
//            fail("Exception caught");
//        }
//    }


    /**
     * Test getNumberOfUsers()
     * Test case with one connection generated
     * If the output matches only one connection then test case is valid
     */
    @Test
    public void testGetNumberOfUsers() {
        int port = 8004;
        try {
            // Create connection in ChatServer
            ChatServer server = new ChatServer(port);
            Socket clientSocket = new Socket("127.0.0.1", port);
            server.onServerRunning();

            // get the connection instance
            Connection c = server.getConnectionID();

            String getNumberOfUsersString = c.getNumberOfUsers();

            assertTrue("String matches", getNumberOfUsersString.matches("Currently 1 users online."));
        }catch (Exception e) {
            fail("Exception caught");
        }
    }


    @Test
    public void testClientInfo() {
        int port = 8007;
        ArrayList<String> usernameList = new ArrayList<>();
        usernameList.add("user2");
        try {
            // Create connection in ChatServer
            ChatServer server = new ChatServer(port);

            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(8008);

            // Create client socket 1
            // Create connection 1
            Socket clientSocket1 = new Socket("127.0.0.1", 8008);
            try {
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket1.getOutputStream());
                oos.writeObject(usernameList);
            } catch (Exception e) {
                System.out.println(e);
            }
            Socket s = serverSocket.accept();

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            Connection c = new Connection(s, server, dis, dos);

            c.getClientUsername();

            /**
             * Test if getClientUsername is generating correct content
             */

            if(c.getUsernameArrayList().get(0).matches("user2") &&
               c.getConnectionArrayList().get(0).matches(c.toString()) ) {
                assertTrue("Content Matching", true);
            } else {
                fail("Content Not Matching");
            }
        }catch (Exception e) {
            System.out.println(e);
            fail("Exception caught");
        }
    }

    @Test
    public void testGetClientUsername() {
        int port = 8005;
        ArrayList<String> usernameList = new ArrayList<>();
        usernameList.add("user1");
        try {
            // Create connection in ChatServer
            ChatServer server = new ChatServer(port);

            // Create custom server socket
            ServerSocket serverSocket = new ServerSocket(8006);

            // Create client socket 1
            // Create connection 1
            Socket clientSocket1 = new Socket("127.0.0.1", 8006);
            try {
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket1.getOutputStream());
                oos.writeObject(usernameList);
            } catch (Exception e) {
                System.out.println(e);
            }
            Socket s = serverSocket.accept();

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            Connection c1 = new Connection(s, server, dis, dos);

            /**
             * Test getClientUsername
             */
            c1.getClientUsername();
            if(c1.getUsernameArrayList().size() == 2 && c1.getConnectionArrayList().size() == 2) {
                assertTrue("Successfully added new user", true);
            } else {
                fail("Unable to get client");
            }
        }catch (Exception e) {
            fail("Exception caught");
        }
    }

}