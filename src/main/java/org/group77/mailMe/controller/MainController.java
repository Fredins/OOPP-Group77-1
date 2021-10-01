package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.group77.mailMe.Main;
import org.group77.mailMe.control.ApplicationManager;
import org.group77.mailMe.model.Email;
import org.group77.mailMe.model.Folder;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {


  private final ApplicationManager appManager = new ApplicationManager();

  @FXML
  public FlowPane emailListItemFlowPane;
  @FXML
  public FlowPane readingFlowPane;

  @FXML
  private FlowPane flowPaneFolder;
  @FXML
  private ComboBox<String> accountsComboBox;

  private void loadFolders() {
    try {
      List<Folder> folders = null;
      flowPaneFolder.getChildren().clear();
      for (Folder f : folders) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FolderItemView.fxml"));
        flowPaneFolder.getChildren().add(fxmlLoader.load());
        FolderItemController c = fxmlLoader.getController();
        c.init(f, this);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * @param url
   * @param rb
   * @Author David Zamanian
   * <p>
   * This method will be called when starting the application and calls loadEmails to load the emails into listItems into the flowPane
   */
  public void initialize(URL url, ResourceBundle rb) {
    populateAccountsComboBox();
    addOnActionAccountsComboBox();

    //For testing only
                /*
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
                        new Email(testAddress4, to, "Subject 4", "Email 4"),
                        new Email(testAddress2, to, "Subject 2", "Email 2"),
                        new Email(testAddress3, to, "Subject 3", "Email 3"),
                        new Email(testAddress4, to, "Subject 4", "Email 4"),
                        new Email(testAddress2, to, "Subject 2", "Email 2"),
                        new Email(testAddress3, to, "Subject 3", "Email 3"),
                        new Email(testAddress4, to, "Subject 4", "Email 4"),new Email(testAddress2, to, "Subject 2", "Email 2"),
                        new Email(testAddress3, to, "Subject 3", "Email 3"),
                        new Email(testAddress4, to, "Subject 4", "Email 4"),
                        new Email(testAddress2, to, "Subject 2", "Email 2"),
                        new Email(testAddress3, to, "Subject 3", "Email 3"),
                        new Email(testAddress4, to, "Subject 4", "Email 4")
                );

                // loadFolders(); TODO implement getActiveAccount();

                loadEmails(emails);
                 */
  }

  @FXML
  public void refresh() {
    try {
      appManager.refreshFromServer();
      List<Email> emails = appManager.retrieveEmails("Inbox");
      FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FolderItemView.fxml"));
      flowPaneFolder.getChildren().add(fxmlLoader.load());
      FolderItemController c = fxmlLoader.getController();
      c.init(new Folder("Inbox", emails), this);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }



  @FXML
  public void switchAccount() {
  }

  ;

  @FXML
  public void openAddAccountView() {
  }

  ;

  /**
   * @param emails the list of emails that will be shown in the listItems
   * @throws IOException
   * @author David Zamanian
   * <p>
   * Iterates through the list of emails given and creates a listItem for each of them and shows
   * them in the emailListItemflowPane.
   */

  @FXML
  public void loadEmails(List<Email> emails) {
    emailListItemFlowPane.getChildren().clear();
    try {
      for (Email email : emails) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ListItemView.fxml"));
        emailListItemFlowPane.getChildren().add(fxmlLoader.load());
        listItemController controller = fxmlLoader.getController();
        controller.init(appManager, email, this);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @throws IOException
   * @author Alexey Ryabov
   */
  @FXML
  public void openWritingView() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("WritingView.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
    WritingController c = fxmlLoader.getController();
    c.init(appManager);
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

  /**
   * Populates this accountsComboBox with the accounts which are currently stored in appManager.
   *
   * @author Elin Hagman
   */
  @FXML
  private void populateAccountsComboBox() {

    accountsComboBox.getItems().clear();
    List<String> emailAddresses = appManager.getEmailAddresses();

    // Add every emailAddress to accountsComboBox
    for (String emailAddress : emailAddresses) {
      accountsComboBox.getItems().add(emailAddress);
    }

    System.out.println(accountsComboBox.getItems());

  }

  /**
   * Adds listener to this accountComboBox which sets the selected email address in the combobox
   * as activeAccount in this appManager.
   * <p>
   * Does not set new activeAccount if the selected account has the same email address as the current activeAccount.
   *
   * @author Elin Hagman
   */
  private void addOnActionAccountsComboBox() {
    // Add onAction to ComboBox
    accountsComboBox.setOnAction((event) -> {

      // selectedEmailAddress is the email address the user has clicked on
      String selectedEmailAddress = accountsComboBox.getSelectionModel().getSelectedItem();

      if (selectedEmailAddress != null) {

        // change activeAccount if it is null or if it is not the same as before
        if (appManager.getActiveAccount() == null
          || (!selectedEmailAddress.equals(appManager.getActiveAccount().getEmailAddress()))) {

          appManager.setActiveAccount(selectedEmailAddress);
          System.out.println("changed active account to " + selectedEmailAddress);
          System.out.println("active account: " + appManager.getActiveAccount().getEmailAddress());

        }
      }
    });
  }


}
