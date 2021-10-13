package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.util.StringConverter;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.Control;
import org.group77.mailMe.model.data.*;

import java.util.*;

public class ReadingController {
  @FXML private WebView contentArea;
  @FXML private Label fromLabel;
  @FXML private Label subjectLabel;
  @FXML private Label toLabel;
  @FXML private Label dateLabel;
  @FXML private Button replyButton;
  @FXML private Button bin;
  @FXML private ComboBox<Folder> moveEmailComboBox;
  @FXML private Label attachmentsLabel;


  /**
   * 1. set initial values for nodes
   * 2. set event handler for node
   * @param control the model
   * @param email the corresponding email
   * @author Martin, David
   */
  void init(Control control, Email email) {
    contentArea.getEngine().loadContent(email.content());
    fromLabel.setText(email.from());
    subjectLabel.setText(email.subject());
    toLabel.setText(removeBrackets(Arrays.toString(email.to())));
    attachmentsLabel.setText(email.attachments());

    // TODO date
    //Moves email to trash if not already in trash. If in trash --> Deletes permanently (but with confirmation).
    bin.setOnAction(i -> {
      if((control.getActiveFolder().get().name().equals("Trash"))){
        try {
          if (customAlert("Are you sure you want to permanently delete this email?", Alert.AlertType.CONFIRMATION).get().equals(ButtonType.OK)){
          PermDeleteEmail(control);}
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
      try {
        DeleteEmail(control);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }});

    replyButton.setOnAction(inputEvent -> WindowOpener.openReply(control, fromLabel.getText()));
    if (control.getActiveFolders() != null) {
      PopulateFolderComboBox(control.getActiveFolders().get(), control);
    }

    moveEmailComboBox.setOnAction(i -> {
      Folder selected = moveEmailComboBox.getSelectionModel().getSelectedItem();
      if (selected != null) {
        try {
          MoveEmail(selected, control);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
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

  private String removeBrackets(String s){
    s = s.replaceAll("[\\[\\](){}]","");
    return s;
  }

  //TODO Could potentially move these methods below into MasterController but then we need to make some changes in the GUI

  /**
   * Calls the DeleteEmail method in model
   * @param control the facade to model
   * @throws Exception
   * @author David Zamanian
   */

  @FXML
  private void DeleteEmail(Control control) throws Exception {
    control.deleteEmail();
  }

  @FXML
  private void PermDeleteEmail(Control control) throws Exception {
    control.permDeleteEmail();
  }

  /**
   * Calls the MoveEmail method in model
   * @param control holds the state of the application
   * @throws Exception
   * @author David Zamanian
   */

  @FXML
  private void MoveEmail(Folder folder, Control control) throws Exception {
    control.moveEmail(folder);
  }

  /**
   * Populates the comboBox with the names of the folders in model's folders.
   *
   * @param folders all folders in model's folders
   * @param control holds the state of the application
   * @author David Zamanian
   */

  private void PopulateFolderComboBox(List<? extends Folder> folders, Control control){
    moveEmailComboBox.getItems().clear();
    moveEmailComboBox.setConverter(new StringConverter<Folder>() {
      @Override
      public String toString(Folder folder) {
        return folder!= null ? folder.name() : null;
      }
      @Override
      public Folder fromString(String s) {
        Folder folder = null;
        try {
          folder = control.getActiveFolders().stream().filter(fol -> fol.name().equals(s))
                  .findAny()
                  .orElseThrow(Exception::new);
        }catch (Exception e){
          e.printStackTrace();
        }
        return folder;
      }
    });
    moveEmailComboBox.getItems().addAll(folders);
  }
}
