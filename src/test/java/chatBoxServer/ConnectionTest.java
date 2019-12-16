package chatBoxServer;

import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
}