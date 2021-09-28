package org.group77.mejl.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.group77.mejl.Main;
import org.group77.mejl.model.ApplicationManager;
import org.group77.mejl.model.Email;
import org.group77.mejl.model.Folder;
import org.group77.mejl.oldControllers.EmailItemController;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {


        private final ApplicationManager appManager = new ApplicationManager();

        @FXML private FlowPane emailListItemFlowPane;
        @FXML public FlowPane readingFlowPane;

        @FXML
        private FlowPane flowPaneFolder;

        private void loadFolders() {
                try {
                    List<Folder> folders = appManager.refreshFromServer();
                    flowPaneFolder.getChildren().clear();
                    for (Folder f: folders) {
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FolderItemView.fxml"));
                        flowPaneFolder.getChildren().add(fxmlLoader.load());
                        FolderItemController c = fxmlLoader.getController();
                        c.init(f, this);
                    }
                }catch (MessagingException | IOException e) {
                    e.printStackTrace();
                }

        }


        @FXML
        private ComboBox<String> accountsComboBox;


        /**
         * @Author David Zamanian
         *
         * This method will be called when starting the application and calls loadEmails to load the emails into listItems into the flowPane
         *
         * @param url
         * @param rb
         */


        public void initialize(URL url, ResourceBundle rb) {
                populateAccountsComboBox();

                //For testing only
                String testAddress1 = "from@gmail.com";
                String testAddress2 = "from@gmail.com";
                String testAddress3 = "from@gmail.com";
                String testAddress4 = "from@gmail.com";
                List<String> to = Arrays.asList("lol@gmail.com",
                        "lol2@gmail.com",
                        "lol3@gmail.com");
                List<Email> emails = Arrays.asList(
                        new Email(testAddress1, to, "Subject 1", "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
                                "\n" +
                                "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham. Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
                                "\n" +
                                "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham. Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
                                "\n" +
                                "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham."),
                        new Email(testAddress2, to, "Subject 2", "Email 2"),
                        new Email(testAddress3, to, "Subject 3", "Email 3"),
                        new Email(testAddress4, to, "Subject 4", "Email 4")
                );

                // loadFolders(); TODO implement getActiveAccount();

                loadEmails(emails);

        }



        @FXML
        public void refresh(){};

        @FXML
        public void switchAccount(){};

        @FXML
        public void openAddAccountView(){};


        /**
         * @author David Zamanian
         *
         * Iterates through the list of emails given and creates a listItem for each of them and shows
         * them in the emailListItemflowPane.
         *
         * @param emails the list of emails that will be shown in the listItems
         * @throws IOException
         */


        @FXML
        public void loadEmails(List<Email> emails){
                emailListItemFlowPane.getChildren().clear();
                try {

                for (Email email : emails){
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ListItemView.fxml"));
                        emailListItemFlowPane.getChildren().add(fxmlLoader.load());
                        listItemController controller = fxmlLoader.getController();
                        controller.init(email, this);
                }
                } catch(IOException e){
                    e.printStackTrace();
                }
        }




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
                AddAccountController c = fxmlLoader.getController();
                c.init(appManager);
                Stage stage = new Stage();
                stage.setTitle("add account");
                stage.setScene(scene);
                stage.show();
        }

        // IN DEVELOPMENT
        @FXML
        private void fetchMails() {
        }

        /**
         * Populates the ComboBox with the currently stored account
         * and also adds option to add a new account at the end of the list.
         *
         * Also adds listener to ComboBox that either changes activeAccount to the selected account,
         * or opens AddAccountView if the "add a new account"-item is selected.
         *
         * Probably needs some more work
         *
         * @author Elin Hagman
         */

        private void populateAccountsComboBox() {

                List<String> emailAddresses = appManager.getEmailAddresses();

                // Add every emailAddress to the ComboBox and the option to add a new account last
                for (String emailAddress : emailAddresses) {
                        accountsComboBox.getItems().add(emailAddress);
                }
                accountsComboBox.getItems().add("Add new account");

                // Add onAction to ComboBox
                accountsComboBox.setOnAction((event) -> {
                        // selectedIndex shows which index in ComboBox was chosen
                        int selectedIndex = accountsComboBox.getSelectionModel().getSelectedIndex();
                        // selectedItem shows selected item's value
                        String selectedEmailAddress = accountsComboBox.getSelectionModel().getSelectedItem();

                        if (selectedIndex == emailAddresses.size()) {
                                System.out.println("Open add account view");
                                // TODO: Also have to change prompt text in ComboBox to activeAccount, but what to do if there is no activeAccount?
                                String activeAccount = appManager.getActiveAccount().getEmailAddress();
                                accountsComboBox.setPromptText(activeAccount);
                        } else {
                                appManager.setActiveAccount(selectedEmailAddress);
                                System.out.println("changed account to " + selectedEmailAddress );
                        }

                });

        }


}
