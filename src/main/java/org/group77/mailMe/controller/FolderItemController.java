package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;


public class FolderItemController {
  @FXML private Label nameLabel;
  @FXML private AnchorPane root;

  /**
   * 1. set initial values for nodes
   * 2. set event handlers for nodes
   */
  void init(Model model, Folder folder) {
    nameLabel.setText(folder.name());

    // input handlers
    root.setOnMouseClicked(i -> model.activeFolder.set(folder));
  }
}
