package org.group77.mailMe.controller;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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

    // attach event handlers
    button.setOnMouseClicked(inputEvent ->  control.setReadingEmail(email));
    control.getActiveEmail().addObserver(newEmail -> dropShadow(newEmail, email));
  }

  /**
   * @author Martin
   * @param newEmail the new email
   * @param email the email associated with this controller
   */
  private void dropShadow(Email newEmail, Email email){
    if(email.equals(newEmail)){
      button.getStyleClass().add("dropshadow");
    }else{
      button.getStyleClass().remove("dropshadow");
    }

  }
}
