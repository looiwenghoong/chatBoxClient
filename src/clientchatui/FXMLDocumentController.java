/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchatui;

import java.awt.Insets;
import java.awt.Window;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author ZhengKhai
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private Button home;
    @FXML
    private Button login;
    @FXML
    private Button close;
    @FXML
    private Button send;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void homeButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Home");
        alert.setHeaderText("Hi , Mr Nathan !");
        alert.setContentText("Have a good day ! \nTry out the buttons which is interactive !\nDont miss it out \t.\t .\t .");
        
        alert.showAndWait();
    }

    @FXML
    private void loginButton(ActionEvent event) {
        // Create dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Please insert your Username and Password");


        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create  username and password 
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // disable login when no username is entered
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
        loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        //  focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            
        if (dialogButton == loginButtonType) {
                        Alert option = new Alert(Alert.AlertType.INFORMATION);
                  option.setTitle(" Login");
                  option.setHeaderText("Service is curretly unavailable. Sorry for inconvenience cause.");
                  option.setContentText(" ----No Service ----");
                  option.showAndWait();
        return new Pair<>(username.getText(), password.getText());
        
        }
            return null;
        });

         Optional<Pair<String, String>> result = dialog.showAndWait();

         result.ifPresent(usernamePassword -> {
          System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
          });
         }

    @FXML
    private void closeButton(ActionEvent event) {
        
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Exit Confirmation");
    alert.setContentText("Are you sure??");
    alert.setHeaderText(null);
    ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
    alert.getButtonTypes().setAll(yesButton, noButton);

    alert.showAndWait().ifPresent(type -> {
        if (type == yesButton) {
             javafx.stage.Window window =   ((Node)(event.getSource())).getScene().getWindow(); 
            if (window instanceof Stage){
                ((Stage) window).close();
            }
        } else if (type == noButton) {
        } else {
        }
});
    }
        
    @FXML
    private void sendButton(ActionEvent event) {
    }
    
}
