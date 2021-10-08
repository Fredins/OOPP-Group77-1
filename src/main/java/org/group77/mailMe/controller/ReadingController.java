package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.StringConverter;
import org.group77.mailMe.*;
import org.group77.mailMe.controller.utils.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

import java.io.*;
import java.util.*;

public class ReadingController {
  @FXML private TextArea contentArea;
  @FXML private Label fromLabel;
  @FXML private Label subjectLabel;
  @FXML private Label toLabel;
  @FXML private Label dateLabel;
  @FXML private Button replyButton;
  @FXML private Button bin;
  @FXML private Button permDelete;
  @FXML private ComboBox<Folder> moveEmailComboBox;


  /**
   * 1. set initial values for nodes
   * 2. set event handler for node
   * @param model the model
   * @param email the corresponding email
   * @author Martin, David
   */
  void init(Model model, Email email) {
    contentArea.setText(email.content());
    fromLabel.setText(email.from());
    subjectLabel.setText(email.subject());
    toLabel.setText((Arrays.toString(email.to())));
    // TODO date
    bin.setOnAction(i -> {
      if((model.activeFolder.get().name().equals("Trash"))){
        try {
          PermDeleteEmail(model);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
      try {
        DeleteEmail(model);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }});
  /*
    permDelete.setOnAction(i -> {
      try {
        PermDeleteEmail(model);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

   */
    replyButton.setOnAction(inputEvent -> WindowOpener.openReply(model, fromLabel.getText()));
    if (model.folders != null) {
      PopulateFolderComboBox(model.folders.get(), model);
    }

    moveEmailComboBox.setOnAction(i -> {
      Folder selected = moveEmailComboBox.getSelectionModel().getSelectedItem();
      if (selected != null) {
        try {
          MoveEmail(selected, model);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
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
   * @param model holds the state of the application
   * @throws Exception
   * @author David Zamanian
   */

  @FXML
  private void DeleteEmail(Model model) throws Exception {
    model.DeleteEmail();
  }

  @FXML
  private void PermDeleteEmail(Model model) throws Exception {
    model.PermDeleteEmail();
  }

  /**
   * Calls the MoveEmail method in model
   * @param model holds the state of the application
   * @throws Exception
   * @author David Zamanian
   */

  @FXML
  private void MoveEmail(Folder folder, Model model) throws Exception {
    model.MoveEmail(folder);
  }

  /**
   * Populates the comboBox with the names of the folders in model's folders.
   *
   * @param folders all folders in model's folders
   * @param model holds the state of the application
   * @author David Zamanian
   */

  private void PopulateFolderComboBox(List<? extends Folder> folders, Model model){
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
          folder = model.folders.stream().filter(fol -> fol.name().equals(s))
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
