/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchatui;

import java.awt.Window;
import java.io.FileInputStream;
import java.net.URL;
import java.util.*;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
    private Button help;
    @FXML
    private Button bugReport;
    @FXML
    private Button close;
    @FXML
    private Button sendBtn;
    @FXML
    private ScrollPane usernameList;
    @FXML
    private TextField insertMsgTextField;
    @FXML
    private ScrollPane messageArea;
    @FXML
    private Label targetClientLabel;
    @FXML
    private Label totalUsersLabel;

    private ChatClient client;
    private String sendMessageTarget = null; // Connection id of Targeted user
    private String sendMessageTargetUsername = null; // Connection id of Targeted user
    private String yourConnectionID = null; // Connection id of this client
    private HashMap<String, ArrayList<String>> userHashMap; // Hash Map for displaying messages
    private List<String> clientUsernameList = null;
    private List<String> clientConnectionList = null;


    public String loginUsername = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        insertMsgTextField.setEditable(false);
        insertMsgTextField.setDisable(true);
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
    private void helpButton(ActionEvent event) {
        List<String> choices = new ArrayList<>();
        choices.add("Cant receive message");
        choices.add("Connection problem");
        choices.add("Interface issues");
        choices.add("Performance issues");


        ChoiceDialog<String> dialog = new ChoiceDialog<>("Click to Select", choices);
        dialog.setTitle("Help");
        dialog.setHeaderText("How can we help you?");
        dialog.setContentText("What problem have you encountered?");
        

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
         System.out.println("Your choice: " + result.get());
        }
        result.ifPresent(letter -> System.out.println("Your choice: " + letter));
            Alert option = new Alert(AlertType.INFORMATION);
                  option.setTitle(" Help Box");
                  option.setHeaderText("Noted ! Sorry for inconvenience cause.");
                  option.setContentText("We will contact you as soon as possbile !");
                  option.showAndWait();
    }
    
    
    @FXML
    private void bugReportButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bug Report");
        alert.setHeaderText("Seems like you have discovered some bugs\t...");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Inform us ");
        ButtonType buttonTypeTwo = new ButtonType("Message us");
        ButtonType buttonTypeThree = new ButtonType("Call us");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

       alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
         // ... user chose "Inform us"
            System.out.println("Noted. Thanks, We will contact you as soon as possbile !");
            Alert option1 = new Alert(Alert.AlertType.WARNING);
            option1.setTitle(" Bug Report (Inform us)");
            option1.setHeaderText("Noted ! Thanks for helping us improving our software!");
            option1.setContentText("We will contact you as soon as possbile !");
            option1.showAndWait();
            
        } else if (result.get() == buttonTypeTwo) {
        // ... user chose "Message us"
            TextInputDialog dialog = new TextInputDialog("Type here");
            dialog.setTitle("Bug Report (Message us)");
            dialog.setHeaderText("Tell us what happen .\t.\t.");
            dialog.setContentText("Provide Description:");

        Optional<String> option2 = dialog.showAndWait();
            if (option2.isPresent()){
            System.out.println("Description of bugs: " + option2.get());
            Alert option2_1 = new Alert(Alert.AlertType.INFORMATION);
           option2_1.setTitle("Information Dialog");
           option2_1.setHeaderText(null);
           option2_1.setContentText("Noted. Thanks for your feedback.\nHave a great day !");
           option2_1.showAndWait();
        }

        option2.ifPresent(name -> System.out.println("Your name: " + name));
        } else if (result.get() == buttonTypeThree) {
          // ... user chose "Call us"
            Alert option3 = new Alert(Alert.AlertType.INFORMATION);
            option3.setTitle("Bug Report (Call us)");
            option3.setHeaderText(null);
            option3.setContentText("Contact us:\n010-XXXXXXX(Ooi Zheng Khai)\n\t\t\tor\n018-XXXXXXX(Looi Wei Hoong)");

            option3.showAndWait();
        } else {
        }
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
                    System.exit(-1);
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
            this.clientUsernameList = usernameList;
            this.clientConnectionList = connectionList;
            totalUsersLabel.setText(Integer.toString(numberOfUsers));

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
                                insertMsgTextField.setEditable(true);
                                sendBtn.setDisable(false);
                                insertMsgTextField.setEditable(true);
                                insertMsgTextField.setDisable(false);
                                sendMessageTarget = usernameCell.getId();
                                sendMessageTargetUsername = username.getText();
                                updateUsernameLabel(); // Update the username label when users click on the messaging target
                                try {
                                    updatedMsgArea(); // Update the msg area to display all the message;
                                } catch (Exception e) {}
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
        insertMsgTextField.setText("");
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

    // Onclick update the message area
    public void updatedMsgArea() {
        Platform.runLater(() -> {
            VBox msgCell = null;
            List<VBox> msgCellList = new ArrayList<>();
            ArrayList<String> msgList, msgComponent;

            msgList = userHashMap.get(sendMessageTarget);

            if(msgList == null) {
                messageArea.setContent(null);
                messageArea.setPannable(true);
            } else {
                for(int i = 0; i < msgList.size(); i++) {
                    if(sendMessageTarget.matches("chatBoxServer.Connection@1a24k3c0")) {
                        String[] removeHeader = msgList.get(i).split("(grp->MSGheaderFromCLIENT:)");

                        msgComponent = computeUsernameMsg(removeHeader);
                    } else {
                        String[] removeHeader = msgList.get(i).split("(pm->MSGheaderFromCLIENT:)");
                        msgComponent = computeUsernameMsg(removeHeader);
                    }

                    if(msgComponent.get(0).matches("You")) {
                        Label msgtext = new Label();
                        msgtext.setText(msgComponent.get(1));
                        msgtext.setMaxWidth(410);
                        msgtext.setWrapText(true);
                        msgtext.setPadding(new Insets(15, 15, 15, 15));
                        msgtext.setFont(new Font("Arial", 17));
                        CornerRadii cornerRadius =  new CornerRadii(10);
                        msgtext.setBackground(new Background(new BackgroundFill(Color.rgb(224, 224, 224), cornerRadius, Insets.EMPTY)));

                        HBox hbox = new HBox();
                        hbox.getChildren().add(msgtext);
                        hbox.setAlignment(Pos.BASELINE_RIGHT);
                        hbox.setPadding(new Insets(0,0,0,50));

                        msgCell = new VBox();
                        msgCell.getChildren().add(hbox);
                        msgCell.setPrefWidth(420);
                        msgCell.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        Label username = new Label();
                        username.setText(msgComponent.get(0));
                        username.setMaxWidth(410);
                        username.setFont(new Font("Verdana", 14));
                        username.setWrapText(true);
                        username.setPadding(new Insets(10, 10, 0, 10));

                        Label msgtext = new Label();
                        msgtext.setText(msgComponent.get(1));
                        msgtext.setMaxWidth(410);
                        msgtext.setWrapText(true);
                        msgtext.setPadding(new Insets(5, 10, 10, 10));
                        msgtext.setFont(new Font("Arial", 17));

                        VBox mCell = new VBox(username, msgtext);
                        CornerRadii cornerRaddius =  new CornerRadii(10);
                        mCell.setBackground(new Background(new BackgroundFill(Color.rgb(224, 224, 224), cornerRaddius, Insets.EMPTY)));

                        HBox hbox = new HBox();
                        hbox.getChildren().add(mCell);
                        hbox.setPadding(new Insets(0,50,0,15));
                        hbox.setAlignment(Pos.BASELINE_LEFT);

                        msgCell = new VBox();
                        msgCell.getChildren().add(hbox);
                        msgCell.setPrefWidth(420);
                        msgCell.setAlignment(Pos.CENTER_RIGHT);
                    }

                    msgCellList.add(msgCell);
                }

                VBox completeMsgCell = new VBox();
                completeMsgCell.setSpacing(5);
                completeMsgCell.setPadding(new Insets(10,0,0,0));
                for(int i = 0; i<msgCellList.size(); i++) {
                    completeMsgCell.getChildren().add(msgCellList.get(i));
                }
                messageArea.setContent(completeMsgCell);
                messageArea.setPannable(true);
            }
        });
    }

    public ArrayList<String> computeUsernameMsg(String[] header) {
        String msgHeader, msgBody, fromWho;
        ArrayList<String> msgComponent = new ArrayList<>();

        String[] removeHeader = header[1].split("(MSGbodyFromCLIENT:)");
        msgHeader = removeHeader[0];
        msgBody = removeHeader[1];

        removeHeader = msgHeader.split("(==>)");
        fromWho = removeHeader[0];

        if(fromWho.matches(yourConnectionID)){
            fromWho = "You";
        } else {
            int index = clientConnectionList.indexOf(fromWho);
            fromWho = clientUsernameList.get(index);
        }

        msgComponent.add(fromWho);
        msgComponent.add(msgBody);

        return msgComponent;
    }

    public void setMsgHashMap(HashMap<String, ArrayList<String>> userHashMap) {
        this.userHashMap = userHashMap;
        updatedMsgArea();
    }

    public void updateUsernameLabel() {
        targetClientLabel.setText(sendMessageTargetUsername);
    }





    public void test() {
        List<VBox> msgCellList = new ArrayList<>();
//
//        Label username = new Label();
//        username.setText("JEKLasdl");
//        username.setMaxWidth(410);
//        username.setFont(new Font("Verdana", 14));
//        username.setWrapText(true);
//        username.setPadding(new Insets(10, 10, 0, 10));
//
//        Label msgtext = new Label();
//        msgtext.setText("asldkjalsd");
//        msgtext.setMaxWidth(410);
//        msgtext.setWrapText(true);
//        msgtext.setPadding(new Insets(0, 10, 5, 10));
//        msgtext.setFont(new Font("Arial", 17));
//        CornerRadii cornerRadius =  new CornerRadii(10);
//        msgtext.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 0), cornerRadius, Insets.EMPTY)));
//
//        VBox msgCell = new VBox(username, msgtext);
//        CornerRadii cornerRaddius =  new CornerRadii(10);
//        msgCell.setSpacing(5);
//        msgCell.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 22), cornerRaddius, Insets.EMPTY)));
//
//        HBox hbox = new HBox();
//        hbox.getChildren().add(msgCell);
//        hbox.setAlignment(Pos.BASELINE_LEFT);
//
//        VBox mCell = new VBox();
//        mCell.getChildren().add(hbox);
//        mCell.setPrefWidth(420);
//        mCell.setAlignment(Pos.CENTER_RIGHT);



        Label msgtext = new Label();
        msgtext.setText("asldkjaasdsad  asdlaksjdljslad saldja sdjlkasjdlkj asldjlkasjdlklksajdlkjaslkd llsakjdlkj");
        msgtext.setMaxWidth(410);
        msgtext.setWrapText(true);
        msgtext.setPadding(new Insets(15, 15, 15, 15));
        msgtext.setFont(new Font("Arial", 17));
        CornerRadii cornerRadius =  new CornerRadii(10);
        msgtext.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 22), cornerRadius, Insets.EMPTY)));

        HBox hbox = new HBox();
        hbox.getChildren().add(msgtext);
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(0,0,0,50));

        VBox msgCell = new VBox();
        msgCell.getChildren().add(hbox);
        msgCell.setPrefWidth(420);
        msgCell.setAlignment(Pos.CENTER_RIGHT);

        msgCellList.add(msgCell);

        VBox completeMsgCell = new VBox();
        completeMsgCell.setSpacing(10);
        completeMsgCell.setPadding(new Insets(5, 10, 0, 10));
        for (int i = 0; i < msgCellList.size(); i++) {
            completeMsgCell.getChildren().add(msgCellList.get(i));
        }
        messageArea.setContent(completeMsgCell);
        messageArea.setPannable(true);
    }
}
