package org.group77.mailMe.controller;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.web.*;
import org.group77.mailMe.Main;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.Control;
import org.group77.mailMe.model.data.*;
import javafx.concurrent.Worker.State;

import java.util.*;

public class ReadingController {
  @FXML private Label fromLabel;
  @FXML private Label subjectLabel;
  @FXML private Label toLabel;
  @FXML private Label dateLabel;
  @FXML private Label attachmentsLabel;
  @FXML private Button replyBtn;
  @FXML private Button trashBtn;
  @FXML private Button archiveBtn;
  @FXML private VBox vBox;
  @FXML private WebView webView;
  @FXML private ImageView archiveImg;

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
    attachmentsLabel.setText(email.attachments());

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

}
