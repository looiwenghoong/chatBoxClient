/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchatui;

import java.awt.Window;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.text.Text;

/**
 *
 * @author ZhengKhai
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button home;
    @FXML
    private Button login;
    @FXML
    private Button close;
    @FXML
    private Button send;

    @FXML
    private ScrollPane usernameList;

    @FXML
    private TextField insertMsgTextField;
    @FXML
    private TextArea displayMessageTextArea;

    private ChatClient client;
    private String sendMessageTarget = null;
    private String yourConnectionID = null;


    public String loginUsername = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initClientInstance(ChatClient client) {
        this.client = client;
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Information");
        alert.setHeaderText("Logged in as:    " + loginUsername);
        alert.setContentText("Welcome to the chat server!!\nEnjoy your stay and start having great chats!");

        alert.showAndWait();
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

    public void generateUsernameList(List<String> usernameList, List<String> connectionList, int selfIndex, int numberOfUsers) {
        Platform.runLater(() -> {
            List<HBox> usernameCellList = new ArrayList<HBox>();
            String usernameString;
            for (int i = 1; i < usernameList.size(); i++) {
                if(i == selfIndex) {
                    usernameString = "You";
                    yourConnectionID = connectionList.get(i);
                } else {
                    usernameString = usernameList.get(i);
                }
                Image userIcon = null;
                try{
                    userIcon = new Image("image/userIcon.png", true);
                } catch (Exception e) {
                    System.out.println(e);
                }

                ImageView userIconView = new ImageView(userIcon);
                userIconView.setFitHeight(50);
                userIconView.setFitWidth(50);
                userIconView.setPreserveRatio(true);

                Label username = new Label();
                username.setText(usernameString);
                username.setPrefWidth(227);
                username.setPrefHeight(50);
                username.setFont(new Font("Arial", 15));
                username.setWrapText(true);

                HBox usernameCell = new HBox(userIconView, username);
                usernameCell.setId(connectionList.get(i));
                usernameCell.setSpacing(20);
                usernameCell.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, Insets.EMPTY)));
                usernameCell.setMargin(userIconView, new Insets(10, 0, 4, 20));
                usernameCell.setMargin(username, new Insets(10, 0, 4, 0));


                if(i != selfIndex) {
                    if(numberOfUsers > 1){
                        usernameCell.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                usernameCell.setBackground(new Background(new BackgroundFill(Color.rgb(211,211,211), CornerRadii.EMPTY, Insets.EMPTY)));
                            }
                        });

                        usernameCell.setOnMouseReleased(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                usernameCell.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, Insets.EMPTY)));
                                sendMessageTarget = usernameCell.getId();
                            }
                        });
                    }
                } else {
                    usernameCell.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 102), CornerRadii.EMPTY, Insets.EMPTY)));
                }

                // Add Hbox to the Hbox List
                usernameCellList.add(usernameCell);
            }

            updateUsernameList(usernameCellList);
        });
    }

    public void updateUsernameList(List<HBox> usernameCellList) {
        Platform.runLater(() -> {
            VBox root = new VBox();

            for (int i = 0; i<usernameCellList.size(); i++) {
                root.getChildren().add(usernameCellList.get(i));
            }
            usernameList.setContent(root);
            usernameList.setPannable(true);
        });
    }
        
    @FXML
    private void sendButton(ActionEvent event) {
        String messageHeader = "MSGheaderFromCLIENT:";
        String messageBody = "MSGbodyFromCLIENT:";
        String fromToTag = yourConnectionID + "==>" + sendMessageTarget;
        String message = insertMsgTextField.getText();
        String encodedMessage = messageHeader + fromToTag + messageBody + message;
        client.writeMessage(encodedMessage);

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


    public String getUsername() {
        return loginUsername;
    }
}
