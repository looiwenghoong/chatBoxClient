package clientchatui;

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
    private HashMap<String, ArrayList<String>> userHashMap = new HashMap<>();
    private String connectionID;
    private int selfIndex;
    private int numberOfUsers = 0;
    private FXMLDocumentController controller;


    public ReadThread(Socket socket, ChatClient client, FXMLDocumentController controller) {
        this.socket = socket;
        this.client = client;
        this.controller = controller;

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

                        updateHashMap();
                        controller.generateUsernameList(nameOutput, connectionOutput, selfIndex, numberOfUsers);
                        controller.setMsgHashMap(userHashMap);
                    }
                } else {
                    decodeMessage(response);
                }
            } catch (Exception e) {
                System.exit(-1);
            }
        }
    }

    public void updateHashMap() {

        HashSet<String> unionKeys = null;

        // Insert updated user list from server
        HashMap<String, String> serverHashMap = new HashMap<>();
        HashMap<String, ArrayList<String>> clientHashMap = new HashMap<>();

        clientHashMap.putAll(userHashMap);

        for (int i = 1; i < nameOutput.size(); i++) {
            serverHashMap.put(connectionOutput.get(i), nameOutput.get(i));
        }

        // Remove users
        if(clientHashMap.size() > serverHashMap.size()){
            //Union of keys from both maps
            unionKeys = new HashSet<>(serverHashMap.keySet());
            unionKeys.addAll(clientHashMap.keySet());

            unionKeys.removeAll(serverHashMap.keySet());

//            System.out.println(unionKeys + " Left");

            Iterator iterator = unionKeys.iterator();
            while(iterator.hasNext()) {
                userHashMap.remove(iterator.next());
            }
        } else if(serverHashMap.size() > clientHashMap.size()) {
            //Union of keys from both maps
            unionKeys = new HashSet<>(clientHashMap.keySet());
            unionKeys.addAll(serverHashMap.keySet());

            unionKeys.removeAll(clientHashMap.keySet());

//            System.out.println(unionKeys + " Join");

            Iterator iterator = unionKeys.iterator();
            while(iterator.hasNext()) {
                String a = iterator.next().toString();
                userHashMap.put(a, new ArrayList<>());
            }
        } else {
            // Do Nothing
        }
    }

    public void decodeMessage(String response) {
        String msgHeader;
        String msgBody;
        String targetClient;
        String selfClient;
        ArrayList<String> msgList;
        if(response.startsWith("grp->MSGheaderFromCLIENT:")){
            String[] removeHeader = response.split("(grp->MSGheaderFromCLIENT:)");
            removeHeader =  removeHeader[1].split("(MSGbodyFromCLIENT:)");
            msgHeader = removeHeader[0];
            msgBody = removeHeader[1];

            removeHeader = msgHeader.split("(==>)");
            selfClient = removeHeader[0];
            targetClient = removeHeader[1];

            msgList = userHashMap.get(targetClient);
            msgList.add(response);
            userHashMap.put(targetClient, msgList);

            controller.setMsgHashMap(userHashMap);
        } else if(response.startsWith("pm->MSGheaderFromCLIENT:")) {
            String[] removeHeader = response.split("(pm->MSGheaderFromCLIENT:)");
            removeHeader =  removeHeader[1].split("(MSGbodyFromCLIENT:)");
            msgHeader = removeHeader[0];
            msgBody = removeHeader[1];

            removeHeader = msgHeader.split("(==>)");
            selfClient = removeHeader[0];
            targetClient = removeHeader[1];

            if(!selfClient.matches(connectionID)){
                msgList = userHashMap.get(selfClient);
                msgList.add(response);
                userHashMap.put(selfClient, msgList);
            } else if(!targetClient.matches(connectionID)) {
                msgList = userHashMap.get(targetClient);
                msgList.add(response);
                userHashMap.put(targetClient, msgList);
            }
            controller.setMsgHashMap(userHashMap);
        }
    }

    public void setConnectionOutput(List<String> connectionOutput) {
        this.connectionOutput = connectionOutput;
    }

    public void setNameOutput(List<String> nameOutput) {
        this.nameOutput = nameOutput;
    }

    public void setUserHashMap(HashMap<String, ArrayList<String>> userHashMap) {
        this.userHashMap = userHashMap;
    }

    public HashMap<String, ArrayList<String>> getUserHashMap() {
        return userHashMap;
    }

    public void print() {
        System.out.println(nameOutput.size());
    }
}