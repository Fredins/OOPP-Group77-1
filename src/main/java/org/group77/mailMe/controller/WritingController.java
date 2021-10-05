package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.util.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;

public class WritingController {

  @FXML private TextField toField;
  @FXML private Label fromLabel;
  @FXML private TextField contentField;
  @FXML private Button sendBtn;
  @FXML private Button attachBtn;
  @FXML private TextField subjectField;

  private final List<String> attachments = new ArrayList<>();

  public void init(Model model) {
    init(model, null);
  }

  public void init(Model model, String to) {
    if (to != null) {
      toField.setText(to);
    }
    fromLabel.setText(model.activeAccount.get().emailAddress());
    // input handlers
    sendBtn.setOnAction(i -> send(model));
    attachBtn.setOnAction(i -> attachFiles());

    // change handlers
    model.activeAccount.addListener((ChangeListener<? super Account>) (o, oa, na) -> fromLabel.setText(na.emailAddress()));
  }

  private void send(Model model) {
    try {
      model.send(
        fromTextFieldToListOfRecipients(toField.getText()),
        subjectField.getText(),
        contentField.getText(),
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
      attachments.add(selectedFile.toString());
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
    alert.setHeaderText(message);
    //alert.setContentText("Are you ok with this?");
    alert.getDialogPane().setPrefSize(300, 300);
    Optional<ButtonType> result = alert.showAndWait();
    return result;
  }

  /** @author Alexey Ryabov
   * For testing. This closes the stage of the window where certain stage is located.
   */
  @FXML
  private void closeWindowAction(Stage stage){
    stage.getScene().getWindow();
    // Close the window of a stage.
    stage.close();
  }
}
