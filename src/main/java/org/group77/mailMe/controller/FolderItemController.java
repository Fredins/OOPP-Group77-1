package org.group77.mailMe.controller;

import javafx.application.*;
import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;


public class FolderItemController {
  @FXML private Label nameLabel;
  @FXML private AnchorPane root;
  @FXML private Button button;

  /**
   * 1. set initial values for nodes
   * 2. set event handlers for nodes
   */
  void init(Model model, Folder folder) {
    nameLabel.setText(folder.name());

    // input handlers
    button.setOnMouseClicked(i -> model.activeFolder.set(folder));
    // change handlers
    model.activeFolder.addListener((ChangeListener<? super Folder>) (obs, oldFolder, newFolder) -> {
      if(folder.equals(newFolder)){
        button.requestFocus();
      }
    });
  }
}
