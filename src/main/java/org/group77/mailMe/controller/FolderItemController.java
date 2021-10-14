package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import org.group77.mailMe.model.Control;
import org.group77.mailMe.model.data.*;


public class FolderItemController {
  @FXML private Label nameLabel;
  @FXML private Button button;

  /**
   * 1. set initial values for nodes
   * 2. set event handlers for nodes
   * @param control control
   * @param folder the corresponding folder
   * @author Martin
   */
  void init(Control control, Folder folder) {
    nameLabel.setText(folder.name());
    button.getStyleClass().add(folder.name().equals("Inbox") ? "focused" : "unfocused");

    // attach event handlers
    button.setOnMouseClicked(i -> control.getActiveFolder().set(folder));
    control.getActiveFolder().addObserver(newFolder -> focused(newFolder, folder));
  }

  /**
   * @author Martin
   * @param newFolder the new folder
   * @param folder the folder associated with this controller
   */
  private void focused(Folder newFolder, Folder folder){
    button.getStyleClass().clear();
    button.getStyleClass().add(folder.equals(newFolder) ? "focused" : "unfocused");
  }
}
