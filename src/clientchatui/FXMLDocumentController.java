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
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
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
    @FXML
    private TextField insertMsgTextField;

    public String loginUsername = null;

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
        createLoginDialog();
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
        System.out.println(insertMsgTextField.getText());
    }

    public void createLoginDialog() {
        // Create dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Login");
        dialog.setHeaderText("Please insert your Username");


        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create  username and password
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        username.setPromptText("Username");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);

        // disable login when no username is entered
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Enable Login Button if there's username
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        //  focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return username.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {
            loginUsername = result.get();
            System.out.println(loginUsername);
        } else {
            createLoginDialog();
        }
    }
}
