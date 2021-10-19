package org.group77.mailMe.controller;

import javafx.application.*;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.web.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.*;
import org.controlsfx.control.*;

import org.controlsfx.control.textfield.TextFields;

import org.group77.mailMe.Main;
import org.group77.mailMe.model.Control;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class WritingController {
  @FXML private TextField toField;
  @FXML private Label fromLabel;
  //@FXML private TextArea contentField;
  @FXML private Button sendBtn;
  @FXML private Button attachBtn;
  @FXML private TextField subjectField;
  @FXML private HTMLEditor contentField;
  @FXML private HBox attachmentsHBox;
  private final ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

  private final List<File> attachments = new ArrayList<>();

  /**
   * normal init method when not replying
   * @param control the model
   * @author Martin, Alexey, David
   */

  public void init(Control control) {
    init(control, null);
  }

  /**
   * 1. set initial values for nodes
   * 2. set event handlers for nodes and state fields
   * @param control the model
   * @param to the email address which the user is replying to
   * @author David
   */

  public void init(Control control, String to) {
    //Lets the tField get auto suggestions when typing
    TextFields.bindAutoCompletion(toField, splitAndMakeToList(control.getAutoSuggestions().get()));
    if (to != null) {
      toField.setText(to);
    }
    fromLabel.setText(control.getActiveAccount().get().emailAddress());
    // input handlers
    sendBtn.setOnAction(inputEvent -> send(control, ((Node) sendBtn)));
    attachBtn.setOnAction(inputEvent -> attachFiles());
    // change handlers
    control.getActiveAccount().addObserver(newAccount -> fromLabel.setText(newAccount.emailAddress()));
    //remove attachments
    attachmentsHBox.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getTarget() == attachmentsHBox) {
        clearAllAttachments();
      }
    });

  }

  /** Removes brackets from a list and takes the first element of that list and breaks it up where there are ; and creates a new list will all the new elements
   *
   * @param list a list with only one element
   * @return
   * @author David Zamanian
   */

  private List<String> splitAndMakeToList(List<String> list){
    if (!list.isEmpty()) {
      String theList = list.get(0);
      String[] strings = theList.split(";");
      return Arrays.asList(strings);
    } else return list;

  }


  /**
   * 1. try to send email
   * 2. display notification if successful or not
   * 3. close window
   * @author Martin
   * @param control the control layer
   * @param node any node in active scene
   */
  private void send(Control control, Node node){
    Notifications notification = Notifications.create()
      .position(Pos.TOP_CENTER)
      .hideAfter(Duration.seconds(2));

    threadExecutor.execute(() -> {
      try {
        control.addSuggestion(toField.getText());
        control.send(
          removeDuplicates(fromTextFieldToListOfRecipients(toField.getText())),
          subjectField.getText(),
          contentField.getHtmlText(),
          attachments
        );
        Platform.runLater(() -> notification
          .graphic(new Label("Message sent successfully!"))
          .show());
          // control.moveSentEmail(removeDuplicates(fromTextFieldToListOfRecipients(toField.getText())), subjectField.getText(), contentField.getHtmlText(), attachments, 2); // TODO alexey this trows exception
      } catch (Exception e) {
        System.out.println("here");
        e.printStackTrace();
        Platform.runLater(() -> notification
          .title("Failed")
          .text(e.getMessage())
          .showError());
      }
    });
    ((Stage) node.getScene().getWindow()).close();
  }

  /** @author Alexey Ryabov
   * Method creates file chooser and lets user to select multiple files and adds them to a list.
   */
  @FXML
  private void attachFiles() {
    FileChooser fileChooser = new FileChooser();
    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
      attachments.add(selectedFile);
      Button button = hBoxButtonSetup(selectedFile);
      attachmentsHBox.getChildren().add(button);
    }
  }

  /**
   * @author Alexey Ryabov
   * @param selectedFile - file that was added as an attachment.
   * @return button for the HBox.
   */
  private Button hBoxButtonSetup(File selectedFile) {
    //Creating image for attachments button
    ImageView iv = new ImageView(new Image(String.valueOf(Main.class.getResource("images_and_icons/attachmentIcon.png"))));
    iv.setFitHeight(30);
    iv.setFitWidth(30);
    //Creating button for the HBox
    Button button = new Button();
    Tooltip tt = new Tooltip(); // Tooltip for the button with attachment's name.
    tt.setText(selectedFile.getName());
    button.setTooltip(tt);
    button.setGraphic(iv);
    button.setMinSize(30,30);
    button.setMaxSize(30, 30);
    button.setStyle("-fx-background-color: white"); //  + "-fx-border-color: black;"

    return button;
  }

  /**
   * @author Alexey Ryabov
   * Removed all attachments from from HBox and List of attachments
   */
  public void clearAllAttachments () {
    attachments.clear();
    attachmentsHBox.getChildren().removeAll(attachmentsHBox.getChildren());
  }

  /**
   * @param textfield - toTextField.
   * @return list of recipints mail adresses.
   * @author Alexey Ryabov
   * This method converts TextField string to list of recipients separated with ";" -sign.
   */
  private List<String> fromTextFieldToListOfRecipients(String textfield) {
    //String textFieldToString = textfield.toString();
    String[] strings = textfield.split(";");
    return Arrays.asList(strings);
  }

  /** @author Alexey Ryabov
   * @param stage the stage of the window
   * For testing. This closes the stage of the window where certain stage is located.
   */
  @FXML
  private void closeWindowAction(Stage stage){
    stage.getScene().getWindow();
    // Close the window of a stage.
    stage.close();
  }

  /**
   * @author Alexey Ryabov
   * @param list - list of strings
   * @return - new list of string without duplicates
   */
  private List<String> removeDuplicates (List<String> list) {
    List<String> newList = new ArrayList<>();
    for(int i = 0; i < list.size(); i++){
      if( !newList.contains(list.get(i)) ){
        newList.add(list.get(i));
      }
    }
    return newList;
  }
}
