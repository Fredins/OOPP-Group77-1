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

    // input handlers
    button.setOnMouseClicked(i -> control.getActiveFolder().set(folder));
    // change handlers
    control.getActiveFolder().addObserver(newFolder -> {
        button.getStyleClass().clear();
        button.getStyleClass().add(folder.equals(newFolder) ? "focused" : "unfocused");
    });
  }
}
