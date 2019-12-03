/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatboxclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author ZhengKhai
 * @author LooiWengHoong
 */
public class ChatBoxClient {

    private Socket socket = null;
    private InputStream input = null;
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
}
