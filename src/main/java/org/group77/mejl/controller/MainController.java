package org.group77.mejl.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.group77.mejl.Main;
import org.group77.mejl.model.ApplicationManager;
import org.group77.mejl.model.Email;
import org.group77.mejl.oldControllers.EmailItemController;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

        ApplicationManager appManager;
        @FXML private FlowPane emailListItemFlowPane;

        @FXML private FlowPane flowPaneTrees;

        @FXML private Button writeEmailButton;


        /**
         * @Author David Zamanian
         *
         * This method will be called when starting the application and calls loadEmails to load the emails into listItems into the flowPane
         *
         * @param url
         * @param rb
         */

        public void initialize(URL url, ResourceBundle rb) {

                //For testing only
                String testAddress1 = "from@gmail.com";
                String testAddress2 = "from@gmail.com";
                String testAddress3 = "from@gmail.com";
                String testAddress4 = "from@gmail.com";
                List<String> to = Arrays.asList("lol@gmail.com",
                        "lol2@gmail.com",
                        "lol3@gmail.com");
                List<Email> emails = Arrays.asList(
                        new Email(testAddress1, to, "Subject 1", "Email 1"),
                        new Email(testAddress2, to, "Subject 2", "Email 2"),
                        new Email(testAddress3, to, "Subject 3", "Email 3"),
                        new Email(testAddress4, to, "Subject 4", "Email 4")
                );

                try {
                        loadEmails(emails);
                        loadFolders();
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }



        @FXML
        public void refresh(){};

        @FXML
        public void switchAccount(){};

        @FXML
        public void openAddAccountView(){};




        @FXML
        public void loadEmails(List<Email> emails) throws IOException {
                emailListItemFlowPane.getChildren().clear();
                for (Email email : emails){
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ListItemView.fxml"));
                        emailListItemFlowPane.getChildren().add(fxmlLoader.load());
                        listItemController controller = fxmlLoader.getController();
                        controller.init(email);
                }
        }




        @FXML
        private void loadFolders(){};

        /**
         * @author Alexey Ryabov
         * @throws IOException
         */
        @FXML
        public void openWritingView() throws IOException {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("WritingView.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle("New Me Mail");
                stage.setScene(scene);
                stage.show();
        }

        @FXML
        private void openEmailSettings() throws IOException {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AddAccountView.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 400);
                Stage stage = new Stage();
                stage.setTitle("mejl");
                stage.setScene(scene);
                stage.show();
        }

        // IN DEVELOPMENT
        @FXML
        private void fetchMails() {
        }
}
