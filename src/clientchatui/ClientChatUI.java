/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchatui;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author ZhengKhai
 * @author LooiWengHoong
 */
public class ClientChatUI extends Application {

//    private FXMLDocumentController controller;
    public ChatClient client;
    public String loginUsername;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("Client Chat Box");
        stage.setScene(scene);
        stage.show();

        FXMLDocumentController controller = loader.getController();

//        controller.updateUsernameList();
//        controller.generateUsernameList();
        controller.createLoginDialog();
        loginUsername = controller.getUsername();
        if(loginUsername != null) {
            client = new ChatClient("127.0.0.1", 9000, loginUsername);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
