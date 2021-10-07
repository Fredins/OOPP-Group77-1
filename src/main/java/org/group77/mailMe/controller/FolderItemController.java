package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;


public class FolderItemController {
  @FXML private Label nameLabel;
  @FXML private Button button;

  /**
   * 1. set initial values for nodes
   * 2. set event handlers for nodes
   * @param model the model
   * @param folder the corresponding folder
   * @author Martin
   */
  void init(Model model, Folder folder) {
    nameLabel.setText(folder.name());
    button.getStyleClass().add(folder.name().equals("Inbox") ? "focused" : "unfocused");

    // input handlers
    button.setOnMouseClicked(i -> model.activeFolder.set(folder));
    // change handlers
    model.activeFolder.addListener((ChangeListener<? super Folder>) (obs, oldFolder, newFolder) -> {
        button.getStyleClass().clear();
        button.getStyleClass().add(folder.equals(newFolder) ? "focused" : "unfocused");
    });
  }
}
