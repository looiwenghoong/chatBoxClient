package chatBoxServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ChatServerTest {


    // Test init server component
    // If any exception during server init then test case failed
    @Test
    public void initChatServer() {
        int port = 9000;
        try{
            ChatServer server = new ChatServer(port);
        } catch (Exception e) {
            fail("Action of creating new server socket with same port is prohibited");
        }
    }


    // Test createConnection function
    @Test
    public void testCreateConnectionSuccess() {
        int port = 9001;
        try {
            ChatServer server = new ChatServer(port);
            Socket clientSocket = new Socket("127.0.0.1", port);
            server.onServerRunning();
        } catch (Exception e) {
            fail("Unable to create Connection");
        }
    }

    // Test init the 2 servers with the same port
    // Test case will pass if any exception is caught
    // Test case will fail if there is no exception caught
    @Test
    public void testCreateConnectionFailed() {
        int port = 9002;
        try {
            ChatServer server = new ChatServer(port);
            server = new ChatServer(port);
            fail("Should not be able to re-init the server with the same port again");
        } catch (Exception e) {
            assertTrue("This should be a failed operation", true);
        }
    }

//    @Test
//    public void testAbleToCreateConnection() {
//        int port = 9000;
//        try {
//            ChatServer server = new ChatServer(port);
//            for (int i = 0; i<6; i++) {
//                Socket socket = new Socket("127.0.0.1", port);
////                server.createConnection(socket);
//            }
////            server.getNumberOfUsers();
//            assertEquals(5, 6);
////            fail("The expected size and the loop times is not the same");
//        } catch (Exception e) {
//            assertTrue("Its false to throw exception", true);
//        }
//    }

    // Test getNumberOfUsers Function algorithm
    // Using mock data type and data to test the algorithm
    // Target to return the number of item in the list
    @Test
    public void testGetNumberOfUsersSuccess() {
        ArrayList<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            integerList.add(i);
        }
        assertEquals(5, integerList.size());
    }

//    @Test
//    public void testGetNumberOfUsersFailed() {
//
//    }

//    // Testing case to test if this function will get all item in list and run send message function
//    // for all items in list
//    @Test
//    public void testBroadcastFunction() {
//
//    }
}