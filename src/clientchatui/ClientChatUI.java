/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchatui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 *
 * @author ZhengKhai
 * @author LooiWengHoong
 */
public class ClientChatUI extends Application {

    public ChatClient client;
    public String loginUsername;
    public FXMLLoader loader;
    public FXMLDocumentController controller;
    public String a = "";

    @Override
    public void start(Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("Client Chat Box");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(-1);
            }
        });

        controller = loader.getController();
        controller.createLoginDialog();
//        controller.test();

        loginUsername = controller.getUsername();
        if(loginUsername != null) {
            client = new ChatClient("127.0.0.1", 9000, loginUsername, this, loader);
            controller.initClientInstance(client);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
