package org.group77.mailMe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.group77.mailMe.model.Control;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WritingController {
  @FXML private TextField toField;
  @FXML private Label fromLabel;
  //@FXML private TextArea contentField;
  @FXML private Button sendBtn;
  @FXML private Button attachBtn;
  @FXML private TextField subjectField;
  @FXML private HTMLEditor contentField;

  private final List<File> attachments = new ArrayList<File>();

  /**
   * normal init method when not replying
   * @param control the model
   * @author Martin, Alexey
   */
  public void init(Control control) {
    init(control, null);
  }

  /**
   * 1. set initial values for nodes
   * 2. set event handlers for nodes and state fields
   * @param control the model
   * @param to the email address which the user is replying to
   */
  public void init(Control control, String to) {
    if (to != null) {
      toField.setText(to);
    }
    fromLabel.setText(control.getActiveAccount().get().emailAddress());
    // input handlers
    sendBtn.setOnAction(inputEvent -> send(control));
    attachBtn.setOnAction(inputEvent -> attachFiles());

    // change handlers
    control.getActiveAccount().addObserver(newAccount -> fromLabel.setText(newAccount.emailAddress()));
  }

  /**
   * 1. send email
   * 2. display feedback if sending was successful
   * @param control the model
   * @author Alexey
   */
  private void send(Control control) {
    try {
      control.send(
        fromTextFieldToListOfRecipients(toField.getText()),
        subjectField.getText(),
        contentField.getHtmlText(),
        attachments
      );
      if (customAlert("Confirmation !", Alert.AlertType.CONFIRMATION).get() == ButtonType.OK) {closeWindowAction((Stage) sendBtn.getScene().getWindow());}
    } catch (Exception e) {
      if (customAlert(e.getMessage(), Alert.AlertType.ERROR).get() == ButtonType.OK) {closeWindowAction((Stage) sendBtn.getScene().getWindow());}
      e.printStackTrace(); // TODO display feedback
    }
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
      System.out.println("File selected >" + " " + selectedFile); // For Testing
    }
  }

  /**
   * @param textfield - toTextField.
   * @return list of recipints mail adresses.
   * @author Alexey Ryabov
   * This method converts TextField string to list of recipients separated with ";" -sign.
   */
  private List<String> fromTextFieldToListOfRecipients(String textfield) {
    //String textFieldToString = textfield.toString();
    String strings[] = textfield.split(";");
    List list = Arrays.asList(strings);

    //System.out.println(list); // For Testing.
    return list;
  }

  /** @author Alexey Ryabov
   * @param message - Type in your message you want user to see in an alert.
   * This will show conformation window when message has been sent.
   * @return optional button.
   */
  private Optional<ButtonType> customAlert (String message, Alert.AlertType alertType) {
    // Example of alert type: Alert.AlertType.INFORMATION
    Alert alert = new Alert(alertType);
    alert.setTitle("MeAlert");
    //alert.setHeaderText(message);
    alert.setContentText(message);
    alert.getDialogPane().setPrefSize(300, 300);
    Optional<ButtonType> result = alert.showAndWait();
    return result;
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
}
