package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.control.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

import java.util.*;

public class FolderItemController {
  @FXML private Label nameLabel;

  void init(Model model, Folder f) {
    nameLabel.setText(f.name());

    // input handlers
    nameLabel.setOnMouseClicked(i -> model.visibleEmails.setAll(Arrays.asList(f.emails())));
  }
}
