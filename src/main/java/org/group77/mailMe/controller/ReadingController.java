package org.group77.mailMe.controller;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.web.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.group77.mailMe.Main;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.Control;
import org.group77.mailMe.model.data.*;
import javafx.concurrent.Worker.State;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ReadingController {
  @FXML private Label fromLabel;
  @FXML private Label subjectLabel;
  @FXML private Label toLabel;
  @FXML private Label dateLabel;
  @FXML private Button replyBtn;
  @FXML private Button trashBtn;
  @FXML private Button archiveBtn;
  @FXML private VBox vBox;
  @FXML private WebView webView;
  @FXML private ImageView archiveImg;
  @FXML private HBox attachmentsHBox;

  /**
   * 1. set initial values for nodes
   * 2. add event handlers to nodes
   * @param control the model
   * @param email the corresponding email
   * @author Martin, David
   */
  void init(Control control, Email email) {
    fromLabel.setText(email.from());
    subjectLabel.setText(email.subject());
    toLabel.setText(control.removeBrackets(Arrays.toString(email.to())));
    dateLabel.setText(email.date().toString());
    AttachmentsController(email);

    // set button action handler and button icon
    EventHandler<ActionEvent> archiveHandler = actionEvent -> moveEmailTo(control, "Archive");
    EventHandler<ActionEvent> restoreHandler = actionEvent -> moveEmailTo(control, "Inbox");
    setButtonHandler(control.getActiveFolder().get(), archiveHandler, restoreHandler);
    setButtonImage(control.getActiveFolder().get());

    WebEngine webEngine = webView.getEngine();
    webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> adjustHeigth(webEngine, newState));
    webEngine.loadContent(email.content());
    // attach event handlers
    trashBtn.setOnAction(i -> handleDelete(control));
    replyBtn.setOnAction(inputEvent -> WindowOpener.openReply(control, fromLabel.getText()));
  }


  /**
   * set image to either a archive-image or a restore-image depending on current folder
   * @author Martin
   * @param folder active folder
   */
  private void setButtonImage(Folder folder){
    archiveImg.setImage(new Image(
      String.valueOf((Main.class.getResource(
        (folder.name().equals("Archive") | folder.name().equals("Trash")) ? "images_and_icons/restore.png" : "images_and_icons/archive.png"
      )))
    ));
  }

  /**
   * finds folder based on folderName and then calls control.moveEmail()
   * @author Martin
   * @param control the control layer
   * @param folderName the move-email-to-folder-name
   */
  private void moveEmailTo(Control control, String folderName){
    Optional<Folder> maybeFolder = control
      .getFolders()
      .stream()
      .filter(folder -> folder.name().equals(folderName))
      .findFirst();
    maybeFolder.ifPresent(folder -> {
      try{
        control.moveEmail(folder);
      }catch (Exception e){
        e.printStackTrace();
      }
    });
  }

  /**
   * try to remove event handler, if fail then do nothing
   * @author Martin
   * @param node the node to remove event handler
   * @param eventHandler the event handler to be removed
   */
  private void removeEventHandler(Node node, EventHandler<ActionEvent> eventHandler){
    try{
     node.removeEventHandler(ActionEvent.ACTION, eventHandler);
    }catch (NullPointerException ignore){}
  }

  /**
   * 1. remove all existing even handlers
   * 2. set a new event handler depending on current folder
   * @author Martin
   * @param folder the current folder
   * @param archiveHandler handler to execute if archive button
   * @param restoreHandler handler to execute if restore button
   */
  private void setButtonHandler(Folder folder,EventHandler<ActionEvent> archiveHandler, EventHandler<ActionEvent> restoreHandler){
    if(folder == null){
      return;
    }
    removeEventHandler(archiveBtn, archiveHandler);
    removeEventHandler(archiveBtn, restoreHandler);
    archiveBtn.setOnAction(
      (folder.name().equals("Archive") | folder.name().equals("Trash")) ? restoreHandler : archiveHandler
    );
  }

  /**
   * @author Martin
   * @param webEngine the webviews webengine
   * @param newState the state of the webengine
   * once the web content is loaded
   * 1. get height of document
   * 2. adjust height accordingly
   */
  private void adjustHeigth(WebEngine webEngine, State newState){
    if (newState == State.SUCCEEDED) {
      double height = Double.parseDouble(((String) webEngine.executeScript(
        "window.getComputedStyle(document.body, null).getPropertyValue('height')"
      )).replace("px", ""));
      webView.setPrefHeight(height);
      vBox.setPrefHeight(height);
    }
  }

  /**
   * Moves email to trash if not already in trash. If in trash --> Deletes permanently (but with confirmation).
   * @author David
   * @param control the control layer
   */
  private void handleDelete(Control control) {
    if ((control.getActiveFolder().get().name().equals("Trash"))) {
      try {
        if (customAlert("Are you sure you want to permanently delete this email?", Alert.AlertType.CONFIRMATION).get().equals(ButtonType.OK)) {
          control.permDeleteEmail();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      try {
        control.deleteEmail();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Displays a confirmation alert when you try to permanently delete an email.
   * @param message Type in your message you want user to see in an alert.
   * @param alertType What types of alert you want to display (CONFIRMATION in this case)
   * @author David Zamanian
   */
  private Optional<ButtonType> customAlert(String message, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle("Delete Email");
    alert.setContentText(message);
    alert.getDialogPane().setPrefSize(300, 200);
    Optional<ButtonType> result = alert.showAndWait();
    return result;
  }

  /**
   * Removes brackets from a string. Used to remove "[" and "]" from recipients.
   * @param s
   * @author David Zamanian
   */
  private String removeBrackets(String s) {
    s = s.replaceAll("[\\[\\](){}]", "");
    return s;
  }

  /**
   * @author Alexey Ryabov
   * @param email - Received email.
   */
  private void AttachmentsController(Email email){
    if (email.attachments() != null){
      //For every attachment in the list.
      for (Map.Entry<byte[], String> attachment : email.attachmentsAsBytes().entrySet()){
        try {
          //Creating a button with attachment name.
          Button button = hBoxButtonSetup(attachment);
          AttachmentButtonAction(attachment.getKey(), attachment.getValue(), button);
          //Adding a button to HBox.
          attachmentsHBox.setSpacing(5);
          attachmentsHBox.getChildren().add(button);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * @author Alexey Ryabov
   * @param attachment - list of attachments
   * @return button for the HBox.
   */
  private Button hBoxButtonSetup(Map.Entry<byte[], String> attachment) {
    //Creating image for attachments button
    ImageView iv = new ImageView(new Image(String.valueOf(Main.class.getResource("images_and_icons/attachmentIcon.png"))));
    iv.setFitHeight(30);
    iv.setFitWidth(30);
    //Creating button for the HBox
    Button button = new Button();
    Tooltip tt = new Tooltip(); // Tooltip for the button with attachment's name.
    tt.setText(attachment.getValue());
    button.setTooltip(tt);
    button.setGraphic(iv);
    button.setMinSize(30,30);
    button.setMaxSize(30, 30);
    button.setStyle("-fx-background-color: white"); //  + "-fx-border-color: black;"
    return button;
  }

  /**
   * @author - Alexey Ryabov
   * @param fileToSave - array of the attachment.
   * @param fileName - file name of the attachment.
   * @param button - button, with info about its attachment.
   */
  public void AttachmentButtonAction (byte[] fileToSave, String fileName, Button button) {
    button.setOnAction( e -> {
      try {
        //On button action a file chooser going to open.
        OpenFileChooser((Stage)button.getScene().getWindow(), fileToSave, fileName);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    });
  }

  /**
   * @author - Alexey Ryabov
   * @param stage - stage where save window is going to be displayed.
   * @param fileToSave - array of the attachment.
   * @param fileName - name of the attachment.
   * @throws IOException
   */
  public void OpenFileChooser (Stage stage, byte[] fileToSave, String fileName) throws IOException {
    //Creating file chooser.
    FileChooser fileChooser = new FileChooser();
    //Set extension filter
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
    fileChooser.getExtensionFilters().add(extFilter);
    fileChooser.setInitialFileName(fileName);
    //Show save file dialog
    File filePath = fileChooser.showSaveDialog(stage);

    if(fileToSave != null){
      SaveFile(fileToSave, filePath);
    }
  }

  /**
   * @author Alexey Ryabov
   * @param content - byte array to be saved as a file.
   * @param file - path where file is going to be saved.
   */
  private void SaveFile(byte[] content, File file){
    try {
      FileOutputStream outputStream = new FileOutputStream(file);
      outputStream.write(content);
      outputStream.close();
      outputStream.flush();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

  }


}
