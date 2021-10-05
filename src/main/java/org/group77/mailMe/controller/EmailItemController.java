package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import org.group77.mailMe.model.*;
import org.group77.mailMe.model.data.*;

public class EmailItemController {
  @FXML private AnchorPane root;
  @FXML private Label fromLabel;
  @FXML private Label subjectLabel;

  /**
   * 1. set initial values for nodes
   * 2. set event handlers for nodes
   */
  void init(Model model, Email email) {
    fromLabel.setText(email.from());
    subjectLabel.setText(email.subject());

    // input handler
    root.setOnMouseClicked(inputEvent -> model.readingEmail.set(email));
  }
}
