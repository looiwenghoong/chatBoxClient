package clientchatui;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author looiwenghoong
 */
public class ReadThread implements Runnable {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;
    private ArrayList<String> clientName;
    String concatPattern = "%CoNcAt%";

    public ReadThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (Exception e) {
            System.out.println("Error getting input stream: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                String response = reader.readLine();
                if(response.startsWith("USERNAMElist:")) {
                    String[] removeHeader = response.split("(USERNAMElist:)");
                    String[] output = removeHeader[1].split(concatPattern);
                    for (int i = 1; i<output.length; i++) {
                        System.out.println(output[i]);
                    }
                } else {
                    System.out.println(response);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}