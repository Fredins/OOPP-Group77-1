package org.group77.mailMe.controller;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import org.group77.mailMe.model.Control;
import org.group77.mailMe.model.data.*;

public class EmailItemController {
  @FXML private AnchorPane root;
  @FXML private Label fromLabel;
  @FXML private Label subjectLabel;
  @FXML private Button button;

  /**
   * 1. set initial values for nodes
   * 2. set event handlers for nodes
   * @param control the model
   * @param email the corresponding email
   * @author David, Martin
   */
  void init(Control control, Email email) {
    fromLabel.setText(email.from());
    subjectLabel.setText(email.subject());

    // input handler
    button.setOnMouseClicked(inputEvent -> {
      control.setReadingEmail(null); //Need to first set it to null because otherwise it does not count as a newEmail when we want to render the email in readingView
      control.setReadingEmail(email);});

    // change handler
    control.getReadingEmail().addObserver(newEmail -> {
      if(email.equals(newEmail)){
        button.getStyleClass().add("dropshadow");
      }else{
        button.getStyleClass().remove("dropshadow");
      }
    });
  }
}
