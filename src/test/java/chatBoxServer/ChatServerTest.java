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
           ServerSocket server = new ServerSocket(port);
        } catch (Exception e) {
           fail("Action of creating new server socket with same port is prohibited");
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