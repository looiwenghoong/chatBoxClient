package clientchatui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 *
 * @author looiwenghoong
 */
public class ReadThread implements Runnable {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;
    private List<String> nameOutput = new ArrayList<>();
    private List<String> connectionOutput = new ArrayList<>();
    private String concatPattern = "%CoNcAt%";
    private HashMap<String, String> userHashMap;
    private ClientChatUI application;
    private String connectionID;
    private int selfIndex;
    private int numberOfUsers = 0;


    public ReadThread(Socket socket, ChatClient client, ClientChatUI application) {
        this.socket = socket;
        this.client = client;
        this.application = application;

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
                if(response.startsWith("USERNAMElist:") || response.startsWith("CONNECTIONlist:") || response.startsWith("CONNECTIONid:")) {
                    if(response.startsWith("USERNAMElist:")) {
                        String[] removeHeader = response.split("(USERNAMElist:)");
                        nameOutput = Arrays.asList(removeHeader[1].split(concatPattern));
                    } else if(response.startsWith("CONNECTIONid:")) {
                        String[] removeHeader = response.split("(CONNECTIONid:)");
                        connectionID = removeHeader[1];
                    } else {
                        String[] removeHeader = response.split("(CONNECTIONlist:)");
                        connectionOutput = Arrays.asList(removeHeader[1].split(concatPattern));
                    }

                    if(nameOutput.size() == connectionOutput.size()) {
                        numberOfUsers = connectionOutput.size() - 2;
                        for (int i = 1; i < connectionOutput.size(); i++) {
                            if(connectionID.matches(connectionOutput.get(i))) {
                                selfIndex = i;
                                break;
                            }
                        }

                        application.controller.generateUsernameList(nameOutput, selfIndex, numberOfUsers);
                        updateHashMap();
                    }
                } else {
                    System.out.println(response);
                }
            } catch (Exception e) {
                System.out.println("The error is " + e);
            }
        }
    }

    public void updateHashMap() {
        userHashMap = new HashMap<>();
        for (int i = 1; i < nameOutput.size(); i++) {
            userHashMap.put(connectionOutput.get(i), nameOutput.get(i));
        }

        Set set = userHashMap.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
            System.out.println(mentry.getValue());
        }
    }
}