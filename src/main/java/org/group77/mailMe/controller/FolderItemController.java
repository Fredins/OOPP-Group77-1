package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;


public class FolderItemController {
  @FXML private Label nameLabel;
  @FXML private AnchorPane root;

  void init(Model model, Folder f) {
    nameLabel.setText(f.name());

    // input handlers
    root.setOnMouseClicked(i -> model.activeFolder.set(f));
  }
}
