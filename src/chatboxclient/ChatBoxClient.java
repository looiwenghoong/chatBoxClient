/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatboxclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author ZhengKhai
 * @author LooiWengHoong
 */
public class ChatBoxClient {

    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    
    public ChatBoxClient (String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected to server at port " + port);
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException e) {
            System.out.println(e);
        }
    } 
    
    public void writeMessage(String msgString) {
//        Scanner scn = new Scanner(System.in); 
        if(msgString != "") {
//            String messageString = scn.nextLine();
            try {
                output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(msgString);
            } catch (Exception e) {
                System.out.println(e);
            }   
        }
    }
    
    public void readMessage() {
        while(true) {
            try {
                input = new DataInputStream(socket.getInputStream());
                String msg = input.readUTF();
                System.out.println(msg);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
